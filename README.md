[![CircleCI](https://circleci.com/gh/dschien/santas-grotto.svg?style=svg&circle-token=7d61325169687c2a3091138240b672604e77a912)](https://circleci.com/gh/dschien/santas-grotto) [![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fdschien%2Fsantas-grotto.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fdschien%2Fsantas-grotto?ref=badge_shield)

Please note that this project is released with a Contributor Code of Conduct. By participating in this project you agree to abide by its terms.

# Santa's Grotto App

An end-to-end demonstrator of a web application based on (Spring)[https://spring.io/]. 

# Dependencies
* Java 11 
* Maven
* Google Maps Geolocation API (optional - configuration below)
* DB - (optional - example with Postgres below)

# Deployment

## Development

Install the above dependencies on your machine.

* get the repo  
`git clone git@github.com:dschien/santas-grotto.git && cd santas-grotto`

## Run with 'dev' environment
`SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run`


## Option - Google Maps API
* Get key: [Google developers API](https://developers.google.com/maps/documentation/javascript/get-api-key)
* Store key in `~/.secret.properties` (referenced in [here](https://github.com/dschien/santas-grotto/blob/8832cdd7fbd1031d0f13cc1f5aac32c5a179f589/src/main/java/ac/uk/bristol/cs/santa/grotto/SantasGrottoApp.java#L8))

This demonstrates externalisation of credentials - don't submit credentials to the VCS.    

## Option - Configure Database

In the `dev` profile an in-memory DB is used. It's easy to get going but you'll loose DB contents when your reload the app. 
Better a RDBMS.

For example, the `prod` environment (uses a Postgres DB)(https://github.com/dschien/santas-grotto/blob/7bd1d7bc12bd80ff2d29684f980008eedbcff45b/src/main/resources/application-prod.yml#L10).  

Official instructions are [here](https://www.postgresql.org/download/).

### Dockerised Postgres 

Alternatively, you can run the DB in [Docker](https://www.docker.com/get-started). It will keep the DB in a nicely contained environment without installing PG in your host system.
To install, follow the easy installation instructions.
  
  
The run the following command to run a PG container. Don't forget to replace the 'XXX' with something meaningful.  
```bash
docker run --rm -p 5432:5432 \
-e POSTGRES_PASSWORD=XXX -e POSTGRES_USER=XXX \
--name postgres-db \
-v /tmp/pgdata:/var/lib/postgresql/data
-d postgres:latest
```

This
 
- starts a container from image `postgres:latest`, 
- runs it in deamon mode (`-d`), stores the DB contents to a folder `/tmp/pgdata` (`-v /tmp/pgdata:/var/lib/postgresql/data`)  on your host - 
so you don't loose your data when your container restarts. 
- passes  `POSTGRES_PASSWORD` and name as environment variables 
- opens the default port `5432` from the host
- will remove the container when stopped (`-rm`)  

You still need to create DB user and a DB. You can do that via the `psql` client in the docker container: 
`docker run -it --rm --link postgres postgres /bin/bash`
(note the `--link postgres` switch so that your container can reach the DB container)

Create a user  - here called 'santa'.
`createuser santa -P --createdb -h postgres -U root`

Make sure the user matches the [name in the config file](https://github.com/dschien/santas-grotto/blob/7bd1d7bc12bd80ff2d29684f980008eedbcff45b/src/main/resources/application-prod.yml#L12).
and matches the password in your `.secret.properties` file (see above). For an example of the file structure look [here](https://github.com/dschien/santas-grotto/blob/master/src/main/resources/default.properties). 

Then create a DB for your user: 
`createdb grottodb -U santa -h postgres` 

You can achive the same with the following **with `psql` client**:
* create user
`CREATE ROLE santa WITH LOGIN PASSWORD 'hohoho';`
* create db
`CREATE DATABASE grottodb;`
`GRANT ALL PRIVILEGES ON DATABASE grottodb TO santa;`

## Run
`mvn spring-boot:run`

Open in the browser:
[http://localhost:8080/](http://localhost:8080/)

*CONGRATULATIONS* if you made it to here!

# Deployment

[Create a systemd service for Spring](https://www.baeldung.com/spring-boot-app-as-a-service)
 
## SSL certs

### With Let's encrypt:
https://certbot.eff.org/lets-encrypt/ubuntubionic-nginx

### SSL Termination with Nginx

server {

        index index.html index.htm index.nginx-debian.html;

        server_name YOURNAME HERE;

        location / {
                proxy_pass http://127.0.0.1:8080;
        }



    listen [::]:443 ssl ipv6only=on; # managed by Certbot
    listen 443 ssl; # managed by Certbot
    ssl_certificate /etc/letsencrypt/live/cms.spe-hub.net/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/cms.spe-hub.net/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot



}


server {
    if ($host = YOUR NAME HERE) {
        return 301 https://$host$request_uri;
    } # managed by Certbot


        listen 80 default_server;
        listen [::]:80 default_server;

        server_name YOUR NAME HERE;
        return 404; # managed by Certbot


}
