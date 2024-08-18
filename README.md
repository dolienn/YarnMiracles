# YarnMiracles


![Logo](https://i.ibb.co/DVR4fgg/temporary-logo-02.png)


YarnMiracles is a cutting-edge e-commerce platform designed to offer a seamless shopping experience. Built with a robust tech stack including **Spring Boot**, **Spring Security**, **Hibernate**, **PostgreSQL**, **Angular**, **TypeScript**, **HTML**, **SCSS**, **Docker**, and **Stripe**, YarnMiracles combines security, efficiency, and user satisfaction.

## Key Features

- **User Management**: Secure registration and login with email verification. Users can manage their sessions effectively.
- **Product Interaction**: Browse and interact with products, including rating (1-5 stars) and leaving comments.
- **Payment Integration**: Secure transactions using Stripe.
- **Order and Favorites**: Access your order history and manage a list of favorite products.
- **Navigation**: Efficient product browsing with pagination and sorting options.
- **Contact Form**: Directly communicate with the site owner via the contact form.

The platform emphasizes security and user experience, leveraging **JUnit** and **Mockito** for testing, **Docker** for containerization, and **JWT** with **Spring Security** for authentication. **Maildev** handles email verification processes.

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
   git clone https://github.com/dolienn/YarnMiracles.git
   cd yarn-miracles

2. **Configure Stripe Publishable Key in Angular**

   To configure Stripe in the Angular project, you need to update the `stripePublishableKey` in the Angular environment file.

   **Steps:**

   - Log in to the [Stripe Dashboard](https://dashboard.stripe.com).
   - Navigate to `Developers` > `API keys`.
   - Copy the publishable key (`Publishable Key`), which is labeled as `pk_test_...` or `pk_live_...` depending on the environment.

   Open the file `src/environments/environment.development.ts` in your Angular project and update the Stripe publishable key in this file.

   **Example `src/environments/environment.development.ts` file:**

   ```typescript
   export const environment = {
     production: false,
     url: 'http://localhost:8088/api/v1',
     stripePublishableKey: 'YOUR_PUBLISHABLE_KEY',
   };

2. **Create a `.env` File**

   In the root directory of the Spring Boot project, create a file named `.env` and add the following configuration:

   ```dotenv
   # Database Configuration
   DATABASE_URL=jdbc:postgresql://localhost:5432/shop
   DATABASE_USERNAME=username
   DATABASE_PASSWORD=password

   # Email Configuration
   MAIL_HOST=localhost
   MAIL_PORT=1025
   MAIL_USERNAME=username
   MAIL_PASSWORD=password

   # Stripe Configuration
   STRIPE_SECRET_KEY=your_stripe_secret_key

   # JWT Configuration
   JWT_SECRET_KEY=your_jwt_secret_key
   JWT_EXPIRATION=8640000

   # Other configurations
   SERVER_ADDRESS=0.0.0.0
   SERVER_PORT=8088
   ```
   **Note**: Replace` your_stripe_secret_key` with the secret key from Stripe (`Developers` > `API keys`).
   **Note**: Replace `your_jwt_secret_key` with your chosen JWT secret key. 

5. **Start Docker Services**

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
