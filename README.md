# 🚀 API Rate Limiter — Scalable Token Bucket System with Redis

## 🌐 Live Demo

👉 Frontend (Vercel): https://your-vercel-link.vercel.app
👉 Backend (Render): https://api-rate-limiter-xknt.onrender.com

---

## 📌 Overview

API Rate Limiter is a **production-style backend system** designed to control and throttle API usage using a **Token Bucket algorithm powered by Redis**.

The system simulates **real-world API gateway behavior**, where users are assigned different plans (FREE / PREMIUM) and are allowed a limited number of requests over time.

It includes a **minimal frontend dashboard** to visualize:

* Request consumption
* Token refill behavior
* Rate limit enforcement in real time

This project demonstrates **scalable backend design, distributed rate limiting, and system observability**.

---

## 🎯 Key Features

* 🔐 API Key-based request identification
* ⚙️ Token Bucket Rate Limiting Algorithm
* ⚡ Redis-powered fast in-memory state management
* 🧠 Dynamic user plans (FREE vs PREMIUM)
* 📉 Real-time token consumption tracking
* 🔄 Automatic token refill (per second)
* 🚫 429 Too Many Requests handling
* 🌐 REST API with interceptor-based enforcement
* 🖥️ Lightweight frontend for visualization
* ☁️ Deployed backend + frontend (Render + Vercel)

---

## 🧠 Tech Stack

### ⚙️ Backend

* Java
* Spring Boot
* Spring MVC (Interceptor-based request filtering)

### ⚡ Rate Limiting Engine

* Redis (via StringRedisTemplate)
* Token Bucket Algorithm

### 🖥️ Frontend

* React (Vite)
* Minimal CSS (clean UI, not design-heavy)

### ☁️ Deployment

* Backend: Render
* Frontend: Vercel

---

## 🧱 System Architecture

The system follows a **layered backend architecture with distributed state management**.

### 1. Client Layer (Frontend)

* Sends API requests with `X-API-KEY`
* Displays:

  * Plan
  * Limit
  * Remaining tokens
  * Status (SUCCESS / RATE LIMITED)
* Visualizes token usage with a progress bar

---

### 2. Interceptor Layer (Spring Boot)

* Implemented using `HandlerInterceptor`
* Executes **before controller logic**
* Responsibilities:

  * Validate API Key
  * Enforce rate limit
  * Return `429` if exceeded

```java
preHandle(...)
```

👉 This mimics **real-world API gateways (like AWS API Gateway / NGINX)**

---

### 3. Service Layer

* Handles business logic
* Fetches:

  * User plan
  * Request limits
* Delegates to Redis layer

---

### 4. Redis Rate Limiter Layer

Core logic of the system:

* Stores:

  * `tokens:<userId>`
  * `lastRefill:<userId>`

* Performs:

  * Token refill calculation
  * Token deduction
  * Request validation

---

### 5. Configuration Layer

```java
RateLimitConfig.java
```

Defines:

* User plans
* Request limits

Example:

```java
"user1" → FREE → 5 requests
"user2" → PREMIUM → 20 requests
```

---

## ⚙️ Rate Limiting Algorithm (Token Bucket)

The system uses the **Token Bucket Algorithm**, widely used in:

* API Gateways
* Network Traffic Control
* Distributed Systems

---

### 🔄 How it works

1. Each user has a bucket with max tokens
2. Tokens refill at a constant rate (1/sec)
3. Each request consumes 1 token
4. If tokens = 0 → request blocked (429)

---

### 📊 Example

| User  | Plan    | Limit | Behavior                  |
| ----- | ------- | ----- | ------------------------- |
| user1 | FREE    | 5     | Gets rate limited quickly |
| user2 | PREMIUM | 20    | Higher throughput         |

---

### 🧠 Why Token Bucket?

Compared to Fixed Window:

| Feature          | Token Bucket | Fixed Window |
| ---------------- | ------------ | ------------ |
| Burst handling   | ✅ Yes        | ❌ No         |
| Smooth limiting  | ✅ Yes        | ❌ No         |
| Real-world usage | ✅ High       | ⚠️ Limited   |

---

## 🔑 API Usage

### Endpoint

```http
GET /api/test
```

### Headers

```http
X-API-KEY: user1
```

---

### ✅ Success Response

```json
{
  "message": "API Working",
  "plan": "FREE",
  "limit": 5,
  "requestsLeft": 3
}
```

---

### ❌ Rate Limited Response

```http
429 Too Many Requests
```

---

## 🖥️ Frontend Features

Minimal but **functionally strong UI**:

* Plan display (FREE / PREMIUM)
* Limit display
* Remaining requests
* Status indicator:

  * 🟢 SUCCESS
  * 🔴 RATE LIMITED
* Progress bar → visual token bucket

👉 Designed to **explain backend behavior visually**

---

## 🚀 Run Locally

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

(Windows)

```bash
mvnw.cmd spring-boot:run
```

---

### Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## ☁️ Deployment Details

### Backend (Render)

* Java Spring Boot service
* Public API endpoint
* Handles rate limiting

### Frontend (Vercel)

* Connected to backend API
* Displays live token behavior

---

## 🧠 Engineering Highlights

* Interceptor-based request filtering (clean separation of concerns)
* Redis-based distributed rate limiting
* Token Bucket implementation (industry standard)
* Plan-based dynamic throttling
* Stateless backend with external state (Redis)
* Real-time observability via frontend
* Production-style API response handling

---

## 📈 Future Improvements

* JWT-based authentication (instead of API key)
* Distributed rate limiting across multiple instances
* Sliding window algorithm comparison
* Dashboard analytics (per-user usage stats)
* Rate limit tiers configurable via DB
* WebSocket-based real-time updates

---

## 👨‍💻 Author

Ghanashyam D

---

## ⭐ If you like this project

Give it a star ⭐ on GitHub!
