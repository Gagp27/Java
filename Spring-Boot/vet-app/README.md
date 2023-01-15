# Spring Boot project (vet-app)

## This project include
>+ Spring Boot
>+ Spring Mongo
>+ Validations
>+ JSON API
>+ Emails with spring-boot-starter-mail
>+ Json Web Token with JJWT
>+ Password hash with Bcrypt
>+ MongoDB

## Project configurations

>1. Create a mongoDb database with 2 documents
>>+ Vets
>>+ Patients
>2. Connect the database, set up the application.properties for MongoDB
>```
> spring.data.mongodb.database
> spring.data.mongodb.host
> spring.data.mongodb.port
>```
>3. Mailer application.properties
>```
> spring.mail.host
> spring.mail.port
> spring.mail.username
> spring.mail.password
>```
>4. Set up a secret key from the Json Web Token
>```
>jwt.secret
>```
>5. Set up the frontend url for cors
>```
>frontend.url
>```