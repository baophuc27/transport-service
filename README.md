# Marvin
Marvin is a ProductionReady project used for speed up building data management platform based application

# Start dmp service
- Point to dmp service work dir: cd services/core-service
- Build and run container: docker-compose down && ./gradlew clean build && docker-compose build && docker-compose up -d
- Sanity check: ./scripts/test-em-all.bash
- Then: http://localhost:7001/swagger-ui/


## Main Component
- General purpose services (IAM for example)
- Core services (DMP and related services)
- Support services (Crawler services, Ingestion services, IoT)
- Scripts describe the construction of the projects
- Scripts for sanity checking

## Criteria
- Provide general purpose microservices with basic use cases, and the facilities to extend or customize for a special domain 
- Provide scalable Infrastructure
- Expose some PoC of concrete special domain applications

## Application Implementation
- Use Spring Boot framework to construct application code base on the framework's AOP and IoC supports
- Use async manner for communication between core services


## Infrastructure
- Use K8s as the container-orchestration system for automating application deployment, scaling, and management

## Guideline
- [Reactive approach](https://www.reactivemanifesto.org/)
- [Microservice Architecture Pattern](http://martinfowler.com/microservices/)








 


