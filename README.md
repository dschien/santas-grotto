[![CircleCI](https://circleci.com/gh/dschien/santas-grotto.svg?style=svg&circle-token=7d61325169687c2a3091138240b672604e77a912)](https://circleci.com/gh/dschien/santas-grotto) [![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fdschien%2Fsantas-grotto.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fdschien%2Fsantas-grotto?ref=badge_shield)

Please note that this project is released with a Contributor Code of Conduct. By participating in this project you agree to abide by its terms.

# Santa's Grotto App

An end-to-end demonstrator of a web application based on (Spring)[https://spring.io/]. 

# TOC

* [Santa's Grotto App](#santas-grotto-app)
* [TOC](#toc)
* [Dependencies](#dependencies)
* [Deployment](#deployment)
  * [Development](#development)
  * [Run with 'dev' environment](#run-with-dev-environment)
  * [Option - Google Maps API](#option---google-maps-api)
  * [Option - Configure Database](#option---configure-database)
     * [Dockerised Postgres](#dockerised-postgres)
  * [Run](#run)
* [Deployment](#deployment-1)
  * [SSL certs](#ssl-certs)
     * [With Let's encrypt:](#with-lets-encrypt)
     * [SSL Termination with Nginx](#ssl-termination-with-nginx)
* [Repository Structure and "Spring Cookbook"](#repository-structure-and-spring-cookbook)
  * [Controllers](#controllers)
     * [CustomErrorController](#customerrorcontroller)
     * [WebController](#webcontroller)
     * [EventBookingController, EventController, GrottoController](#eventbookingcontroller-eventcontroller-grottocontroller)
* [A Form with Spring Step-by-Step](#a-form-with-spring-step-by-step)
  * [HTML Forms](#html-forms)
        * [CSRF protection](#csrf-protection)
     * [Server Side](#server-side)
        * [GET Handler](#get-handler)
        * [Whitelist in Security Controller](#whitelist-in-security-controller)
        * [POST Handler](#post-handler)
        * [Data Binding](#data-binding)
        * [Repositories](#repositories)
        * [Redirects](#redirects)
        * [Success Message](#success-message)
        * [Validation](#validation)
        * [Style](#style)
  * [REST APIs](#rest-apis)
     * [Simple Example](#simple-example)
     * [Try on the command line](#try-on-the-command-line)


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

#### Create Network
If you would like to also run the Spring App in docker, then you should create a new network for them:
`docker network create sg-net`

Then run the following command to run a PG container. Don't forget to replace the passwords with something secret.  
```bash
docker run -p 5432:5432 -v /some/folder/to/store/DB/files:/var/lib/postgresql/data --env POSTGRES_PASSWORD=mysecretpassword --env POSTGRES_USER=santa --env POSTGRES_DB=santa_db --name postgres --network sg-net --network-alias postgres postgres 
```

This
 
- starts a container from image `postgres:latest`, 
- stores the DB contents to a folder `/some/folder/to/store/DB/files` (`-v /some/folder/to/store/DB/files:/var/lib/postgresql/data`)  on your host - 
so you don't loose your data when your container restarts. 
- passes  `POSTGRES_PASSWORD` and name as environment variables 
- opens the default port `5432` from the host

This will also create a default DB (`--env POSTGRES_DB=santa_db`) and enables access to `POSTGRES_USER=santa`

If you'd like to control the DB creation, you can a create DB user and a DB via the `psql` client in the docker container: 
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

## Run spring app in docker
It is possible to also run the spring server in docker. 
There is a `Dockerfile` in the project. Let's assume you build this out to an image, with a tag of `santas_grotto`, then you can run the below to link it to the PG instance:
(this is the build command: `docker build -t santas_grotto .`)

```shell
docker run -p 8080:8080  
--env POSTGRES_PASSWORD=mysecretpassword 
--env POSTGRES_USER=santa 
--env POSTGRES_DB=santa_db 
--env SPRING_PROFILES_ACTIVE=prod 
--env PG_HOST=postgres 
--name santas_grotto 
--network sg-net 
santas_grotto
```

Open in the browser:
[http://localhost:8080/](http://localhost:8080/)

*CONGRATULATIONS* if you made it to here!

## Run in your host environment
`mvn spring-boot:run`

# Deployment

[Create a systemd service for Spring](https://www.baeldung.com/spring-boot-app-as-a-service)
 

### SSL Termination with Nginx

Simply install nginx with apt (if on ubuntu): `sudo apt-get install nginx`

Then get SSL certificates for your domain name from  
[let's encrypt](https://certbot.eff.org/lets-encrypt/ubuntubionic-nginx). The instructions are straigth forward.


You also need to make nginx forward traffic to your Spring application in Tomcat - 
Here an example nginx config file to be placed/updated in `/etc/ngingx/sites-enabled/default` 
```
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
```

### Continuous deployment
You can use the `.circleci/config.yml` file in this repository as a blueprint to have CI and CD with circleci.

One thing to notice - you need to create SSH keys in the circle console. 

I recommend you do that on the VM itself via: 
`ssh-keygen -t rsa -N "" -b "2048" -C "santa" -f santa_keys`. Then add the public key to authorized_keys: `cat santa_keys.pub >> ~/.ssh/authorized_keys`
Update the finger print in the `.circleci/config.yml` with your own key's fingerprint.

Also, create env variables for user and host for the line `... $SSH_USER@$SSH_HOST...` in `.circleci/config.yml`.

#### Spring server service
The config file deploy script expects a service for your Spring application to exist on your server. I have followed the steps in [here](https://www.baeldung.com/spring-boot-app-as-a-service) and defined 
a systemd service without problems.  

# Repository Structure and "Spring Cookbook"

For a basic tutorial on constructing a web application with Spring check [here](https://spring.io/guides/gs/serving-web-content/).


The repository has the typcial structure with 
`src/main/java/` containing your runtime sources
`src/main/resources/` containing config files and web assets

There are corresponding directories for tests.

The main package is `ac.uk.bristol.cs.santa.grotto`.

There are packages for `business` (JPA persistence), `configuration` (security), `controllers` (see below).

## Controllers 

Here is the set of controllers defined in the project.   

### `CustomErrorController`
Hooks into Springs own error handling mechanism that uses HTTP error codes. Depending on the specific code, different templates are rendered.

### WebController

- defines static templates by overriding `public void addViewControllers(ViewControllerRegistry registry) {`
- includes all REST APIs `/api/geolookup`, `/api/grottoroute` and `/api/account`
- demonstrates the use of `@Secured({"ROLE_ADMIN"})` annotations to restrict access
- demonstrates the use of  data transfer objects (DTOs) for REST call payloads
- includes a controller for a contact form 

### `EventBookingController, EventController, GrottoController`

- classic MVC controllers
- return `String` template names to render using Thymeleaf
- demonstrate use of (optional) PathVariables, Models 


# A Form with Spring Step-by-Step

The following is a detailed description of the project [contact form](https://github.com/dschien/santas-grotto/blob/master/src/main/resources/templates/contact.html) and the structure of corresponding [spring request handlers](https://github.com/dschien/santas-grotto/blob/e4225eb114fad82b9ef98b31aad8b17dda0b875e/src/main/java/ac/uk/bristol/cs/santa/grotto/MainController.java#L98). 

## HTML Forms

Below is a simple contact form. It consists of opening and closing `form` tags and one or more `input` elements. 

```html
<form action="/path" method="post" class="form">
    <label for="name">Name</label>
    <input type="text" id="nameField" name="name"/>
	<button type="submit" class="btn btn-primary">Submit</button>
</form>
```

**Note** `input` elements need to have a `name` attribute! When the form content is submitted, key-value pairs from `name` and the corresponding user input are put together. _That is what I had been missing in the lecture!_ And without the name, the user input could not be matched to a key...

Such a form is complete except for one thing - a CSRF token, which brings us to...

#### CSRF protection
Here more about [Cross-Site Request Forgery (CSRF)](https://www.owasp.org/index.php/Cross-Site_Request_Forgery_(CSRF)). Long story short, a CSRF token prevents a malicious person from abusing session cookies stored on your computer by adding to (POST/PUT/DELETE/PATCH) requests an additional piece of information that is specific to your session but is not a cookie.

If you let it, Spring will automatically insert a CSRF token into forms for you.

You do this by using the `th:action="@{` attribute in the `form` tag:

```html
<form th:action="@{/contactSubmit}" method="post" class="form">
```
Instead of the target path (`/contactSubmit`) in verbatim as in the first example above.

So here our complete contact form. 

```html
<form th:action="@{/contactSubmit}" method="post" class="form">

    <div class="form-group blu-margin">
        <label for="name">Name</label>
        <input type="text" id="name" name="name"/>
    </div>
    <div class="form-group">
        <label for="email">Email</label>
        <input type="email" id="email" name="email"/>
    </div>
    <div class="form-group">
        <label for="body">Content</label>
        <textarea class="form-control" rows="5" id="body" name="body"></textarea>
    </div>

    <button type="submit" class="btn btn-primary">Submit</button>

</form>
```

### Server Side
Once we have the template complete and stored in the `resource/templates` folder we can deal with the handler functions. On the server we need to have two end points:

1. a GET handler one for sending our contact form to the client in an inital request
2. a POST handler to deal with the contents of the form

#### GET Handler

Because the contact form needs to processing and can be sent out as is, we can just register it with the [default view controller] (https://github.com/dschien/santas-grotto/blob/e4225eb114fad82b9ef98b31aad8b17dda0b875e/src/main/java/ac/uk/bristol/cs/santa/grotto/MainController.java#L44) as :

```java
@Override
public void addViewControllers(ViewControllerRegistry registry) {
	registry.addViewController("/contact");
    ...
}
```

#### Whitelist in Security Controller
In order for visitors to our page to contact us without login we need to whitelist the path in our [`WebSecurityConfig`](https://github.com/dschien/santas-grotto/blob/c2a3ffd0b10e87d1387bd365466f686be0c4828d/src/main/java/ac/uk/bristol/cs/santa/grotto/WebSecurityConfig.java#L25).

```java
protected void configure(HttpSecurity http) throws Exception {
	http
        .authorizeRequests()
        // allow browsing index
        .antMatchers("/", "/terms", "/contact", "/contactSubmit").permitAll()
    
```

#### POST Handler
A POST handler function is any ordinary function, decorated with either 

* `@PostMapping("/path")` or
* `@RequestMapping(value = "/path", method = RequestMethod.POST)`

(there are equivalent GET handler annotations available)

Spring will provide the request data to you as annotated parameters to the method.

The most primitive option is to access the raw POST payload data through the `@RequestBody` annotation:

```java
@PostMapping(value = "/contactSubmit")
    public String contact(@RequestBody String contact) {
    logger.info(String.valueOf(contact));
    return "index";
}
```

If we entered our name and email we would see something like following in the log output
`name=Daniel+Schien&email=daniel.schien%40bristol&body=ASD&_csrf=04c9a1e7-cdb6-408a-a0fb-0d1a16393413
` ... note the CSRF token...

This is the default html form encoding. The alternative would be `application/json` if your client were sending  JSON. Spring does a good job at detecting the encoding for you. You can manual control what encoding your handler function consumes with attributes to the `@PostMapping` annotation.

```java
@PostMapping(value = "/contactSubmit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
```

#### Data Binding

More convenient than iterating over the key-value pairs from the form is having Spring bind the request data to an Entity object for you. For that to work you need an Entity with properties corresponding to the keys in your form.

```java
@PostMapping(value = "/contactSubmit")
public String submitContact(@ModelAttribute ContactRequest contact) {
    logger.info(String.valueOf(contact.getName()));
    return "index";
}
```

#### Repositories
With Entities completely populated with might as well store it in our `CRUDRepository`

```java
@Autowired
private ContactRequestRepository contactRepository;

@PostMapping(value = "/contactSubmit")
public String submitContact(@ModelAttribute ContactRequest contact) {
    contactRepository.save(contact);
    return "index";
}
```

#### Redirects
It is good practice to [redirect after a post](https://en.wikipedia.org/wiki/Post/Redirect/Get) submission to make it less likely that users submit a form twice.

We can achieve that with `return "redirect:/contact";` as the return value in our post handler.

#### Success Message
We might also want to display a message when the contact request was received and stored.

We can achieve that by add `RedirectAttributes` to our post handler before we do the redirect

```java
@PostMapping(value = "/contactSubmit")
    public String submitContact(@Valid ContactRequest contact, RedirectAttributes attr) {
        contactRepository.save(contact);
        attr.addFlashAttribute("message", "Thank you for your message. We'll be in touch ASAP");
        return "redirect:/contact";
    }
```

And we also need to add a place in the form to display it. 

```html
<form ...>
	<span th:text="${message}" class="text-success"></span>
	...
</form>
```
This will tell thymeleaf to show any messages. It will not show up if there are no messages.

#### Validation

Finally, you will want to automatically validate your user input. 

You can achieve that with annoations on your Entities. For example `NotEmpty` annotations below do what you would expect.

```java
@Entity
public class ContactRequest {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Please enter your name.")
    String name;
    @NotEmpty(message = "Please let us know your email.")
    @Email
    String email;
    @NotEmpty
    String body;
```

In addition for the validation errors to be displayed client side we need to bind our form to a java object instance (create a "bean backed form"). Two steps are required:

* add a bean to our form get handler
* add thymeleaf expressions to our form html template

The first step requires that we add an explict GetMapping for our `/contact` path (instead of the `ViewControllerRegistry` above).

```java
@GetMapping("/contact")
public String showContactForm(ContactRequest contact) {
    return "contact";
}
```

The modifications to our template include adding a `th:object` reference that refers to a bean **type** (in this case ContactRequest). And binding of individual fields `th:field="*{}"`

```html
<form th:action="@{/contact}" th:object="${contactRequest}" method="post" class="form">

	<div class="form-group">
	    <label for="name">Name</label>
	    <input type="text" id="name" th:field="*{name}"/>	
	</div>
	...
```

Note I also changed the form action path to `contact` because we now have explicit GET and POST handlers that can serve the same path.

We also need to update our POST handler to not save in the presence of errors but redirect to our GET handler (which will include the bound, incomplete ContactRequest bean).

```java
 @PostMapping(value = "/contact")
public String submitContact(@Valid ContactRequest contact, BindingResult binding, RedirectAttributes attr) {
    if (binding.hasErrors()) {
        return "/contact";
    }
    contactRepository.save(contact);
    attr.addFlashAttribute("message", "Thank you for your message. We'll be in touch ASAP");
    return "redirect:/contact";
}
```

#### Style

The final bit of sugar is to highlight errors with a bit of colour. Bootstrap does that for us nicely.

The main elements here are the thymeleaf classappend command (with an inline elvis operator ?:) `th:classappend="${#fields.hasErrors('name')} ? has-error : blu-margin">`
and a bunch of bootstrap classes.

The form html does become a bit hard to read, I admit, but it looks good to the user.


```html
<form th:action="@{/contact}" th:object="${contactRequest}" method="post" class="form" role="form">
    <span th:text="${message}" class="text-success"></span>

    <div class="form-group" th:classappend="${#fields.hasErrors('name')} ? has-error : blu-margin">
        <label class="control-label" for="name">Name</label>
        <input type="text" id="name" class="form-control" th:field="*{name}"/>
        <span th:if="${#fields.hasErrors('name')}" th:errors="*{name}" class="help-block"></span>
    </div>
    <div class="form-group" th:classappend="${#fields.hasErrors('email')} ? has-error : blu-margin">
        <label class="control-label" for="email">Email</label>
        <input type="email" id="email" th:field="*{email}" class="form-control"/>
        <span th:if="${#fields.hasErrors('email')}" th:errors="*{email}" class="help-block"></span>
    </div>
    <div class="form-group" th:classappend="${#fields.hasErrors('body')} ? has-error : blu-margin">
        <label class="control-label" for="body">Content</label>
        <textarea class="form-control" rows="5" id="body" th:field="*{body}"></textarea>
        <span th:if="${#fields.hasErrors('body')}" th:errors="*{body}" class="help-block"></span>
    </div>

    <button type="submit" class="btn btn-primary">Submit</button>

</form>
```

<a href="url"><img src="https://github.com/dschien/santas-grotto/raw/master/doc/validation.png" width="348"></a>

## REST APIs
<a name="restapis"></a>
### Simple Example
Our request mapping to ["/api/geolookup"](https://github.com/dschien/santas-grotto/blob/d6f9dbf1557b810d2ec2c3173db6e379d675a524/src/main/java/ac/uk/bristol/cs/santa/grotto/controllers/WebController.java#L77)


### Try on the command line

`curl --user ZZZ:ABC http://localhost:8080/api/geolookup -X POST -H "Content-Type: text/plain" --data 'MVB, Woodland Rd, Bristol'`

`curl --user XXX:YYY http://localhost:8080/api/account -X POST -H "Content-Type: application/json" --data '{"username":"dan","password":"test","name":"Dan", "role":"USER"}'`
  


