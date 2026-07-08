# Online Product Order Management System (OMS)

This is a full-stack Online Product Order Management System (OMS) built using Spring Boot, Angular, and MySQL.

---

## Technical Stack
- **Backend:** Java 17, Spring Boot 3.2.5, Spring Security, Spring Data JPA, JWT, Apache POI (Excel), OpenPDF
- **Frontend:** Angular 18 (Standalone Components, Reactive Forms, Router, HTTP Client), Bootstrap 5, Bootstrap Icons
- **Database:** MySQL 8.x

---

## Directory Structure
- `database/`: Contains `schema.sql` database file.
- `backend/`: Spring Boot maven codebase.
- `frontend/`: Angular SPA codebase.
- `postman/`: Postman collection file `oms_collection.json`.

---

## Getting Started

### 1. Database Setup
1. Open your MySQL client.
2. Run the SQL script found in `database/schema.sql`. This script initializes the database `oms_db`, sets up all 15 required tables, and seeds the default roles and an administrator account.

### 2. Backend Setup
1. Navigate to the `backend` folder.
2. Open `src/main/resources/application.properties` and verify your MySQL `username` and `password`.
3. Build the application using maven wrapper:
   ```bash
   mvn clean install
   ```
4. Start the application:
   ```bash
   mvn spring-boot:run
   ```
5. The backend API server will start on `http://localhost:8080`.

### 3. Frontend Setup
1. Navigate to the `frontend` folder.
2. Install npm dependencies:
   ```bash
   npm install
   ```
3. Start the Angular dev server:
   ```bash
   npm run start
   ```
4. The client application will be accessible at `http://localhost:4200`.

---

## User Credentials

- **Admin User:**
  - **Username:** `admin`
  - **Password:** `admin123`
- **Customer User:**
  - Perform customer registration on the signup screen to generate a new account.

---

## Screenshot Capture Guide

For submission, capture screenshots of the following interfaces:

1. **Database Schema:**
   - Capture a screenshot of your MySQL database showing the list of created tables (e.g. using `SHOW TABLES;` command).

2. **Login & Registration Screens:**
   - Sign Up Screen: Fill out registration details and capture the form.
   - Sign In Screen: Capture the login interface before logging in.

3. **Customer Catalog:**
   - Product Catalog: Capture the browse screen showing categories, products, prices, and GST info.
   - Cart / Booking Tray: Capture the sidebar cart with added products, quantities, and calculated prices.

4. **Customer Order History:**
   - My Orders Page: Capture the list of placed bookings showing status indicators (e.g. PENDING, CONFIRMED).
   - Invoice PDF Download: Click download PDF and capture the generated PDF bill page.

5. **Admin Dashboard:**
   - Main Dashboard: Capture the admin welcome page showing counters (Total Products, Revenue) and monthly trends.

6. **Inventory Adjustments:**
   - Inventory Page: Capture the product list with stock balances, low-stock warning labels, and the adjustment history logs.

7. **Reports & Logs:**
   - Business Reports: Capture the custom query search reports page showing the export buttons.
   - WhatsApp Logs: Capture the WhatsApp logs table detailing dispatch booking notification entries.
