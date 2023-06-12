Environment: \
Java version: Java 8\
SpringBoot Version: 2.7.12\
Development Machine: Macbook


InMemory Database: h2\
H2-Console-URL : http://localhost:8080/h2-console\
Driver Class: org.h2.Driver\
JDBC URL: jdbc:h2:mem:drone\
User Name: sa\
Password: sa


Inside a project directory:

BUILD JAR AND RUN TESTS:\
mvn clean install

RUN APPLICATION:\
java -jar dronedispatcher-0.0.1-SNAPSHOT.jar\

==============================================\
Audit logs will be found in logs/audit-battery.log for current audit logs and historical logs will be stored in /archived/ folder. The scheduled time is 5 Minutes thus it will Audit battery logs every 5 minutes\
=================================================\
Assumption 1:\
All the medication items are listed in src/main/resources/data.sql path and will get loaded into the database, No api created for medication apart from listAllMedication.You can list the medication using below request\
   curl --location --request GET 'localhost:8080/medication/list'

Assumption 2:\
There is no mention of how the state should change. Thus, created an api to change the status of a drone.\
The request will accept the drone serial number in path params as suggested AA0 in example and takes batteryPercentage and droneState as query parameters\
curl --location --request POST 'localhost:8080/dispatch/drone/update/state/AA0?batteryPercantage=65&droneState=IDLE'

Rules for state change:
IDLE: A drone must be either IDLE, LOADING, LOADED, RETURNING state to be changed to IDLE. If the drone have medications and are either in LOADING or LOADED state the medications entry will be automatically removed from the drone.

LOADED: If current state of a drone is LOADING then only the drone status can be updated to LOADED state.

DELIVERING: If current state of a drone is LOADED then only the drone status can be updated to DELIVERING state.

DELIVERED: If current state of a drone is DELIVERING then only the drone status can be updated to DELIVERED state. And all the medication items will be removed from drone

RETURNING: If current state of a drone is DELIVERED then only the drone status can be updated to RETURNING state.

=======================================================\
REQUEST URL FOR APIS:\
LIST MEDICATIONS: \
curl --location --request GET 'localhost:8080/medication/list'

REGISTER DRONE: \
curl --location --request POST 'localhost:8080/drone/register' \
--header 'Content-Type: application/json' \
--data-raw '{
"serialNumber":"AA0",
"model":"Heavyweight",
"weightLimit": 200,
"batteryCapacity": 100
}'

GET AVAILABLE DRONE FOR LOADING:\
curl --location --request GET 'localhost:8080/dispatch/drones/available'

CHECK DRONE BATTERY:\
curl --location --request GET 'localhost:8080/dispatch/drone/battery/{serialNumber}'

LIST MEDICATION BY DRONE:\
curl --location --request GET 'localhost:8080/dispatch/medications/{serialNumber}'

LOAD MEDICATION IN A DRONE:\
The request body of this request contains the list of medicine codes.\
curl --location --request POST 'localhost:8080/dispatch/load/{serialNumber}' \
--header 'Content-Type: application/json' \
--data-raw '["med_1"]'

DRONE CHANGE STATE:\
curl --location --request POST 'localhost:8080/dispatch/drone/update/state/{serialNumber}?batteryPercantage=65&droneState=IDLE'











