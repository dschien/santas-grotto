# Santa's Grotto App


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