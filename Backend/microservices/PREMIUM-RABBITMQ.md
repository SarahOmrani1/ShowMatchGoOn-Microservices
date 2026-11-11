# Asynchronous Premium subscription

## RabbitMQ

Start the broker from `Backend/microservices`:

```powershell
docker compose -f rabbitmq-compose.yml up -d
```

The broker exposes only AMQP port `5672`; no RabbitMQ web console port is published. Set `RABBITMQ_USERNAME` and `RABBITMQ_PASSWORD` before starting the services if you change the compose defaults.

All six services use the durable topic exchange `showmatch.events`. The subscription request key is `subscription.premium.activation.requested`; the validated user response is `user.premium.activation.validated`; activation is broadcast with `subscription.premium.activated`.

## Start order

Start RabbitMQ, MongoDB, MySQL, Eureka, then the `user`, `subscription-loyalty`, `watchparty-chat`, `cinema-reservation`, `forum-promotion`, and `content-notification` services. The activation consumer queues are declared by their services when they start.

## Test with Postman

Create a Premium request:

```http
POST http://localhost:8888/api/subscriptions/premium
Content-Type: application/json
```

```json
{
  "userId": 1
}
```

The API responds `202 Accepted` with a subscription ID and `PENDING` status. Poll the returned ID until processing finishes:

```http
GET http://localhost:8888/api/subscriptions/premium/{subscriptionId}
```

An active user leads to `ACTIVE`; a missing, disabled, or locked user leads to `REJECTED`. On activation, the user record is marked Premium, and the watch-party, cinema, forum, and content services each persist their own Premium entitlement or activation notification.

Verify the consumer projections in Postman with these GET requests (restart the Gateway after adding its routes):

```http
GET http://localhost:8888/api/watchparty/premium/entitlements/{userId}
GET http://localhost:8888/api/cinema/premium/entitlements/{userId}
GET http://localhost:8888/api/forum/premium/entitlements/{userId}
```

The direct service URLs, useful for distinguishing a Gateway routing issue from a missing projection, are:

```http
GET http://localhost:8084/api/watchparty/premium/entitlements/{userId}
GET http://localhost:8085/api/cinema/premium/entitlements/{userId}
GET http://localhost:8086/api/forum/premium/entitlements/{userId}
```

Each endpoint returns `200` and the entitlement when that consumer has processed the activation; otherwise it returns `404`.