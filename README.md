# InsightInflux
InsightInflux is a Spring Boot-based REST API for managing products and their reviews. Developed in Java 17 and utilizing Maven for project management, it integrates with the H2 in-memory database for storage. 

#How to run the project

1. **Run docker commands**
   ```shell
   docker build -t insightinflux .
   docker run -p 9060:9060 insightinflux

2. **Once started go to one of the endpoints**
   http://localhost:9060/           - for frontend
   http://localhost:9060/h2-console - for database
   
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
   curl -X GET http://localhost:9060/api/products

3. **Product update example via curl**
   ```shell
   curl -X PUT http://localhost:9060/api/products/1 \
     -H 'Content-Type: application/json' \
     -d '{
         "code": "REG00001",
         "name": "Regular Product",
         "priceEur": 19.99,
         "description": "This regular product does just what you need."
     }'

4. **Product delete example via curl**
   ```shell
   curl -X DELETE http://localhost:9060/api/products/1

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

3. **Review update example via curl**
   ```shell
   curl -X PUT http://localhost:9060/api/reviews/1 \
   -H 'Content-Type: application/json' \
   -d '{
      "reviewer": "Pero",
      "text": "Edit: died after a month. Disappointing!",
      "rating": 1
   }'

4. **Review delete example via curl**
   ```shell
   curl -X DELETE http://localhost:9060/api/reviews/1

## Price Conversion: 
Integrates with the HNB API for conversion of product prices between EUR and USD.

Done by fetching "prodajni_tecaj" value from:
https://api.hnb.hr/tecajn-eur/v3?valuta=USD

## Search and Filter: 
Offers functionality to search for products by code or name, with case-insensitive matching that includes partial matches.

1. **Search product by code example via curl**
   ```shell
   curl -X GET http://localhost:9060/api/products?code=UniQ1

2. **Search product by name example via curl**
   ```shell
   curl -X GET http://localhost:9060/api/products?name=SuPeR

## Popular Products: 
An endpoint to identify and list the top three products based on their average review rating.

1. **Top 3 popular Products read example via curl**
   ```shell
   curl -X GET http://localhost:9060/api/products/popular

## H2 database preload
On application startup, InsightInflux automatically preloads a predefined dataset 

## Access to h2 database via h2-console:
To access database you can login via: http://localhost:9060/h2-console
Access data is:
   ```shell
   Saved Settings:Generic H2 (Embedded)
   Setting Name:Generic H2 (Embedded)
   
   Driver Class:org.h2.Driver
   JDBC URL:jdbc:h2:mem:flux_db
   User Name:pero
   Password:pero1234!	
