# VIP-Membership-System
The aim of the project was to develop a VIP Membership System for an SMB retailer, utilising fully-automated business process models in order to demonstrate the logic of the following business processes within the organisation:

1. How a customer can become a VIP member
2. How will VIP membership be used within orders
3. How customers can cancel their VIP membership

The development team comprised three individuals: Callum Jones, Connor Mackintosh and Jeno Labodi. Throughout the project we completed the project deliverables in an Agile environment, breaking down the project into two-week sprints. Additionally, we utilised GitLab for version control and Jira for tracking the progress of product development.

The VIP Membership System is based on:
* 3 BPMN 2.0 operational models designed with Camunda 7.18
* Camunda 7 Forms
* Java 15
* Spring Boot 2.7.3
* H2 Database

## Project Description

The initial aim of the project was to evaluate the current architecture of the client's information system and propose changes in order to implement the VIP Membership System. The proposal consisted of developing a socio-technical model of the business using i* framework and delivering 3 conceptual-level BPMN 2.0 diagrams that represent the required business processes. Delivering the conceptual models followed by extending the developed business process models to operation-level diagrams that provided the basis of the delivered VIP Membership System. The system allows the user to interact with the automated business processes through Camunda Forms, storing the validated user inputs as process variables followed by completing CRUD operations in the system's H2 database. The developed system demonstrates the logic of setting a customer as a VIP member, utilising VIP membership in the customer order and cancelling a VIP membership by a customer service assistant.  


### i* model of the client's organisation

<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/fea30a22-5bd9-4130-b294-d533aa2bcef0" width="600px">




### Strategic-level Business Process Models 
BPMN Diagram 1: How customer can become a VIP member
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/b2541804-e2b2-4c4e-a1d8-f089c0ca2e18" width="900px">

BPMN Diagram 2: How VIP membership is used within orders
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/9c2379b1-88c8-4782-9b10-eabac5ac1bbc" width="900px">

BPMN Diagram 3: How customers can cancel the VIP membership
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/b0df20b7-6391-4859-af50-00e5506b913f" width="900px">


### Operational-level Business Process Models 




### Screenshots of the deployed system 






### Run the system locally


