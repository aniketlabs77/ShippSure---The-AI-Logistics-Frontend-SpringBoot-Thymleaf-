<div align="center">

# 🚚 SHIPPSURE — AI-POWERED SMART LOGISTICS & TELEMETRY PLATFORM

### ⚡ Move Smart. Ship Sure.
### 💻 Spring Boot 3 + Spring Security 6 + Groq AI + Razorpay + Leaflet.js + Java 17

<br/>

<a href="#"><img src="https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white" /></a>
<a href="#"><img src="https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" /></a>
<a href="#"><img src="https://img.shields.io/badge/Spring_Security-6.2.0-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" /></a>
<a href="#"><img src="https://img.shields.io/badge/Thymeleaf-3.1-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" /></a>
<a href="#"><img src="https://img.shields.io/badge/Leaflet.js-1.9.4-199900?style=for-the-badge&logo=leaflet&logoColor=white" /></a>
<a href="#"><img src="https://img.shields.io/badge/Razorpay-Payment_Gateway-02042B?style=for-the-badge&logo=razorpay&logoColor=white" /></a>
<a href="#"><img src="https://img.shields.io/badge/Groq_AI-LLM_Inference-F05A28?style=for-the-badge&logo=openai&logoColor=white" /></a>
<a href="#"><img src="https://img.shields.io/badge/Bootstrap-5.3.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white" /></a>

<br/><br/>

