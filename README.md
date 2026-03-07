# Teams Notification Service

## Overview

The Teams Notification Service allows applications to send notifications to **Microsoft Teams channels via 
Incoming Webhooks** programmatically.
<br/>
It uses Spring Boot for REST endpoint, Jakarta Validation for request validation, and Springdoc OpenAPI to 
automatically generate Swagger documentation.
<br />
<br />


## The service supports:

- Structured and validated payloads

- Clear success/failure response models

- Interactive API documentation with Swagger
<br />
<br />


## Getting Started

⚠️ Note: You need to create an incoming webhook in Microsoft Teams to get the URL above.
Follow the instructions here: [Creating an Incoming Webhook in Teams](docs/CREATE_TEAMS_WEBHOOK.md)

### 1. Running Locally

#### Prerequisites
- Java 25
- Maven 3.9+
- Microsoft Teams account with permission to create incoming webhooks

#### Configuration

Before running the application, set the TEAMS_WEBHOOK_URL environment variable:

```bash
export TEAMS_WEBHOOK_URL="${YOUR_TEAMS_WEBHOOK_URL}"
```

#### Steps
```bash
# Clone the repo
git clone -b main https://github.com/anish-anantharaman/teams-notification-service.git

# Build the project
mvn clean install

# Run the unit and integration tests (Optional)
> Currently, the application does not include any automated tests.  
> You can add your own unit or integration tests as needed, and then run:
mvn test

# Run the application
mvn spring-boot:run
```

### 2. Running with Docker (Optional)

For a quick start without building locally, you can use Docker. No tests are included in this Docker build.

```bash
# Build Docker image
docker build -t teams-notification-service .

# Run the container with the webhook URL
docker run -e TEAMS_WEBHOOK_URL="${YOUR_TEAMS_WEBHOOK_URL}" -p 8080:8080 teams-notification-service

```

This is useful if you want to start the service quickly without installing Java or Maven locally.

### 3. Sending a Test Message

Once the service is running, you can send a test notification using the curl provide below:

```bash
curl --location 'http://localhost:8085/api/v1/notifications' \
--header 'Content-Type: application/json' \
--data '{
    "title": "Title",
    "subtitle": "Subtitle",
    "content": "Content",
    "buttonTitle": "Button Title",
    "url": "https://www.google.com"
}'
```

### 4. Swagger API Documentation

Once the application is running, open your browser to:

[Swagger UI - Send Channel Notification](http://localhost:8085/swagger-ui/index.html#/Notifications/sendChannelNotification)
<br />
<br />

## Contributing

This project is open for suggestions, improvements, and pull requests.
Feel free to fork the repo, make changes, and submit a PR. Your contributions are welcome!
