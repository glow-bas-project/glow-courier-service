# GLOW Courier Service

The GLOW Courier Service is a backend microservice for courier-related functionality within the GLOW platform. It is independently deployable and forms part of the platform's distributed microservice architecture.

Each courier has a profile with their vehicle type (car, bike, motorcycle or scooter) and an availability status: available, busy or offline. The service exposes REST endpoints to create, update and delete couriers and to change their availability.

The service is still in progress. It reuses the courier domain model from `glow-user-service` as a library and currently keeps couriers in memory, so data is not persisted between restarts yet. Assigning orders to couriers for delivery is planned but not built.

Technologies:
* Java 25 & Quarkus
* REST API
* OpenID Connect (OIDC)
* Kubernetes
* Gradle
