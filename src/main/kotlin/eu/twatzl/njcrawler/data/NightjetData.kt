package eu.twatzl.njcrawler.data

import eu.twatzl.njcrawler.model.Station
import eu.twatzl.njcrawler.model.TrainConnection

val wien = Station("WIEN", "1190100")
val innsbruck = Station("Innsbruck Hbf", "8100108")
val bregenz = Station("Bregenz", "8100090")
val munich = Station("MÜNCHEN", "8081998")
val zurich = Station("ZÜRICH", "8596008")
val parisEst = Station("Paris Est", "8700011")
val praha = Station("Praha hl.n.", "5400014")
val amsterdam = Station("Amsterdam Centraal", "8400058")
val hamburg = Station("Hamburg Hbf", "8002549")
val brussel = Station("Brüssel Midi", "8800004")
val rom = Station("Roma Termini", "8300263")
val laSpezia = Station("La Spezia Centrale", "8300156")
val livorno = Station("Livorno Centrale", "8300157")
val venedig = Station("Venedig Santa Lucia", "8396008")
val ancona = Station("Ancona", "8300186")
val zagreb = Station("Zagreb Glavni kolodvor", "7800020")
val rijeka = Station("Rijeka", "7800013")
val budapest = Station("BUDAPEST", "5596001")
val bratislava = Station("BRATISLAVA", "5696001")
val warschau = Station("WARSZAWA", "5196003")
val stuttgart = Station("Stuttgart Hbf", "8000096")
val split = Station("Split", "7800016")
val graz = Station("Graz Hbf", "8100173")
val kiew = Station("Kiew Pass", "2200005")
val berlin = Station("BERLIN", "8096003")

// ************************************************
// ** NJ AB WIEN ÖSTERREICH WESTEN
// ************************************************

val nj446 = TrainConnection("NJ 446", wien, bregenz) //  Wien – Linz – Innsbruck – Feldkirch – Bregenz
val nj466 = TrainConnection("NJ 466", wien, zurich) //  Wien – Linz – Innsbruck – Feldkirch – Zürich
val nj468 =
    TrainConnection("NJ 468", wien, parisEst) //  Wien – Linz – Salzburg – München Ost – Karlsruhe – Strasbourg – Paris
val nj40420 = TrainConnection("NJ 40420", innsbruck, hamburg) // Innsbruck – München – Nürnberg – Hamburg
val nj420 = TrainConnection("NJ 420", innsbruck, amsterdam) // Innsbruck – München – Nürnberg – Amsterdam

val njATnachWest = listOf(
    nj446,
    nj466,
    nj468,
    nj420,
    nj40420,
)

// ************************************************
// ** NJ NACH ÖSTERREICH VON WESTEN
// ************************************************

val nj447 = TrainConnection("NJ 447", bregenz, wien) //  Wien – Linz – Innsbruck – Feldkirch – Bregenz
val nj467 = TrainConnection("NJ 467", zurich, wien) //  Wien – Linz – Innsbruck – Feldkirch – Zürich
val nj469 = TrainConnection(
    "NJ 469",
    parisEst,
    wien
)     //  Wien – Linz – Salzburg – München Ost – Karlsruhe – Strasbourg – Paris
val nj465 = TrainConnection("NJ 465", zurich, graz) // Graz – Leoben – Innsbruck – Feldkirch – Zürich
val nj40491 = TrainConnection("NJ 40491", hamburg, innsbruck) // Innsbruck – München – Nürnberg – Hamburg
val nj421 = TrainConnection("NJ 421", amsterdam, innsbruck) // Innsbruck – München – Nürnberg – Amsterdam

val njATVonWest = listOf(
    nj447,
    nj467,
    nj469,
    nj465,
    nj40491,
    nj421,
)

// ************************************************
// ** NJ AB ÖSTERREICH RICHTUNG NORDEN
// ************************************************

val nj490 = TrainConnection("NJ 490", wien, hamburg) //  Wien – Linz – Nürnberg – Hamburg
val nj40490 = TrainConnection("NJ 40490", wien, amsterdam) //  Wien – Linz – Nürnberg – Amsterdam
val nj40468 = TrainConnection(
    "NJ 40468",
    wien,
    brussel
) //  Wien – Linz – Nürnberg – Frankfurt – Bonn – Aachen – Lüttich – Brüssel
val nj456 = TrainConnection(
    "NJ 456",
    graz,
    berlin
)   //     Graz – Wien – Břeclav – Praha - Dresden – Berlin

val njATNachNord = listOf(
    nj490,
    nj40490,
    nj40468,
    nj456,
)

// ************************************************
// ** NJ NACH ÖSTERREICH VON NORDEN
// ************************************************


