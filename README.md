# Ticket Booking Platform — Backend

Backend API for a concert ticket booking platform — supports multi-item cart bookings, mock payment flow, and an operations dashboard for admins/operators.

🔗 **Live API:** https://ticket-booking-backend-a2sx.onrender.com
🔗 **Live Demo (Frontend):** https://ticket-booking-frontend-gules.vercel.app
🔗 **API Docs (Swagger):** https://ticket-booking-backend-a2sx.onrender.com/swagger-ui.html

> ⚠️ Deployed on Render's free tier — the first request after a period of inactivity may take 30–50 seconds while the instance spins back up.

## Tech Stack

- Java 21, Spring Boot 3, Spring Security (JWT)
- PostgreSQL (Neon serverless)
- Docker (deployed on Render)
- Swagger/OpenAPI (springdoc)

## Key Features

**Customer-facing**

- Registration / login (stateless JWT)
- Browse concerts, view ticket categories and prices
- Book tickets via a **shopping cart** — a single booking can include multiple different ticket categories
- Apply promotional vouchers
- Mock payment flow: `PENDING_PAYMENT` → `CONFIRMED`
- View booking history, cancel bookings

**Operation Dashboard**

- Manage concerts, ticket categories, voucher campaigns
- Monitor all bookings, filterable by status
- Manually update a booking's status (resolve failed/suspicious bookings)
- Overview statistics: revenue, tickets sold, booking counts by status

## Three Core Technical Problems Solved

1. **Preventing overselling** — uses an atomic `UPDATE ... WHERE quantity >= :qty` at the repository layer instead of a read-then-write pattern, avoiding race conditions when multiple requests book tickets concurrently.
2. **Preventing duplicate bookings from retries** — the client sends an `idempotencyKey`; a unique constraint on `(user_id, idempotency_key)` at the database level ensures a retried request never creates a duplicate booking.
3. **Preventing voucher abuse** — total redemption limits enforced via atomic decrement, per-user limits enforced via a unique constraint on `(voucher_id, user_id)`.

## Running Locally

### Requirements

- Java 21, Maven, PostgreSQL (this project uses Neon cloud DB)

### Configuration

Set the following environment variables before running (never commit real values to code):
DB_URL=jdbc:postgresql://<host>/neondb?sslmode=require
DB_USERNAME=<username>
DB_PASSWORD=<password>
JWT_SECRET=<a sufficiently long random string>

### Build & Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The app runs at `http://localhost:8080`

### Docker (matches the production environment)

```bash
docker build -t ticket-booking-backend .
docker run -p 8080:8080 --env-file .env ticket-booking-backend
```

## Main API Endpoints

| Method | Path                            | Description                        |
| ------ | ------------------------------- | ---------------------------------- |
| POST   | /api/auth/register              | Register a new account             |
| POST   | /api/auth/login                 | Login, returns a JWT               |
| GET    | /api/concerts                   | List concerts                      |
| GET    | /api/ticket-types/concert/{id}  | Ticket categories for a concert    |
| POST   | /api/bookings                   | Create a booking (multi-item cart) |
| POST   | /api/bookings/{id}/pay          | Pay for a booking (mock)           |
| GET    | /api/bookings/my                | Booking history                    |
| PATCH  | /api/bookings/{id}/cancel       | Cancel a booking                   |
| GET    | /api/admin/bookings             | [Admin] View all bookings          |
| PUT    | /api/admin/bookings/{id}/status | [Admin] Update booking status      |
| GET    | /api/admin/bookings/dashboard   | [Admin] Overview statistics        |

The full, interactive list is available via Swagger UI (link above).

## Architecture & Database Design

See `ARCHITECTURE.md` for full details.

## Assumptions & Known Limitations

See `ASSUMPTIONS.md` for full details.

## Deployment

- **Backend:** Render (Docker), auto-deploys on every push to `main`
- **Database:** Neon (serverless PostgreSQL)
