To run application:
go to "micro" directory and run following commands:


1. java -jar discovery.jar         

Runs the discovery service to recognize all the other services. Based on Netflix Eureka. Runs on port 1111
------------------------------------------------------------------------------------------------------------------


2. java -jar jms.jar

Runs jms queue (HornetQ). JMS server runs on port 5455
------------------------------------------------------------------------------------------------------------------


3. java -jar note-archive-microservice.jar

Runs service that stores all the notes as archive. Notes are passed into that service via JMS from timeline service.
------------------------------------------------------------------------------------------------------------------


4. java -jar timeline-microservice.jar

Runs timeline service that controlls all the operations with notes. Also sends notes to archive service via JMS. Runs on port 1000
------------------------------------------------------------------------------------------------------------------


5. java -jar users-microservice.jar

Runs users service that controlls all the operations. When needs to perform operation with notes, calls the timeline servise via http rest call
------------------------------------------------------------------------------------------------------------------


6. java -jar gateway.jar

Runs the gateway service. Based on Netflix Zuul. It was partially implemented (security does no work) because of the lack of time. Runs on port 5000