val nj40421 = TrainConnection("NJ 40421", amsterdam, wien)   //  Wien – Linz – Nürnberg – Amsterdam
val nj491 = TrainConnection("NJ 491", hamburg, wien)     //  Wien – Linz – Nürnberg – Hamburg
val nj40425 = TrainConnection("NJ 40425", brussel, wien)
val nj457 = TrainConnection(
    "NJ 457",
    berlin,
    graz
)      //  Graz – Wien – Břeclav – Praha - Dresden – Berlin
val en50237 = TrainConnection(
    "EN 50237",
    stuttgart,
    wien
)    //  MÁV (Nightjet Partner)  Budapest – Wien – Linz – Salzburg – München Ost – Stuttgart  Nein

val njATVonNord = listOf(
    nj40421,
    nj491,
    nj40425,
    nj457,
    en50237,
)

// ************************************************
// ** NJ AB ÖSTERREICH RICHTUNG SÜDEN
// ************************************************

val nj233 = TrainConnection("NJ 233", wien, laSpezia)   //  Wien – Villach – Verona – Mailand – Genua – La Spezia
val nj40233 = TrainConnection("NJ 40233", wien, rom) //  Wien – Villach – Florenz – Rom  Nein
val nj40466 = TrainConnection("NJ 40466", wien, venedig) //  Wien – Linz – Salzburg – Venedig

// baustellenbedingter umleiter im sommer 2023
// val nj40233 = TrainConnection("NJ 40233", wien, ancona) //  Wien – Villach – Florenz – Rom  Nein

// Karteileiche auf Wikipedia
// val nj1237 = TrainConnection("NJ 1237", wien, livorno),  //  Wien – Villach – Florenz – Livorno

val njATNachSued = listOf(
    nj233,
    nj40233,
    nj40466,
)

// ************************************************
// ** NJ NACH ÖSTERREICH VON SÜDEN
// ************************************************

val nj40294 = TrainConnection("NJ 40294", rom, wien)    //  Wien – Villach – Florenz – Rom  Nein

// war baustellenbedingt umleiter im sommer 2023
// val nj40294 = TrainConnection("NJ 40294", ancona, wien),    //  Wien – Villach – Florenz – Ancona (Baustellnumleitung)  Nein
val nj235 = TrainConnection("NJ 235", laSpezia, wien)      //  Wien – Villach – Verona – Mailand – Genua – La Spezia

//TrainConnection("NJ 1234"),  //  Wien – Villach – Florenz – Livorno
val nj40236 = TrainConnection("NJ 40236", venedig, wien)    //  Wien – Linz – Salzburg – Venedig

val njATVonSued = listOf(
    nj235,
    nj40236,
    nj40294,
)

// ************************************************
// ** NJ AB SCHWEIZ
// ************************************************

val en40462 = TrainConnection("EN 40462", budapest, zurich) //   Budapest – Wien – Linz – Innsbruck – Feldkirch – Zürich
val nj402 = TrainConnection("NJ 402", zurich, amsterdam)  // Zürich – Basel – Bonn – Amsterdam
val en50467 = TrainConnection(
    "EN 50467",
    zurich,
    esPraha
) // ČD (Nightjet Partner)  Zürich – Feldkirch – Salzburg – Linz – Prag  Nein
val en40467 =
    TrainConnection("EN 40467", zurich, budapest)    //  Budapest – Wien – Linz – Innsbruck – Feldkirch – Zürich
val nj409 = TrainConnection("NJ 409", zurich, berlin) // Berlin – Halle – Leipzig – Mannheim – Basel – Zürich
val nj470 = TrainConnection("NJ 470", zurich, hamburg) // Hamburg – Hannover – Mannheim – Basel – Zürich
val en40465 = TrainConnection(
    "EN 40465",
    zurich,
    zagreb
) // HŽ (Nightjet Partner)  Zagreb – Ljubljana – Villach – Feldkirch – Zürich  Nein
val en40459 = TrainConnection("EN 40459", zurich, esPraha) // Zürich - Karlsruhe - Frankfurt - Leipzig - Prag

val njAbCH = listOf(
    en40462,
    nj402,
    en50467,
    en40467,
    nj409,
    nj470,
    en40465,
    en40459,
)

// ************************************************
// ** NJ NACH SCHWEIZ
// ************************************************

val nj408 = TrainConnection("NJ 408", berlin, zurich) // Berlin – Halle – Leipzig – Mannheim – Basel – Zürich
val nj464 = TrainConnection("NJ 464", graz, zurich) // Graz – Leoben – Innsbruck – Feldkirch – Zürich
val nj471 = TrainConnection("NJ 471", hamburg, zurich) // Hamburg – Hannover – Mannheim – Basel – Zürich  Ja
val en40414 = TrainConnection(
    "EN 40414",
    zagreb,
    zurich
) // HŽ (Nightjet Partner)  Zagreb – Ljubljana – Villach – Feldkirch – Zürich  Nein
val nj403 = TrainConnection("NJ 403", amsterdam, zurich)
val en50466 = TrainConnection(
    "EN 50466",
    esPraha,
    zurich
) // ČD (,Nightjet Partner)  Zürich – Feldkirch – Salzburg – Linz – Prag  Nein
val en40458 = TrainConnection("EN 40458", esPraha, zurich) // Zürich - Karlsruhe - Frankfurt - Leipzig - Prag

