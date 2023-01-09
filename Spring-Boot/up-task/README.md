# Spring Boot project (Up-Task)

## This project include
>+ Spring Boot
>+ Spring JPA
>+ Validations
>+ JSON API
>+ Emails with spring-boot-starter-mail
>+ Json Web Token with JJWT
>+ Password hash with Bcrypt
>+ MySQL

## Project configurations

>1. create the database
>```
> https://github.com/Gagp27/Databases/tree/master/mysql/up-task-db
>```
>2. Connect the database, set up the application.properties for MySQL
>```
> spring.datasource.url
> spring.datasource.username
> spring.datasource.password
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