# Project Setup

## Prerequisites

Ensure the following tools are installed on your system:

- Docker
- Docker Compose
- Maven
- Node.js and npm (Node Package Manager)
- Angular CLI (globally installed)

## Installation and Setup

1. **Clone the Repository**

   Clone the repository and navigate into the project directory:

   ```bash
   git clone <repository-url>
   cd <repository-directory>

2. **Start Docker Services**

   Ensure Docker and Docker Compose are installed and running. Start the services with:

   ```bash
   docker-compose up

3. **Build and Run the Spring Boot Application**

   In the project directory where mvnw is located, build and run the Spring Boot application:

   ```bash
   ./mvnw spring-boot:run

4. **Install Angular CLI**

   Ensure Angular CLI is installed globally. If not, install it using npm:

   ```bash
   npm install -g @angular/cli

5. **Install Angular Dependencies**

   Navigate to the Angular project directory (where package.json is located) and install dependencies:

   ```bash
   npm install

6. **Start the Angular Application**

   Start the Angular development server with:

   ```bash
   npm start
