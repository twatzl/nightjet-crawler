# Nightjet Crawler

This app is crawling

## Warum gibt es diese App

Ich fahre gerne mit dem Nachtzug, doch leider hatte ich oft das Problem, dass die gewählten Verbindungen
ausgebucht oder nicht verfügbar waren.

Doch oft ist es nicht so wichtig an welchem Tag man genau fährt oder ob man vielleicht eine alternative
Route nimmt mit einem anderen Nachtzug und so stellen sich die folgenden Fragen:
- In welchen Nachtzügen gibt es noch freie Plätze?
- Gibt es vielleicht vor oder nach meinem geplanten Reisetag noch freie Plätze?
- Kann ich mein Ziel auch mit einer anderen Verbindung erreichen auf der es noch freie Plätze gibt?

## Setup 

Recommended development environment is IntelliJ or any other gradle compatible IDE.

1. git clone
2. open in IntelliJ
3. gradle sync (or run `./gradlew build`)
4. run main function in NjCrawlerApp.kt

## How to use the tool

All data about stations and trains is stored in `./data/NightjetData.kt` and `./data/EuropeanSleeperData.kt`.

Currently, there is no commandline interface or anything, so you have to edit the 
main function in `./app/NjCrawlerApp.kt` to use different functionality.

### Add new NightJet connection

1. Fetch the HAFAS IDs for the stations.
2. Add the station definitions in `./data/NightjetData.kt`.
3. Add a train connection using departure station, destination station, and train number.
4. Optional: add the train connection to either one of the lists of trains.
5. Add a list containing the train connection to the list of trains to fetch in the main function.

### Add new European Sleeper connection
1. Visit [europeansleeper.eu](https://www.europeansleeper.eu) and inspect the source code of the departure or arrival station input field to find all station IDs.
2. Add the station definitions in `./data/EuropeanSleeperData.kt`.
3. see steps 3. - 5. above

### Fetch data

1. Select the trains you want to fetch by modifying the data files accordingly.
2. In `./app/NjCrawlerApp.kt`:
   1. Select whether to write individual files for each train, one combined file, or both.
   2. Select the number of trains to fetch (due to API restrictions it must be divisible by 3).
   3. Run the main function.
3. The output is stored in `./data/combined/`.

‼ PLEASE USE THE TOOL RESPONSIBLY. DO ONLY REQUEST THE DATA YOU ABSOLUTELY NEED. ‼

## Feature Roadmap

- use ÖBB ticket shop api to get more information  
- handle "Damenabteil"  
- add some kind of sliding "window distance" for the trains that do not run every night
- add other night trains
- combine pricing data
- automate data fetching
- write a UI

## Shortcomings of the nightjet.com API

1. at the moment there could be places only available in a "Damenabteil" which could not be booked by men
2. we do not know the amount of places in a certain comfort class remaining
3. nightjet.com cannot properly handle capsules (mini cabins) and "comfort plus" sleeper cabins

## Contributing

1. create an issue or comment on an existing issue
2. fork the repo and work on it
3. create a pull request