val njNachCH = listOf(
    nj408,
    nj464,
    nj471,
    en40414,
    nj403,
    en50466,
    en40458,
)

// ************************************************
// ** REST
// ************************************************

val nj295 = TrainConnection("NJ 295", munich, rom) // München – Salzburg – Villach – Florenz – Rom
//val nj295 = TrainConnection("NJ 295", munich, ancona), // München – Salzburg – Villach – Florenz – Rom
val nj40295 = TrainConnection(
    "NJ 40295",
    munich,
    laSpezia
) // München – Salzburg – Villach – Verona – Mailand – Genua – La Spezia
val nj237 = TrainConnection(
    "NJ 237",
    stuttgart,
    venedig
) // Stuttgart – München Ost – Salzburg – Villach – Udine – Treviso – Venedig
val en40237 =
    TrainConnection("EN 40237", stuttgart, zagreb) // Stuttgart – München Ost – Salzburg – Villach – Ljubljana – Zagreb
val en60237 = TrainConnection(
    "EN 60237",
    stuttgart,
    rijeka
) // Stuttgart – München Ost – Salzburg – Villach – Ljubljana – Opatija – Rijeka
val en1153 = TrainConnection(
    "EN 1153",
    bratislava,
    split
) // Bratislava - Wien - Graz - Maribor - Zagreb - Split
val en40476 = TrainConnection(
    "EN 40476",
    budapest,
    berlin
) // Budapest – Bratislava – Břeclav – Bohumín – Racibórz – Wrocław – Frankfurt/Oder – Berlinval
val en50462 = TrainConnection(
    "EN 50462",
    budapest,
    stuttgart
) //   MÁV (Nightjet Partner)  Budapest – Wien – Linz – Salzburg – München Ost – Stuttgart
val en414 =
    TrainConnection("EN 414", zagreb, stuttgart) // Stuttgart – München Ost – Salzburg – Villach – Ljubljana – Zagreb
val en480 = TrainConnection(
    "EN 480",
    rijeka,
    stuttgart
) // Stuttgart – München Ost – Salzburg – Villach – Ljubljana – Opatija – Rijeka
val en1152 = TrainConnection(
    "EN 1152",
    split,
    bratislava
) // Bratislava - Wien - Graz - Maribor - Zagreb - Split
val en40457 = TrainConnection(
    "EN 40457",
    berlin,
    budapest
) // Budapest – Bratislava – Břeclav – Bohumín – Racibórz – Wrocław – Frankfurt/Oder – Berlin

val nj294 = TrainConnection("NJ 294", rom, munich) // München – Salzburg – Villach – Florenz – Rom

//val nj294 = TrainConnection("NJ 294", ancona, munich), // München – Salzburg – Villach – Florenz – Rom
val nj40235 = TrainConnection(
    "NJ 40235",
    laSpezia,
    munich
) // München – Salzburg – Villach – Verona – Mailand – Genua – La Spezia
val nj236 = TrainConnection(
    "NJ 236",
    venedig,
    stuttgart
) // Stuttgart – München Ost – Salzburg – Villach – Udine – Treviso – Venedig

val nj424 = TrainConnection("NJ 424", berlin, brussel) // Berlin – Frankfurt - Köln - Brüssel
val nj425 = TrainConnection("NJ 425", brussel, berlin) // Berlin – Frankfurt - Köln - Brüssel
val nj40424 = TrainConnection("NJ 40424", berlin, parisEst) // Berlin - Erfurt - Frankfurt - Strasbourg - Paris
val nj40469 = TrainConnection("NJ 40469", parisEst, berlin) // Berlin - Erfurt - Frankfurt - Strasbourg - Paris
val en406 = TrainConnection(
    "EN 406",
    munich,
    warschau
) //   PKP (Nightjet Partner) München - Salzburg - Wien – Ostrava – Bohumín – Krakau – Warschau
val en407 = TrainConnection(
    "EN 407",
    warschau,
    munich
)   //  PKP (Nightjet Partner)  München - Salzburg - Wien – Ostrava – Bohumín – Krakau – Warschau

val njRest = listOf(
    nj40295,
    nj237,
    en60237,
    en1153,
    en40237,
    en40476,
    en50462,
    en414,
    en480,
    en1152,
    en40457,
    nj294,
    nj295,
    nj40235,
    nj236,
    nj424,
    nj425,
    nj40424,
    nj40469,
    en406,
    en407,
)

// ÖBB does not sell tickets for these connections...

//    TrainConnection("D 40149",
//        wien,
//        kiew), // Wien - Kiew

//    TrainConnection("D 40749",
//        kiew,
//        wien), // Wien - Kiew


val allNightjets = listOf(
    njATnachWest,
    njATVonWest,
    njATNachNord,
    njATVonNord,
    njATNachSued,
    njATVonSued,
    njAbCH,
    njNachCH,
    njRest
).flatten()