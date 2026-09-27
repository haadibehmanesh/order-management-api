# Order Management API

A project built with Java 21, Spring Boot, and Maven.
All code and examples are created for this project without company code or data.

## Features

- Create an order with validated input.
- List orders.
- Find an order by ID.
- Return HTTP 404 when an order is not found.

Each order contains one product, a quantity, a unit price, and a status.
New orders receive a generated UUID and the status NEW.

## Requirements

- JDK 21
- An internet connection for the first build to download dependencies

The Maven Wrapper is included; a separate Maven installation is not required.

## Run

On macOS or Linux, from the project directory:

```sh
./mvnw spring-boot:run
```

The API runs at http://localhost:8080.

## Test

```sh
./mvnw test
```

Tests cover application startup, controller responses, rejection of zero
quantity, order creation, and lookup of a stored order.

## Endpoints

| Method | Path | Success | Description |
|--------|------|---------|-------------|
| POST | /api/orders | 201 | Create an order |
| GET | /api/orders | 200 | List orders |
| GET | /api/orders/{id} | 200 | Find an order; returns 404 if absent |

## Example

```sh
curl -i -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"productName":"Notebook","quantity":2,"unitPrice":12.50}'
```

The product name must not be blank, quantity must be positive,
and unit price must be at least 0.01. All three fields are required.
Invalid input returns HTTP 400.

## Current Limitations

- Orders are stored in memory and disappear when the application stops.
- Order listing has no guaranteed sort order.
- Status changes are not implemented yet.
- Authentication and database persistence are not implemented yet.