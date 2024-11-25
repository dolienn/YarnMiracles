# YarnMiracles - E-Commerce Platform


![Logo](https://i.ibb.co/DVR4fgg/temporary-logo-02.png)


**YarnMiracles** is a cutting-edge e-commerce platform built to deliver a secure, scalable, and high-performance shopping experience. Using a robust modern tech stack, the platform ensures seamless integration of powerful tools and technologies, making it a reliable foundation for a growing online business.

---

**Tech Stack Highlights**:
- **Backend**: Spring Boot, Spring Security, Hibernate, JWT (JSON Web Tokens)
- **Frontend**:  Angular
- **Database**: PostgreSQL, Liquibase
- **Infrastructure**: Docker, NGINX, Redis, Apache Kafka, Zookeeper
- **Integrations**: Stripe, Cloudinary API, Swagger
- **Testing**: JUnit, Mockito

The platform adheres strictly to **Clean Code** principles and industry **best practices**, ensuring maintainable, robust, and well-structured solutions.

---

## Key Responsibilities and Achievements

- **System Architecture & Security**
  - Designed and implemented a scalable backend using **Spring Boot**.
  - Secured user authentication and authorization with **Spring Security** and **JWT**.
  - Integrated **Stripe** for secure payment processing.
  - Ensured a modular, reusable codebase by following **SOLID** principles and Clean Code practices.
- **API Development & Documentation**
  - Documented comprehensive API endpoints with **Swagger**, enabling easy exploration and testing for developers.
- **Database Management**
  - Employed **Liquibase** for version-controlled database migrations, ensuring seamless schema updates across environments.
  - Designed an efficient, relational database schema in **PostgreSQL** to support complex e-commerce workflows.
- **Advanced Media Management**
  - Integrated **Cloudinary API** for efficient image upload, storage, and optimization, ensuring smooth product image handling.
- **User Management & Authentication**
  - Developed a secure user registration and login system with email verification using **Maildev**.
  - Added advanced user features like session management, password recovery, and profile updates to enhance the overall experience.
- **Product Interaction & Ratings**
  - Implemented sorting, filtering, and pagination features to enable advanced product browsing.
  - Developed product rating (1-5 stars) and review functionality to boost user engagement.
- **Order Tracking & Favorites Management**
  - Built intuitive systems for users to:
    - Track order history.
    - Manage their favorite products for easy future access.
- **Real-Time Data Processing**
  - Leveraged **Apache Kafka** and **Zookeeper** to build a real-time, event-driven architecture for efficient message streaming across microservices.
- **Performance Optimization**
  - Integrated **Redis** for caching, significantly reducing database load and improving response times.
  - Optimized **Hibernate** queries and resolved N+1 query issues using Spring Cache, ensuring top-notch performance.
- **Enhanced User Experience with Pagination & Sorting**
  - Developed robust sorting and pagination functionalities for smoother, more efficient navigation.
- **Load Balancing & High Availability**
  - Configured **NGINX** for load balancing, ensuring high availability and smooth operation in production.
- **Testing & Reliability**
  - Applied **JUnit** and **Mockito** for thorough unit and integration testing, ensuring a defect-free application.
  - Followed **Test-Driven Development** (**TDD**) practices to uphold code quality.
- **Containerization & Deployment**
  - Utilized **Docker** for containerization, streamlining deployment across various environments.
  - Built CI/CD pipelines for efficient development and release management.

---

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

3. **Create a Cloudinary Account**

   You will need to create an account on Cloudinary for image hosting and management. After signing up:

   - Go to the [Cloudinary](https://cloudinary.com).
   - Navigate to `Getting Started` > `View API Keys`.
   - You will find your `Cloud Name`, `API Key`, and `API Secret` here.

5. **Create a `.env` File**

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

   SUPPORT_EMAIL=yoursupportemail@test.com

   # Stripe Configuration
   STRIPE_SECRET_KEY=your_stripe_secret_key

   # JWT Configuration
   JWT_SECRET_KEY=your_jwt_secret_key
   JWT_EXPIRATION=8640000

   # Redis Configuration
   REDIS_HOST=redis
   REDIS_PORT=6380
   REDIS_PASSWORD=password

   # Kafka Configuration
   KAFKA_BOOTSTRAP_SERVERS=kafka:29092

   #Cloudinary Configuration
   CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name
   CLOUDINARY_API_KEY=your_cloudinary_api_key
   CLOUDINARY_API_SECRET=your_cloudinary_api_secret

   # Other configurations
   SERVER_ADDRESS=0.0.0.0
   SERVER_PORT=8088
   ```
   
   **Note**: Replace` your_stripe_secret_key` with the secret key from Stripe (`Developers` > `API keys`).
   
   **Note**: Replace `your_jwt_secret_key` with your chosen JWT secret key.

   **Note**: Replace `your_cloudinary_cloud_name`, `your_cloudinary_api_key`, and `your_cloudinary_api_secret` with the details from your Cloudinary account.

7. **Start Docker Services**

   Ensure Docker and Docker Compose are installed and running. Start the services with:

   ```bash
   docker-compose up --build

8. **Access the Application**

   Once the Docker services are up and running, you can visit the application at:

   http://localhost:80

## Interactive Testing

To perform interactive testing, use the following payment details:

- **Card Number**: 4242 4242 4242 4242
- **Expiration Date**: 12/34 (or any valid future date)
- **CVC**: Any three digits (or four digits for American Express cards)

Input these details into payment forms or the dashboard to test the application’s payment functionality.

## Functionality Testing

1. **User Registration and Activation Flow**
   To create a user and access the platform, follow these steps:

   - **Register a new user**: Navigate to the registration page and fill out the form with the required information such as name, email, and password.
   - **Email verification**: After registration, an activation email with a verification code will be sent to the provided email address.
     You can check the email using Maildev, which is configured for testing purposes.
     - **Maildev** can be accessed at: http://localhost:1080
   - **Activate the account**: Enter the verification code from the email to activate your account.
      Once the code is confirmed, your account will be successfully activated, and you can log in using your credentials.

2. **Admin Account and Admin Panel Access**
   For testing purposes, a pre-configured **test admin** account is available. This account allows you to access the **Admin Panel** where you can view user data, manage orders, and perform other administrative actions.

   - **Admin Login Details**:
     - **Username**: `admin@test.com`
     - **Password**: `testadmin123`
   - **Accessing the Admin Panel**:
     - **User Management**: View all registered users and their details.
     - **Order Statistics**: : View the number of orders, sell products and feedbacks.
     - **User and Product Management**: Edit user information, block users, and create or modify products.

## Troubleshooting

If you encounter issues, try the following:

1. Verify that Docker and Docker Compose are properly installed and running

2. Check the logs for any errors:

   ```bash
   docker-compose logs

3. Refer to the project's documentation or reach out to the support team for further assistance.
