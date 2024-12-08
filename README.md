# Job Management System
* Designing and implementing a Job Management System. The goal of this system is to handle the
  execution of multiple types of Jobs. The actions performed by these Jobs are not relevant.
  Possible examples of such jobs could be loading data into a Data Warehouse, indexing some
  file content, or sending emails.
* High-level system design
<div align='center'>
<img height="350px" src="https://github.com/user-attachments/assets/dc5c2685-fb88-4959-ba5d-73f93809323d">
<hr/>
</div>
## Languages and frameworks 📑
* Java
* Spring Boot
* H2
## Features 🥇
* Easy scheduling jobs with a retry mechanism for failure.
## Cloning the repo and starting the app
* clone the repository and open the project in any IDE
``` bash
git clone https://github.com/MohamedRagaab/job_management_system.git
cd job_management_system
```
* You can run the following commands to build the app
``` bash
 ./gradlew build
docker build -t job-management-app .
```
* You can run the following command to test the app
``` bash
./gradlew test
```
* You can run the following command to run the app
``` bash
docker run -d -p 8080:8080 --name job-management-container job-management-app
```
## Usage 🚀
* Here is the list of the RESTful APIs
    - Application:

        - Create a new Job:
            ``` bash
            curl --location 'http://localhost:8080/jobs' \
            --header 'Content-Type: application/json' \
            --data '{
            "name": "job 3",
            "description": "This is the first task..",
            "scheduledAt": "2024-11-06T15:59:29Z",
            "executableJarFilePath": "./executableTasks/HiSafeyTask.jar",
            "immediateExecution": true,
            "priority": 2
            }'
            ```
        - Create a batch of Jobs:
            ``` bash
            curl --location 'http://localhost:8080/jobs/batch' \
            --header 'Content-Type: application/json' \
            --data '[
            {
            "name": "job 3",
            "description": "This is the first task..",
            "scheduledAt": "2024-11-06T15:59:29Z",
            "executableJarFilePath": "./executableTasks/HiSafeyTask.jar",
            "immediateExecution": false,
            "priority": 2
            },
            {
            "name": "job 3",
            "description": "This is the first task..",
            "scheduledAt": "2024-11-06T15:59:29Z",
            "executableJarFilePath": "./executableTasks/HiSafeyTask.jar",
            "immediateExecution": true,
            "priority": 2
            }
            ]'
            ```
        - Delete a Job:
            ``` bash
            curl --location --request DELETE 'http://localhost:8080/jobs/1'
            ```
        - Get a Job By ID:
           ``` bash
           curl --location 'http://localhost:8080/jobs/1'
           ```
        - List Jobs by status:
           ``` bash
           curl --location 'http://localhost:8080/jobs/status/SUCCESS'
           ```
## Assumptions
- Assume a maximum of 10 seconds delay to run the job after the scheduled date
   - This assumes the scheduling system (such as Spring's @Scheduled annotation) is fast and efficient enough to meet this requirement. 
- Assume the maximum concurrent jobs are 4
   - This assumption implies that the job scheduler or execution system is limited to processing no more than 4 jobs simultaneously. It could be because of resource constraints, such as CPU, memory, or database connections, or a design decision to prevent overloading the system.

## Technical Debt
- Maximum of 10 seconds delay to run the job after the scheduled date
   - **Technical Debt**: If the system grows and more jobs are scheduled, there's a risk that delays could become more significant than the 10-second window, especially if the resources aren't scaled appropriately. 
   - **Solution**: To mitigate this, we have to implement a distributed job scheduling mechanism, such as using Quartz or a message queue (e.g., RabbitMQ, Kafka) to manage job execution across multiple workers or nodes.
- Maximum concurrent jobs are 4
  - Technical Debt: The assumption of handling 4 concurrent jobs could cause resource contention if the jobs are resource-intensive, potentially slowing down the system.
  - Implement monitoring for CPU, memory, and I/O utilization to detect performance degradation. Consider scaling horizontally by distributing jobs across multiple machines if resource contention becomes a problem.
## Deployment Options
- Docker Containers
- Cloud-Based Deployment (e.g., AWS, GCP, Azure)
- Platform as a Service (Google App Engine, Azure App Service)
- Kubernetes

