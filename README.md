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
The developed i* model represents the organisation's key stakeholders and their goals, dependencies, resources and commitments. 

<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/fea30a22-5bd9-4130-b294-d533aa2bcef0" width="600px">


### Strategic-level Business Process Models 
Strategic-level BPMN 2.0 diagrams representing the 3 different business processes regarding the implementation of the VIP Membership System within the client's organisation.

BPMN Diagram 1: How customer can become a VIP member
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/b2541804-e2b2-4c4e-a1d8-f089c0ca2e18" width="900px">

BPMN Diagram 2: How VIP membership is used within orders
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/6452d62c-acf3-49cc-8a42-6f474159230d" width="900px">

BPMN Diagram 3: How customers can cancel the VIP membership
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/b0df20b7-6391-4859-af50-00e5506b913f" width="900px">


### Operational-level Business Process Models 
Operational-level BPMN 2.0 diagrams representing the 3 different business processes regarding the implementation of the VIP Membership System within the client's organisation.

BPMN Diagram 1: How customer can become a VIP member
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/9b76d988-49b7-4f87-ac22-c2eafa6f380c" width="1000px">

Handel Order Method Subprocess </br>
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/f561a05a-9ba5-4c8a-888c-af3639567d26" width="600px">

BPMN Diagram 2: How VIP membership is used within orders
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/22695093-5531-461e-b03c-7d89bfc2c233" width="1000px">

Confirm Customer Details Subprocess </br>
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/faa47b1c-3800-43cb-881e-97db84f5cea3" width="600px">

BPMN Diagram 3: How customers can cancel the VIP membership
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/0ec2d117-b59a-4517-b95b-e38c36a93db8" width="1000px">



## Screenshots 
The following screenshots demonstrate the running system, completing the business process of using the VIP membership in a customer order by the application that is deployed on the Camunnda Platform.

Executed Process: </br> 
BPMN Diagram 2: How VIP membership is used within orders (Operational-model)
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/22695093-5531-461e-b03c-7d89bfc2c233" width="1000px">
Confirm Customer Details Subprocess </br>
<img src="https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/faa47b1c-3800-43cb-881e-97db84f5cea3" width="600px">

Step 1: Running the Java application and sending the appropriate HTTP request to Camunda's API
![PhoneOrder](https://github.com/J-Labodi/VIP-Membership-System/assets/79979904/e7f26866-da70-4a02-91c2-be1cc0218114)

The above HTTP request starts the deployed application, imitating that the Customer Service Assistant received a phone order as the request contains the relevant, pre-defined process variables.

Step 2: Customer Service Assitant presented with the







### Run the system locally


