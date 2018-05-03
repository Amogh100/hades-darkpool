[![Build Status](https://travis-ci.org/Amogh100/hades-darkpool.svg?branch=master)](https://travis-ci.org/Amogh100/hades-darkpool)
# hades-darkpool
This is a simple dark pool for cryptocurrencies called hades. This is a project for my CSCI 3010 course. It includes a front end client in react, and a rest api implemented using Spring Boot. For the final part of the project, I deployed this on AWS
EC2. The setup is the same, you may need to install maven, node, npm, and docker via the package manager
of your choice of Server. I used an ubuntu server, so these were all just installed via apt-get.

System Requirements:
Is cross-platform (Windows, OS X, Linux), although it has been tested the most on Linux.

Dependencies/Tools needed:
Java JDK/JRE 8+<br />
Maven 3 <br />
Node.js version 8.9.1 <br />
NPM version 5.5.1 <br />
Docker https://docs.docker.com/ for docs <br />
rabbitmq Docker image https://hub.docker.com/_/rabbitmq/      

Ensure PostgresSQL is downloaded on your environment, and a database with the name hadesmaster is created.
Create a username/password in Postgres to access this database. Before you run anything, create the environment variables
POSTGRES_USERNAME and POSTGRES_PASSWORD corresponding to the hadesmaster database. As of now this database simply refers to this local postgres instance running on port 5432. Run the rabbitmq docker container, along with management UI which is running on port 15672. The docker container should have an IP address of 172.17.0.2. See here https://stackoverflow.com/questions/27937185/assign-static-ip-to-docker-container <br />

Building/Running the project <br />

In the root directory of the project run mvn clean install, this will build the core library(which has various datastructures for both the engine and rest api), and then build the engine and rest api.


Running Hades Engine <br />
The hades engine is the main project that listens for orders via RPC and RabbitMq. Run with java -jar target/hadesengine-{vesion number with dependencies}.jar


Running Hades Rest API <br />
The hades rest api is a Spring Boot project that exposes HTTP endpoints for a client application.To run it, do java -jar target/hadesrest-{version num}.jar



To run the client "cd" into the hades-client folder. Run "npm install" to donwload the various JS dependencies. Then
run "npm start". The client app should be running on localhost port 3000. Navigate in your web browser to http://localhost:3000 to make sure.



