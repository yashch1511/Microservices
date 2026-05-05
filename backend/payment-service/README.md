# payment-service

Mock payment microservice wired via Kafka.

## Kafka flow

- Order Service publishes `OrderPlacedEvent` to topic `order-placed-topic`
- Payment Service consumes `order-placed-topic`, creates a `Payment` record, and publishes `PaymentStatusEvent` to `payment-status-topic`
- Order Service consumes `payment-status-topic` and updates order status to `PAID` or `PAYMENT_FAILED`

Mock rule: every 5th `orderId` fails (deterministic).

## Run

1. Start Kafka + Zookeeper:
   - `docker compose up -d`
2. Start services (each in its module folder):
   - `order-service/order-service/mvnw.cmd spring-boot:run`
   - `payment-service/payment-service/mvnw.cmd spring-boot:run`

Payment Service runs on port `8086` and exposes:

- `GET /payments`
- `GET /payments/order/{orderId}`