> 🔗 **Companion Repository:** This frontend web application communicates with the [**ShippSure Backend REST Service**](https://github.com/aniketlabs77/ShippSure-The-AI-Logistics-SpringBoot-Backend-).

</div>

---

## 📖 **Overview**

> **ShippSure** is a production-style, full-stack smart logistics and real-time freight dispatch platform built with **Java 17, Spring Boot 3, Spring Security 6, Thymeleaf, Groq AI, and Leaflet.js**.

Designed to simulate enterprise-grade logistics services (such as Porter, Uber Freight, and Delhivery), ShippSure provides intelligent cargo classification, real-world road network dispatching, digital payment processing, live driver GPS tracking, and distinct role-based management portals.

### 🎯 **Key Features**

| Feature | Logo | Description |
|---------|:---:|-------------|
| 🧠 **AI Freight Advisor** | <img src="https://img.icons8.com/color/32/000000/artificial-intelligence.png"/> | Uses Groq LLM inference to recommend the optimal vehicle from parcel weight and volume dimensions |
| 📍 **2-Stage Smart Dispatch** | <img src="https://img.icons8.com/color/32/000000/route.png"/> | Combines Haversine geometric radius filtering with OSRM real-world road travel time (ETA) |
| 💳 **Razorpay Checkout** | <img src="https://img.icons8.com/color/32/000000/bank-cards.png"/> | Full payment gateway integration with dynamic per-km vehicle fare calculations |
| 🗺️ **Live Leaflet GPS Telemetry** | <img src="https://img.icons8.com/color/32/000000/map-marker.png"/> | Interactive map with reverse geocoding, route polylines, and 10s driver position polling |
| 🔐 **Enterprise Auth & Security** | <img src="https://img.icons8.com/color/32/000000/shield.png"/> | BCrypt password hashing + Google OAuth2 Single Sign-On + defensive null-session protection |
| 👥 **Three Multi-Tenant Portals** | <img src="https://img.icons8.com/color/32/000000/conference-call.png"/> | Dedicated responsive server-rendered dashboards for Customers, Drivers, and Administrators |

---

## 🛠️ **Technology Stack**

| Technology | Version | Logo | Description |
|------------|---------|:---:|-------------|
| ☕ **Java** | 17 LTS | <img src="https://img.icons8.com/color/32/000000/java-coffee-cup-logo.png"/> | Core enterprise programming language |
| 🍃 **Spring Boot** | 3.x | <img src="https://img.icons8.com/color/32/000000/spring-logo.png"/> | Web MVC framework and server runtime |
| 🛡️ **Spring Security** | 6.x | <img src="https://img.icons8.com/color/32/000000/security-checked.png"/> | Authentication, role authorization, and OAuth2 client |
| 🌿 **Thymeleaf** | 3.x | <img src="https://img.icons8.com/color/32/000000/thymeleaf.png"/> | Modern server-side Java template engine |
| 🗺️ **Leaflet.js** | 1.9.4 | <img src="https://img.icons8.com/color/32/000000/map-marker.png"/> | Mobile-friendly interactive JavaScript mapping |
| 💳 **Razorpay SDK** | Test/Live | <img src="https://img.icons8.com/color/32/000000/bank-cards.png"/> | Digital payments and order verification gateway |
| 🤖 **Groq AI (OpenAI API)** | gpt-oss-120b | <img src="https://img.icons8.com/color/32/000000/artificial-intelligence.png"/> | Ultra-low latency LLM inference for cargo recommendations |
| 🌐 **OSRM Routing Engine** | API v5 | <img src="https://img.icons8.com/color/32/000000/compass.png"/> | Open Source Routing Machine road driving network API |
| 🎨 **Bootstrap** | 5.3.3 | <img src="https://img.icons8.com/color/32/000000/bootstrap.png"/> | Responsive UI layout, tab panes, and components |
| 📦 **Apache Maven** | 3.9+ | <img src="https://img.icons8.com/color/32/000000/maven.png"/> | Build lifecycle automation and dependency management |

---

## 📋 **What I Worked On**

### ✅ **Task 1: AI Vehicle Recommendation Engine (Groq LLM)**
<img align="right" width="44" src="https://img.icons8.com/color/48/000000/artificial-intelligence.png"/>

- Integrated OpenAI-compatible Groq API (`openai/gpt-oss-120b`) via Spring Web Client.
- Customers input cargo weight ($kg$) and dimensions ($L \times W \times H$ in $cm$).
- The LLM processes cargo physical constraints against vehicle payloads to recommend the exact freight tier: **Bike**, **Cargo Auto**, **Mini Truck**, or **Truck**.
- Eliminates oversized transport waste and prevents under-capacity booking failures.

**Technologies Used:** 🤖 Groq LLM API, 🍃 Spring Web Client, 📝 Prompt Engineering

---

### ✅ **Task 2: 2-Stage Smart Proximity Dispatch Algorithm**
<img align="right" width="44" src="https://img.icons8.com/color/48/000000/route.png"/>

- Developed an intelligent two-tier driver matching engine in `DriverService.java`:
  - **Stage 1 (Geometric Haversine Pre-filtering):** Rapidly scans active and unassigned (`Free` & `Activated`) fleet drivers matching the requested vehicle tier within a bounding radius.
  - **Stage 2 (OSRM Road Travel Matrix):** Queries the Open Source Routing Machine (OSRM) driving API to calculate **actual real-world road travel time (ETA in seconds)** and road network distance.
- Automatically pairs the customer booking with the driver who has the lowest road transit time.

**Technologies Used:** 🌐 OSRM Driving API, 📐 Haversine Formula, 🔄 Dispatch Engine

---

### ✅ **Task 3: Razorpay Payment Gateway & State Synchronization**
<img align="right" width="44" src="https://img.icons8.com/color/48/000000/bank-cards.png"/>

- Implemented dynamic vehicle-rate calculation based on vehicle class and driving mileage.
- Embedded Razorpay JavaScript Checkout modal supporting test card and UPI transactions.
- Implemented atomic post-payment state transitions:
  - Shipment status is persisted as `"Pending"`.
  - Assigned driver's duty state is automatically updated to `"Busy"`.
- Added defensive session checks to prevent unauthenticated booking submissions.

**Technologies Used:** 💳 Razorpay SDK, 🔄 State Machine, 🔐 Session Management

---

### ✅ **Task 4: Live GPS Telemetry Simulation & Leaflet Mapping**
<img align="right" width="44" src="https://img.icons8.com/color/48/000000/map-marker.png"/>

- Integrated OpenStreetMap Nominatim reverse geocoding to translate user-clicked map coordinates into readable addresses.
- Rendered dynamic Leaflet.js maps featuring custom pickup, dropoff, and live vehicle markers connected by route polylines.
- Implemented a 10-second client-side polling endpoint (`/customer/shipment/{id}/tracking`) returning JSON coordinates to animate the vehicle marker smoothly in real time.
- Built a driver GPS update console in `driver/shipment_details.html` allowing drivers to publish new coordinates to `/shipment/updateLocation`.

**Technologies Used:** 🗺️ Leaflet.js, 📍 OpenStreetMap, ⏱️ Telemetry Polling

---

### ✅ **Task 5: Enterprise Authentication & Defensive Security**
<img align="right" width="44" src="https://img.icons8.com/color/48/000000/shield.png"/>

- **Form Authentication:** BCrypt password hashing ($10$ rounds) for Customers and Drivers.
- **Google OAuth2 Single Sign-On:** Integrated Spring Security 6 OAuth2 client for seamless one-click Google Sign-In.
- **Defensive Error Handling:** Implemented production guards that detect Google-registered accounts attempting password login, guiding them to use Google Sign-In instead of throwing 500 exceptions.
- **Password Recovery:** Integrated Gmail SMTP service to send secure HTML password-reset emails.
- **Clean Logout:** Fixed client-side script interception to ensure native Spring Boot session invalidation (`session.invalidate()`).

**Technologies Used:** 🛡️ Spring Security 6, 🔑 Google OAuth2, 📧 JavaMailSender, 🔒 BCrypt

---

### ✅ **Task 6: Multi-Tenant Role Dashboards & Thymeleaf SSR**
<img align="right" width="44" src="https://img.icons8.com/color/48/000000/conference-call.png"/>

- **Customer Dashboard:** Real-time KPI metric cards (Total, Active, Delivered), dynamic AI dispatch brief, recent orders table with live tracking buttons, and empty-state illustrations.
- **Driver Dashboard:** Assigned delivery queue with status filter pills (`All`, `Pending`, `InTransit`, `Delivered`), duty toggle (`Free` $\leftrightarrow$ `Busy`), and lifetime earnings manifest (`₹`).
- **Admin Control Center:** Global network analytics, live shipments audit table, and an interactive document review suite to inspect uploaded RC and License binary images with one-click **Approve** and **Reject** controls.

**Technologies Used:** 🌿 Thymeleaf 3, 🎨 Bootstrap 5, 📱 Responsive Web Design

---

## 🏗️ **System Architecture**

### 📊 **Component Breakdown:**

| Component | Logo | Responsibility |
|-----------|:---:|----------------|
| **Customer Controller** | <img src="https://img.icons8.com/color/32/000000/guest-male.png"/> | Handles AI vehicle recommendation, booking forms, Razorpay checkout, and live tracking |
| **Driver Controller** | <img src="https://img.icons8.com/color/32/000000/shipped.png"/> | Handles driver registration, duty status toggle, delivery queues, and earnings calculation |
| **Admin Controller** | <img src="https://img.icons8.com/color/32/000000/admin-settings-male.png"/> | Handles administrative login, global shipment auditing, and driver document approvals |
| **Shipment Controller** | <img src="https://img.icons8.com/color/32/000000/box.png"/> | Manages shipment status transitions (`Pending` $\rightarrow$ `InTransit` $\rightarrow$ `Delivered`) and GPS updates |
| **Security Configuration** | <img src="https://img.icons8.com/color/32/000000/security-checked.png"/> | Manages OAuth2 login, public endpoints, and Spring Security session invalidation |
| **REST Service Layer** | <img src="https://img.icons8.com/color/32/000000/services.png"/> | RestTemplate communication bridge to the Backend REST Microservice (Port 3333) |

### 🔄 **Data Flow:**

#### 📨 **Shipment Booking & AI Dispatch Flow:**
```
Customer inputs Cargo Dimensions (Weight, L, W, H)
    ↓
Groq AI LLM classifies freight tier (Bike / Cargo Auto / Mini Truck / Truck)
    ↓
Customer selects Pickup & Drop points via Leaflet Map
    ↓
DriverService executes 2-Stage Dispatch (Haversine + OSRM road travel ETA)
    ↓
Optimal Driver Assigned & Razorpay Checkout Modal rendered
    ↓
Payment Success → Shipment saved as 'Pending' → Driver duty set to 'Busy'
```

#### 🛰️ **Live Telemetry & GPS Tracking Flow:**
```
Driver updates position via GPS Update Console
    ↓
Backend persists current_latitude & current_longitude
    ↓
Customer Tracking Dashboard polls /customer/shipment/{id}/tracking (every 10s)
    ↓
Leaflet.js dynamically animates vehicle marker along Route Polyline
```

---

## 📁 **Project Structure**

```
🚚 AI-Logistics-FrontEnd/
├── 📂 src/
│   ├── 📂 main/
│   │   ├── 📂 java/com/aniket/
│   │   │   ├── 📂 bean/
│   │   │   │   ├── 📄 Admin.java
│   │   │   │   ├── 📄 Customer.java
│   │   │   │   ├── 📄 Driver.java
│   │   │   │   └── 📄 Shipment.java
│   │   │   ├── 📂 controller/
│   │   │   │   ├── 📄 AILogisticsController.java
│   │   │   │   ├── 📄 AdminController.java
│   │   │   │   ├── 📄 CustomerController.java
│   │   │   │   ├── 📄 DriverController.java
│   │   │   │   └── 📄 ShipmentController.java
│   │   │   ├── 📂 security/
│   │   │   │   └── 📄 SecurityConfig.java
│   │   │   └── 📂 service/
│   │   │       ├── 📄 AILogisticService.java
│   │   │       ├── 📄 AdminService.java
│   │   │       ├── 📄 CustomerService.java
│   │   │       ├── 📄 DriverService.java
│   │   │       ├── 📄 MailService.java
│   │   │       └── 📄 ShipmentService.java
│   │   └── 📂 resources/
│   │       ├── 📂 static/assets/
│   │       │   ├── 📂 css/
│   │       │   │   └── 📄 style.css
│   │       │   └── 📂 js/
│   │       │       └── 📄 app.js
│   │       ├── 📂 templates/
│   │       │   ├── 📂 admin/
│   │       │   │   ├── 📄 admin-dashboard.html
│   │       │   │   ├── 📄 driver_details.html
│   │       │   │   └── 📄 drivers.html
│   │       │   ├── 📂 customer/
│   │       │   │   ├── 📄 book-shipment-now.html
│   │       │   │   ├── 📄 book-shipment.html
│   │       │   │   ├── 📄 customer-dashboard.html
│   │       │   │   ├── 📄 shipment_details.html
│   │       │   │   ├── 📄 shipments.html
│   │       │   │   └── 📄 track.html
│   │       │   ├── 📂 driver/
│   │       │   │   ├── 📄 driver-dashboard.html
│   │       │   │   ├── 📄 driver-earnings.html
│   │       │   │   └── 📄 shipment_details.html
│   │       │   ├── 📄 index.html
│   │       │   ├── 📄 login.html
│   │       │   └── 📄 register.html
│   │       └── 📄 application.properties.example
├── 📄 .gitignore
├── 📄 FUTURE_ENHANCEMENTS.md
├── 📄 pom.xml
└── 📄 README.md
```

---

## 🌐 **Web Routes & Endpoints**

| Method | Route / URL | Logo | Access Role | Description |
|:---:|---|:---:|:---:|---|
| `GET` | `/` | <img src="https://img.icons8.com/color/24/000000/home.png"/> | Public | ShippSure landing page with service overview |
| `GET` | `/login` | <img src="https://img.icons8.com/color/24/000000/login-rounded-right.png"/> | Public | Multi-role tabbed login (Customer, Driver, Admin) |
| `GET` | `/register` | <img src="https://img.icons8.com/color/24/000000/add-user-male.png"/> | Public | Registration page with driver document upload |
| `GET` | `/customer/customer-dashboard` | <img src="https://img.icons8.com/color/24/000000/dashboard.png"/> | Customer | Customer overview, active KPIs, and recent orders |
| `GET` | `/customer/book-shipment` | <img src="https://img.icons8.com/color/24/000000/box.png"/> | Customer | Cargo dimension input and AI vehicle recommendation |
| `POST` | `/customer/book-shipment-now` | <img src="https://img.icons8.com/color/24/000000/route.png"/> | Customer | 2-stage dispatch calculation and Razorpay checkout |
| `POST` | `/customer/PayNow` | <img src="https://img.icons8.com/color/24/000000/bank-cards.png"/> | Customer | Confirms booking, transitions driver duty to Busy |
| `GET` | `/customer/shipment/{id}/tracking` | <img src="https://img.icons8.com/color/24/000000/marker.png"/> | Customer | JSON endpoint returning driver latitude & longitude |
| `GET` | `/driver/driver-dashboard` | <img src="https://img.icons8.com/color/24/000000/delivery.png"/> | Driver | Driver queue with status filter pills |
| `GET` | `/driver/SetRideStatus` | <img src="https://img.icons8.com/color/24/000000/switch.png"/> | Driver | Toggles driver duty status between Free and Busy |
| `GET` | `/driver/driver-earnings` | <img src="https://img.icons8.com/color/24/000000/money.png"/> | Driver | Shows total earned amount and completed trips |
| `GET` | `/admin/admin-dashboard` | <img src="https://img.icons8.com/color/24/000000/speed.png"/> | Admin | Global network shipments manifest and pending reviews |
| `GET` | `/admin/drivers` | <img src="https://img.icons8.com/color/24/000000/conference-call.png"/> | Admin | Renders all drivers filterable by approval status |
| `GET` | `/logout` | <img src="https://img.icons8.com/color/24/000000/shutdown.png"/> | All Roles | Invalidates session and redirects to homepage |

---

## 🚀 **Getting Started**

### 📋 **Prerequisites**

| Software | Version | Logo |
|----------|---------|:---:|
| ☕ **Java** | 17+ | <img src="https://img.icons8.com/color/24/000000/java-coffee-cup-logo.png"/> |
| 📦 **Maven** | 3.8+ | <img src="https://img.icons8.com/color/24/000000/maven.png"/> |
| 🗄️ **MySQL** | 8.0+ | <img src="https://img.icons8.com/color/24/000000/mysql-logo.png"/> |
| 💻 **IDE** | STS / IntelliJ / Eclipse / VS Code | <img src="https://img.icons8.com/color/24/000000/visual-studio-code-2019.png"/> |

---

### 📥 **Step-by-Step Installation**

#### 1️⃣ Start Backend Service
Make sure the [**ShippSure Backend Service**](https://github.com/aniketlabs77/ShippSure-The-AI-Logistics-SpringBoot-Backend-) is running on `http://localhost:3333`.

#### 2️⃣ Clone Frontend Repository
```bash
git clone https://github.com/aniketlabs77/ShippSure---The-AI-Logistics-Frontend-SpringBoot-Thymleaf-.git
cd ShippSure---The-AI-Logistics-Frontend-SpringBoot-Thymleaf-
```

#### 3️⃣ Environment & Properties Setup
Copy the sanitized configuration template:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

*(All sensitive keys support fallback defaults for local development or can be customized via OS environment variables: `RAZORPAY_KEY_ID`, `RAZORPAY_KEY_SECRET`, `GROQ_API_KEY`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`, `MAIL_USERNAME`, `MAIL_PASSWORD`)*

#### 4️⃣ Build & Run
```bash
# Build the project
mvn clean install

# Start the Spring Boot application (Runs on Port 2222)
mvn spring-boot:run
```

Open your browser and navigate to:  
👉 **`http://localhost:2222`**

---

## 👥 **Demo Accounts (Local Testing)**

| Role | Email | Password | Access Details |
|:---:|:---|:---|:---|
| 🛡️ **Admin** | `admin@gmail.com` | `admin123` | Full network oversight & driver verification |
| 🚚 **Driver** | Set during registration | Set during registration | Document upload & duty dispatch |
| 📦 **Customer** | Direct Form Signup OR Google Login | Set during registration | AI booking, Razorpay, and live map |

---

## ✅ **Task & Feature Completion Status**

| Task | Logo | Feature Description | Status |
|:---:|:---:|---------------------|:---:|
| **Task 1** | <img src="https://img.icons8.com/color/24/000000/artificial-intelligence.png"/> | AI Vehicle Recommendation via Groq LLM | ✅ Complete |
| **Task 2** | <img src="https://img.icons8.com/color/24/000000/route.png"/> | 2-Stage Proximity Dispatch (Haversine + OSRM) | ✅ Complete |
| **Task 3** | <img src="https://img.icons8.com/color/24/000000/bank-cards.png"/> | Razorpay Payment Gateway & Driver Duty Sync | ✅ Complete |
| **Task 4** | <img src="https://img.icons8.com/color/24/000000/map-marker.png"/> | Real-Time Leaflet GPS Telemetry Polling | ✅ Complete |
| **Task 5** | <img src="https://img.icons8.com/color/24/000000/shield.png"/> | Google OAuth2, BCrypt & Defensive Exception Guards | ✅ Complete |
| **Task 6** | <img src="https://img.icons8.com/color/24/000000/conference-call.png"/> | Customer, Driver & Admin Thymeleaf Portals | ✅ Complete |
| **Task 7** | <img src="https://img.icons8.com/color/24/000000/lock.png"/> | Credential Externalization & Git Sanitization | ✅ Complete |

---

## 👤 **Author**

### Aniket Singh Chauhan

<table>
  <tr>
    <td><img src="https://img.icons8.com/fluency/48/000000/github.png"/></td>
    <td><b>GitHub</b></td>
    <td><a href="https://github.com/aniketlabs77">@aniketlabs77</a></td>
  </tr>
  <tr>
    <td><img src="https://img.icons8.com/fluency/48/000000/linkedin.png"/></td>
    <td><b>LinkedIn</b></td>
    <td><a href="https://linkedin.com/in/aniket-singh-chauhan-740350305/">Aniket Singh Chauhan</a></td>
  </tr>
  <tr>
    <td><img src="https://img.icons8.com/fluency/48/000000/gmail.png"/></td>
    <td><b>Email</b></td>
    <td><a href="mailto:aniketsinghchauhan77@gmail.com">aniketsinghchauhan77@gmail.com</a></td>
  </tr>
</table>

---

<div align="center">

**ShippSure — Move Smart. Ship Sure.**

**Spring Boot 3 + Groq AI + Razorpay + Leaflet.js + Java 17**

</div>
