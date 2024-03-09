# InsightInflux
InsightInflux is a Spring Boot-based REST API for managing products and their reviews. Developed in Java 17 and utilizing Maven for project management, it integrates with the H2 in-memory database for storage. 

# Core Functionalities:

## Product Management: 
CRUD operations on products, creation, retrieval, updating, and deletion.

1. **Product creation example via curl**
   ```shell
   curl -X POST http://localhost:9060/api/products \
    -H 'Content-Type: application/json' \
    -d '{
    "name": "Super Unique Product",
    "code": "UNIQ12345",
    "priceEur": 39.99,
    "description": "This unique product stands out with its exceptional quality."
    }'

2. **Product read example via curl**
   ```shell
   curl -X GET http://localhost:9060/api/reviews

## Review Handling: 
Allows for the posting and retrieval of product reviews, and a rating system alongside textual feedback.

1. **Review creation example via curl**
   ```shell
    curl -X POST http://localhost:9060/api/reviews \
    -H 'Content-Type: application/json' \
    -d '{
        "productId": 1,
        "reviewer": "Pero",
        "text": "Amazing product!",
        "rating": 5
    }'

2. **Review read example via curl**
   ```shell
   curl -X GET http://localhost:9060/api/reviews

## Price Conversion: 
Integrates with the HNB API for conversion of product prices between EUR and USD.

## Search and Filter: 
Offers functionality to search for products by code or name, with case-insensitive matching that includes partial matches.

## Popular Products: 
An endpoint to identify and list the top three products based on their average review rating.

On application startup, InsightInflux automatically preloads a predefined dataset 
