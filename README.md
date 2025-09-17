# Smart Healthcare Appointment System

This project is a Spring Boot application designed to manage doctors, patients, appointments, specialties, and prescriptions. 
It provides a secure system where users can authenticate using JWT-based authentication, and role-based access control is enforced for Owners, 
Admins, Doctors, and Patients. All passwords are stored securely using BCrypt hashing.  

The application supports full user management, including registering new doctors, patients, and admins, updating patient details, and deleting accounts.

A key feature of this system is appointment management. Patients can book and cancel appointments, while the system ensures that double-booking is prevented by 
checking availability. Doctors’ schedules are defined by their working hours, and availability is calculated based on existing appointments. 
Patients can also search for doctors by their specialty, and doctors can have multiple specialties assigned to them.  

Prescriptions and medical records are stored in MongoDB instead of a relational database, since these records are usually unstructured and vary. 
Doctors can add prescriptions, which may include notes, medicines, and lab results, while patients can view their full prescription history at any time.  

The project has clear modules. Controllers handle REST API endpoints for doctors, patients, admins, and authentication. DTOs are used for data transfer between layers, 
and JPA entities define the relational model for users, doctors, patients, appointments, and specialties. 
Repositories provide access to relational data, while MongoDB models are used for prescriptions and medical records. 
The security package handles JWT filters and authentication logic, while service classes contain the core business logic of the application.  

Access to the system is controlled by roles. Owners have full control of the system, including managing admins, doctors, 
and patients. Admins can manage doctors and patients. Doctors can manage their appointments and prescriptions. Patients can book appointments and view prescriptions.  

To run the project, you need Java 17+, Maven 3+, and both MySQL and MongoDB installed on your device.

To test the system, you can clone the repository using **git clone https://github.com/qasimbatrawi/Smart-Healthcare-Appointment-System** . 
After that, you need to update the values inside the **application.properties** and **docker-compose.yml** files with real values.
Then, run the system using the Maven command mvn spring-boot:run. Then, you can test the APIs provided in the file APIs.txt using 
Swagger at **http://localhost:8080/swagger-ui/index.html#/** .

Another simple way to test the system without installing the required software is to use Docker. First, install Docker on your system, then pull the container using **docker 
pull qasimbatrawi/healthcare-system:latest** and run it using **docker-compose up -d**. After that, you can test the APIs using Swagger at **http://localhost:8080/swagger-ui/index.html#/** .
