# quarkus-temporal.io

### Dependencies

- Java 11
- GraalVM
- Docker, Docker-compose (to run temporal.io Stack)
- Quarkus CLI
- Maven

### How to build

you can run ````mvn clean install```` in root directory.


### How to use

**Current Features**

- Produces bean to ```WorkflowClient```, ```WorkflowServiceStub```, ```WorkerFactory```
- Create temporal workers automatically
- register workflows and activies automatically

**To do**
- native-image executable (Todo)
