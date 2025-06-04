
---

# 💰 Crypto Portfolio Tracker

A Spring Boot-based backend system that lets users manage their crypto holdings, fetch real-time prices from an external API, set custom price alerts, and track portfolio performance securely.

## 🌐 Domain

Finance / Cryptocurrency / Investment

---

## 🎯 Objectives

* Let users manage their crypto portfolios with custom inputs.
* Fetch real-time coin prices from a public API.
* Set alerts based on price thresholds (above or below).
* Track asset performance over time.
* Secure endpoints with user role-based access (USER / ADMIN).

---

## 🧱 Tech Stack

| Layer         | Technology                       |
| ------------- | -------------------------------- |
| Framework     | Spring Boot                      |
| Security      | Spring Security + JWT            |
| Persistence   | Spring Data JPA                  |
| Database      | MySQL                            |
| Build Tool    | Maven                            |
| Utilities     | Lombok, ModelMapper              |
| Testing       | JUnit                            |
| Documentation | Swagger (springdoc-openapi)      |
| External API  | CoinGecko API (real-time prices) |
| Scheduler     | `@Scheduled` (Spring)            |

---

## 🧩 Key Modules

1. *User & Role Management*
2. *Crypto Portfolio Management*
3. *Real-Time Price Fetching*
4. *Custom Price Alerts*
5. *Scheduled Price Checks*
6. *Performance Monitoring*

---

## 🔐 Roles & Access

| Role  | Access Description                                    |
| ----- | ----------------------------------------------------- |
| ADMIN | Full access: manage users and view all portfolios     |
| USER  | Manage own portfolio, set alerts, view personal stats |

---

## 🗃 Entity Overview

* *User*: id, username, password, role (USER / ADMIN)
* *PortfolioEntry*: coinId, userId, coinName, symbol, quantityHeld, buyPrice, buyDate
* *CryptoPrice*: coinId, name, symbol, priceUsd, marketCapUsd, volume24hUsd
* *Alert*: id, userId, coinId, alertType (ABOVE/BELOW), thresholdPrice, triggered

---

## 🔁 REST API Endpoints

### 🔐 AuthController

* `POST /api/auth/register`
* `POST /api/auth/login`

### 👤 UserController (Admin)

* `GET /api/users`
* `PUT /api/users/{id}/role`

### 📊 PortfolioController (User)

* `POST /api/portfolio`
* `GET /api/portfolio`
* `PUT /api/portfolio/{coinId}`
* `DELETE /api/portfolio/{coinId}`

### 💸 CryptoPriceController

* `GET /api/prices`
* `GET /api/prices/{symbol}`

### 🚨 AlertController

* `POST /api/alerts`
* `GET /api/alerts`
* `PUT /api/alerts/{id}/toggle`
* `DELETE /api/alerts/{id}`

---

## 🧪 Workflow

1. User registers and logs in.
2. Adds coins manually to their portfolio (quantity, buy price, buy date).
3. The system fetches real-time coin prices from the API.
4. User sets alerts for specific coins and threshold prices.
5. A scheduled job checks prices every minute and triggers alerts if conditions match.
6. Users can monitor portfolio value and receive price notifications.

---

## 🖼 ER Diagram

![WhatsApp Image 2025-05-27 at 11 29 22_9bae6aaf](https://github.com/user-attachments/assets/d738112b-2369-4fb6-8630-c263feca858b)

## 🖼 Class Diagram
![WhatsApp Image 2025-05-30 at 08 47 45_5fff2c25](https://github.com/user-attachments/assets/294d7f5e-04dc-482d-809b-36ca5d9a74d7)



## 📁 Project Structure

```
com.bridgelabz.cryptotracker
├── controller
├── dto
├── entity
├── repository
├── service
├── config
├── security
├── scheduler
├── exception
└── CryptoPortfolioApplication.java
```

---

## ⚙ Configuration (`application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crypto_tracker
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true



## ▶ How to Use the Project

### 🛠 Prerequisites

* Java 17+
* Maven
* MySQL
* Postman / Swagger UI

### 🚀 Steps to Run

1. **Clone the Repository**

   ```bash
   git clone https://github.com/vv6962712/crypto_Portfolio_Tracker
   cd crypto-portfolio-tracker
   ```

2. **Set up the Database**

   * Create a MySQL DB named `crypto_tracker`
   * Update `application.properties` with your credentials

3. **Build and Run the App**

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

4. **Access Swagger UI**
   Visit: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

5. **Test the APIs**

   * Register and login via `/api/auth`
   * Fetch real time price using real time API `/api/prices`
   * Manage portfolio with `/api/portfolio`
   * Set and manage alerts via `/api/alerts`

---

## 👥 Authors
-  ⁠[Chintapalli Dinesh](https://github.com/Dinesh2244)
-  ⁠[S Vishnu Vardhan](https://github.com/vv6962712/)
-  ⁠[Vijaya Lakshmi](https://github.com/Vijaya-Lakshmi-0917)
-  ⁠[Sarthak Mudaliar](https://github.com/Sarthak691)
- ⁠ [Abdul Hadi Mohammed Ashraf](https://github.com/abdulhadi012)



## 🤝 Contributors

Special thanks to the team for collaborative development, testing, and documentation across all modules.
