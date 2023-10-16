# nj-crawler



## How to use it

This was a clumsy quick POC.

We cannot use a frontend directly, because ÖBB blocks cors requests. Hence this is a backend service.

```
❗ This server MUST NOT be used for serving a frontend. It is in no way efficient and would unnecessarily strain the API.
```

### Endpoints

/stationsBulk

/nightjet

/nightjetBulk


## Things to do

1. add caching
2. add a "window distance" for the trains that do not run every night
3. handle "Damenabteil"

## Shortcomings of the API

1. at the moment there could be places only available in a "Damenabteil" which could not be booked by men
2. we do not know the amount of places in a certain comfort class remaining

## Technology 

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

