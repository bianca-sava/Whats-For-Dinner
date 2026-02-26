# What's For Dinner

A full-stack web application that helps you cook smarter and waste less by tracking what's in your fridge and suggesting recipes based on your available ingredients.

**Live demo:** [whats-for-dinner-black.vercel.app](https://whats-for-dinner-black.vercel.app)

---

## Features

- **Fridge tracker** — Add ingredients manually or scan a shopping receipt using AI (powered by Gemini)
- **Recipe search** — Find recipes you can make with what you already have, with filters for meal type, diet type, and number of missing ingredients
- **Allergy & diet preferences** — Set dietary preferences (vegetarian, vegan) and allergy warnings that are applied automatically to recipe results
- **Onboarding flow** — First-time setup to personalize the experience
- **JWT authentication** — Secure register/login with token-based sessions
- **Installable PWA** — Can be added to the home screen on iOS and Android

---

## Tech Stack

**Frontend**
- React + TypeScript (Vite)
- Tailwind CSS
- React Router, TanStack Query, Axios

**Backend**
- Java 17, Spring Boot 3
- Spring Security with JWT
- Spring Data JPA

**Database**
- PostgreSQL (hosted on Neon)

**AI**
- Google Gemini API — for parsing shopping receipts and extracting ingredients

**Deployment**
- Frontend: Vercel
- Backend: Render
- Database: Neon

---

## Getting Started

### Prerequisites

- Node.js 18+
- Java 17+
- PostgreSQL database

### Backend

```bash
# Clone the repo
git clone https://github.com/your-username/whats-for-dinner.git
cd whats-for-dinner/backend

# Set environment variables
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Fill in: DB URL, JWT secret, Gemini API key, CORS allowed origin

# Run
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend

# Install dependencies
npm install

# Set environment variables
echo "VITE_API_URL=http://localhost:8080" > .env.local

# Run
npm run dev
```

---

## Environment Variables

### Backend (Render / application.properties)

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL |
| `SPRING_DATASOURCE_USERNAME` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | DB password |
| `JWT_SECRET` | Secret key for signing JWT tokens |
| `GEMINI_API_KEY` | Google Gemini API key |
| `APP_CORS_ALLOWED_ORIGIN` | Frontend URL (e.g. https://your-app.vercel.app) |

### Frontend (Vercel / .env.local)

| Variable | Description |
|---|---|
| `VITE_API_URL` | Backend URL (e.g. https://your-api.onrender.com) |
