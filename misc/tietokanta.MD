# TIETOKANTA

Mobility Profilessa käytetään SQLite tietokantaa Sugar ORM:in kautta.
Mobility Profile ennustaa määränpäitä perustuen käyttäjän 1) kalenterimerkintöihin ja 2) sijaintihistoriaan. 

Kalenterimerkintöihin perustuvat ehdotukset
-------------------------------------------

Kalenterimerkintöjen sijainnit useimmiten eivät ole oikeita osoitteita, joten Mobility Profilen pitää jotenkin käsitellä tilanteet missä kalenterimerkinnän sijainti on esim. “A307” (luokkahuone Exactumissa).

### Tietokantataulut:

**CalendarTag**
* key:String
* value:String
* counter:int 

**Skenaario: Kalenterimerkinnässä sijaintina “A307” ensimmäisen kerran**
- Käyttäjä avaa reittioppaan
- Mobility Profile hakee CalendarTag taulusta rivit joissa key=“A307”
- Jos rivejä ei ole Mobility Profile tekee määränpääehdotuksen “A307”
- Käyttäjä korjaa “Kumpulan kampus” ja tekee reittihaun
- Reittiopas lähettää “Kumpulan kampus” takaisin Mobility Profilelle
- Mobility Profile hakee CalendarTag-taulusta rivin jossa key=“A307” ja value=“Kumpulan kampus” tai luo sellaisen jos ei ole olemassa
- Kyseistä riviä päivitetään lisäämällä counterin arvoa yhdellä

**Skenaario: Kalenterimerkinnässä sijantina “A307” myöhemmin**
- Käyttäjä avaa reittioppaan
- Mobility Profile hakee CalendarTag taulusta rivit joissa key=“A307” ja järjestää ne counter-arvon mukaan laskevaan järjestykseen
- Mobility Profile tekee määränpääehdotuksen missä määränpäänä on sen rivin value-kentän arvo jonka counter-arvo oli suurin (Oletetaan “Kumpulan kampus”)
- Reittiopas lähettää “Kumpulan kampus” takaisin Mobility Profilelle
- Mobility Profile hakee CalendarTag-taulusta rivin jossa key=“A307” ja value=“Kumpulan kampus” ja päivittää riviä lisäämällä counterin arvoon +1

**Skenaario: Kalenterimerkinnässä sijaintina “Kumpulan kampus” ja käyttäjä on menossa sinne**
- Jos CalendarTag-taulussa ei ole riviä jossa key=“Kumpulan kampus” niin Mobility Profile tekee määränpääehdotuksen “Kumpulan kampus”
- Jos käyttäjä hyväksyy ehdotuksen reittiopas palauttaa “Kumpulan kampus” takaisin Mobility Profilelle
- Mobility Profile tekee CalendarTag-tauluun rivin jossa key=“Kumpulan kampus” value=“Kumpulan kampus” ja counter==1

**Skenaario: Kalenterimerkinnässä sijaintina “Kumpulan kampus” mutta käyttäjä ei ole menossa kalenterin mukaiseen tapahtumaan**
- Mobility Profile hakee CalendarTag-taulusta määränpääehdotuksen kuten aikaisemmin (Oletetaan että ehdotus on “Kumpulan kampus”)
- Käyttäjä lähteekin osoitteeseen “Mannerheimintie 100” ja kirjoittaa sen reittioppaaseen
- Reittiopas palauttaa “Mannerheimintie 100” Mobility Profilelle
- Mobility Profile tekee CalendarTag-tauluun rivin jossa key=“Kumpulan kampus” value=“Mannerheimintie 100” counter==1

Skenaario 3:ssa tietokantaan tulee siis “virheellinen” tag, mutta sillä ei ole merkitystä jos tietokannassa on jo rivi missä esimerkiksi key=“Kumpulan kampus” value=“Kumpulan kampus” counter=100. 




Sijaintihistoriaan perustuvat ehdotukset
-------------------------------------------

Mobility Profile rakentaa käyttäjän sijaintihistorian perustuen GPS-koordinaatteihin, GPS-koordinaateista Pelias Geocoderin (https://mapzen.com/projects/search/) kautta haettuihin osoitteisiin, ja aikaleimoihin. 


Tietokantataulut:

**Location**
* address(String?)

**Visit**
* timestamp(datetime)
* location(fk)
* next visit(fk)

**Skenaario: Ohjelma rakentaa käyttäjän sijantihistoriaa**
- Joka x minuutin välein kysytään koordinaatit GPS:ltä
- Reverse geocoodataan koordinaatit Pelias API:n kautta lähimmäksi osoitteeksi
- Jos osoitetta ei ole jo tietokannassa luodaan Location tauluun rivi, missä address=“peliaksen kautta haettu osoite”
- Haetaan Visit taulusta uusin rivi tai luodaan sellainen jos ei ole vielä olemassa
- Päivitetään haettua riviä niin että timestamp=now location-sarakkeeseen tulee viite aluksi haettuun Location-taulun riviin, ja next visit sarakkeeseen laitetaan viite uuteen Visit riviin jossa taas timestamp(now) (Tästä tulee siis seuraavan kerran “uusin rivi”)

**Skenaario: Ennustetaan seuraava määränpää jos ei ole kalenterimerkintää (seuraavan kahden tunnin sisällä...?)**
- Haetaan Visit-taulusta kaikki rivit joissa location on sama kuin se osoite minkä Pelias antaa nykyisillä GPS-koordinaateilla
- Käydään rivien next visit viitteet läpi ja rakennetaan taulukko missä yhtenä sarakkeena on next visitin viittaaman location rivin address kenttä ja toisena counter johon lasketaan kuinka monta kertaa kyseinen address esiintyy
- Mobility Profile tekee määränpääehdotuksen siihen osoitteeseen jonka counter oli suurin (Oletetaan “Kumpulan kampus”)

CalendarTag taulua ei tarvitse päivittää, koska kaikki Location-taulusta haetut osoitteet lähtökohtaisesti ovat valideja osoitteita. 
Jos käyttäjä ei hyväksykään ehdotusta niin ei tarvitse tehdä mitään korjaavia toimenpiteitä, koska jonkin ajan päästä käyttäjä tuottaa uuden Visit-rivin sinne minne oli oikeasti menossa, jolloin kyseinen rivi vaikuttaa seuraaviin ennusteisiin. 

Visit-taulun next visit sarakkeen idea on siinä, että vaikka saman toiminnallisuuden saisi aikaan pelkillä timestamp ja location sarakkeilla, niin oletettavasti on helpompi tuotaa tehokkaat tietokantakyselyt jos on suoraan viite next visitiin. Ehkä turhaa.

Myöhemmin ennustetta voi tarkentaa esim. niin, että “Haetaan Visit-taulusta kaikki rivit joissa location on sama kuin se osoite minkä Pelias antaa nykyisillä GPS-koordinaateilla” + “ja joissa kellonaika = nyt +/- 2h”, tai vastaavalla tyylillä.

VIRHEITÄ:
Visit-rivi pitäisi luoda vain osoitteisiin, joissa käyttäjä varsinaisesti on käymässä, ei esim. bussimatkan varrella oleviin osoitteisiin. Mutta jotkut validit visit voivat olla kuitenkin lyhyitä, joten korjaus jossa oletetaan että käyttäjän pitää olla esim. samassa osoitteessa 10 minuuttia ennenkuin rekisteröinti tapahtuu ei välttämättä ole hyvä myöskään.












