# Job Management System 🕓
* Designing and implementing a Job Management System. The goal of this system is to handle the
  execution of multiple types of Jobs. The actions performed by these Jobs are not relevant.
  Possible examples of such jobs could be loading data into a Data Warehouse, indexing some
  file content, or sending emails.
* High-level system design
<div align='center'>
<img height="350px" src="https://github.com/user-attachments/assets/82f03d63-2de8-4043-8c0b-3de3961219d2">
<hr/>
</div>

   1. Functional Requirements 🔧
      - Create a Job: The system must allow the creation of a single Job, specifying details such as the Job type and schedule.
      - Create a Batch of Jobs: The system must allow creating multiple Jobs in a batch.
      - Delete a Job: A Job can be deleted as long as it is not currently running.
      - Query a Job’s Status: The system should allow clients to query the current status of a Job (QUEUED, RUNNING, SUCCESS, FAILED).
      - Retry a FAILED Job: Failed Jobs can be retried, but they must be re-scheduled without re-sending the details of the original Job.
      - Prioritization of Jobs: Optionally, Jobs can be assigned a priority for execution.
  2. Non-Functional Requirements 📄
      - Scalability: The system must be able to handle a high number of Jobs in parallel, ensuring that the Job queue and execution do not become a bottleneck.
      - Performance: Job execution and scheduling should be fast, with minimal latency in Job state transitions.
      - Reliability and Fault Tolerance: The system must handle failures gracefully.
      - Consistency: The state of each Job should be consistent and correctly tracked in the database. There should be no conflicting states or missing states.
      

## Languages and frameworks 📑
* Java
* Spring Boot
* H2 Database Engine
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
    - Job:

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
            "name": "job 1",
            "description": "This is the first task..",
            "scheduledAt": "2024-11-06T15:59:29Z",
            "executableJarFilePath": "./executableTasks/HiSafeyTask.jar",
            "immediateExecution": false,
            "priority": 2
            },
            {
            "name": "job 2",
            "description": "This is the second task..",
            "scheduledAt": "2024-11-06T15:59:29Z",
            "executableJarFilePath": "./executableTasks/TaskThatTakesFiveSeconds.jar",
            "immediateExecution": true,
            "priority": 2
            }
            ]'
            ```
        - Delete a Job:
            ``` bash
            curl --location --request DELETE 'http://localhost:8080/jobs/{ID}'
            ```
        - Get a Job By ID:
           ``` bash
           curl --location 'http://localhost:8080/jobs/{ID}'
           ```
        - List Jobs by status:
           ``` bash
           curl --location 'http://localhost:8080/jobs/status/SUCCESS'
           ```
        - Execute a Job By ID:
           ``` bash
           curl --location --request POST 'http://localhost:8080/jobs/{ID}/execute'
           ```
           
## Assumptions 📋
- Assume a maximum of 10 seconds delay to run the job after the scheduled date
   - This assumes the scheduling system (such as Spring's @Scheduled annotation) is fast and efficient enough to meet this requirement. 
- Assume the maximum concurrent jobs are 4
   - This assumption implies that the job scheduler or execution system is limited to processing no more than 4 jobs simultaneously. It could be because of resource constraints, such as CPU, memory, or database connections, or a design decision to prevent overloading the system.

## Technical Debt 🚩
- Maximum of 10 seconds delay to run the job after the scheduled date
   - **Technical Debt**: If the system grows and more jobs are scheduled, there's a risk that delays could become more significant than the 10-second window, especially if the resources aren't scaled appropriately. 
   - **Solution**: To mitigate this, we have to implement a distributed job scheduling mechanism, such as using Quartz or a message queue (e.g., RabbitMQ, Kafka) to manage job execution across multiple workers or nodes.
- Maximum concurrent jobs are 4
  - Technical Debt: The assumption of handling 4 concurrent jobs could cause resource contention if the jobs are resource-intensive, potentially slowing down the system.
  - Implement monitoring for CPU, memory, and I/O utilization to detect performance degradation. Consider scaling horizontally by distributing jobs across multiple machines if resource contention becomes a problem.
## Deployment Options ✅
- Docker Containers
- Cloud-Based Deployment (e.g., AWS, GCP, Azure)
- Platform as a Service (Google App Engine, Azure App Service)
- Kubernetes

