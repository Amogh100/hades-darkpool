[![Build Status](https://travis-ci.org/Amogh100/hades-darkpool.svg?branch=master)](https://travis-ci.org/Amogh100/hades-darkpool)
# hades-darkpool
This is a simple dark pool for cryptocurrencies called hades. This is a project for my CSCI 3010 course. It includes a front end client in react, and a rest api implemented using Spring Boot.

System Requirements:
Is cross-platform (Windows, OS X, Linux), although it has been tested the most on Linux.

Dependencies/Tools needed:
Java JDK/JRE 8+<br />
Maven 3 <br />
Node.js version 8.9.1 <br />
NPM version 5.5.1 <br />

Ensure PostgresSQL is downloaded on your environment, and a database with the name hadesmaster is created.
Create a username/password in Postgres to access this database. Before you run anything, create the environment variables
POSTGRES_USERNAME and POSTGRES_PASSWORD corresponding to the hadesmaster database. As of now this database simply refers to this local postgres instance running on port 5432. <br />

Building/Running the project <br />
Open your terminal.
To run the Spring boot app, build the project with "mvn clean install" in the project's root directory..
Then run "java -jar darkpool-{version_num}-SNAPSHOT.jar" The server should now be running on localhost port 8080.

To run the client "cd" into the hades-client folder. Run "npm install" to donwload the various JS dependencies. Then
run "npm start". The client app should be running on localhost port 3000. Navigate in your web browser to http://localhost:3000 to make sure.



