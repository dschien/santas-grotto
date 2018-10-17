# Santa's Grotto App
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fdschien%2Fsantas-grotto.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fdschien%2Fsantas-grotto?ref=badge_shield)



# Dependencies
* Java 8
* Maven
* Google Maps API (configuration below)
* Postgresql 

## Google Maps API
* Get key: [Google developers API](https://developers.google.com/maps/documentation/javascript/get-api-key)
* Store key in `~/.secret.properties` (referenced in [here](https://github.com/dschien/santas-grotto/blob/8832cdd7fbd1031d0f13cc1f5aac32c5a179f589/src/main/java/ac/uk/bristol/cs/santa/grotto/SantasGrottoApp.java#L8))

# Development
* get the repo  
`git clone git@github.com:dschien/santas-grotto.git && cd santas-grotto`

## Configure Database
(installation instructions [here](https://www.postgresql.org/download/))

**with `psql` client**
* create user
`CREATE ROLE santa WITH LOGIN PASSWORD 'hohoho’;`
* create db
`CREATE DATABASE grottodb;`
`GRANT ALL PRIVILEGES ON DATABASE grottodb TO santa;`

## Run
`mvn spring-boot:run`

Open in the browser:
[http://localhost:8080/](http://localhost:8080/)

## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fdschien%2Fsantas-grotto.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fdschien%2Fsantas-grotto?ref=badge_large)