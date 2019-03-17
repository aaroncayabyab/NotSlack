# NotSlack - Chat Application
Simple chat application created with Java object-oriented programming, mysql database and JavaFX
Users are able to chat with one user (direct messaging) or with multiple users (group chat)

## Introduction

## Technologies
- Java
- mysql 
- JavaFX

## Application Features
- easy and user friendly UI
- user @ mentioning
- private messaging
- joining groups (chat rooms) and group chatting
- emoji support

## Setup?
1. You need to have mysql server setup and running on your local machine
2. Git clone this repo for a local copy
3. You need a mysql connector for Java
	- First option is to add jdbc connector as a library
	- Other option is to download mysql-connector-java and add the downloaded jar file to the library
4. Run the provided chat_db.sql through command line or mysql client to setup the database schema and included tables
	- locally the database and the tables, user and userchathistory, will have been created for the chat application to execute queries with

## Code and Query Samples
User information is stored in the user table
Chat histories between users is stored in the userchathistory table

Sample query to obtain the sending and receiving users as well the their exchanged messages and datetime can be obtained through the following query:
```
SELECT s.username as sender,  r.username as receiver, message, datetime 
FROM userchathistory
INNER JOIN user s ON userchathistory.useridsender=s.id
INNER JOIN user r ON userchathistory.useridreceiver=r.id;
```

## Tests and Test Framework