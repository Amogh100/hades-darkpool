# hades-darkpool
This is a simple dark pool for cryptocurrencies called hades. It includes a front end client in react, and a rest api implemented using Spring Boot.

System Requirements:
Is cross-platform (Windows, OS X, Linux), although it has been tested the most on Linux.

Dependencies/Tools needed:
Java JDK/JRE 8+__
Maven 3 __

Ensure PostgresSQL is downloaded on your environment, and a database with the name hadesmaster is created.
Create a username/password in Postgres to access this database. Before you run anything, create the environment variables
POSTGRES_USERNAME and POSTGRES_PASSWORD corresponding to the hadesmaster database. As of now this database simply refers to this local postgres instance running on port 5432.
