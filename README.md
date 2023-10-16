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
3. gradle sync 
4. run main function in NjCrawlerApp.kt

## How to use the tool

data/NightjetData.kt contains all data about stations and trains.

Currently there is no commandline interface or anything, so you have to edit the 
main function to use different functionality.

### Add new train connection

1. fetch the hafas ids for the stations
2. add the station definitions in data/NightjetData.kt
3. add a train connection using departure station, destination station and train number
4. optional: add the train connection to either one of the lists of trains
5. add a list containing the train connection to the list of trains to fetch in the main function

### Fetch nightjet data

1. select the trains you want to fetch by adding them to the main function
2. select the date 
3. select the number of trains to fetch (due to API restrictions it must be divisible by 3)

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

