# Project Setup

## Prerequisites

Ensure the following tools are installed on your system:

- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)
- **Maven**: [Install Maven](https://maven.apache.org/install.html)
- **Node.js and npm**: [Install Node.js and npm](https://nodejs.org/)
- **Angular CLI** (globally installed): Install using npm:
  
  ```bash
  npm install -g @angular/cli

## Installation and Setup

1. **Clone the Repository**

   Clone the repository and navigate into the project directory:

   ```bash
   git clone <repository-url>
   cd <repository-directory>

2. **Start Docker Services**

   Ensure Docker and Docker Compose are installed and running. Start the services with:

   ```bash
   docker-compose up --build

## Interactive Testing

To perform interactive testing, use the following payment details:

- **Card Number**: 4242 4242 4242 4242
- **Expiration Date**: 12/34 (or any valid future date)
- **CVC**: Any three digits (or four digits for American Express cards)

Input these details into payment forms or the dashboard to test the application’s payment functionality.

## Troubleshooting

If you encounter issues, try the following:

1. Verify that Docker and Docker Compose are properly installed and running

2. Check the logs for any errors:

   ```bash
   docker-compose logs

3. Refer to the project's documentation or reach out to the support team for further assistance.
