# RetailService
================

This Project is the implementation of the myRetail Restful service case study.

Requirement:
-----------
The requirements are to support
```sh
1) HTTP GET request at /products/{id}
   Example response: {"id":13860428,"name":"The Big Lebowski (Blu-ray)","current_price":{"value": 13.49,"currency_code":"USD"}}
   Here the Product name is available through an external service and the price details are available in a data store.
2) (BONUS:) Also accept HTTP PUT request at the same path (/products/{id}) to update the price info in the data store.

Solution:
--------
```sh
This solution is based on microservices architecture.
The price info is persisted in Mongo DB for this implementation.
The Price info interaction with the DB is implemented through a pricing microservice.
The RetailService is the EdgeService, exposed for external interaction of GET and PUT requirements of this case study. This will interact with the external service to fetch the product name and as well interact with the Pricing service for the product price's details and is responsbile to combine the response in an effective way to address the use case needs.
The implementation uses Spring Boot 4 with Java 8.
```

Deployment Strategy:
--------------------
```sh
 Mongo is setup in a separate Docker container.
 The pricing microservice and the RetailEdge service are spun on two separate docker containers.
 Containerized application model gives the ease of flexible deployment option in  scaling choices and can easily be hosted on distributed environments with Load Balancers like niginx in place.
In the case of larger enterprise level Application the needs spectrum shifts to Centralized Management and Authentication, Authorization etc, these can be addressed  and can be better managed by use of API Managers/API Gateways like WSO2, Apigee. They also gives a very granular analytics on the API usages, it has lots of cool features, like oauth support, or custom application bases Authentication & Authorization, better control on throttling API Usages.
```

Execution Steps:
----------------

1) Mongo DB Container:

	a) Location: goto DataStore/Mongo
	b) docker pull mongo
	c) docker run -d -p 37017:27017 --name mongodb mongo
	d) docker exec -it mongodb /bin/bash
	e) The container might need some upgrade to better manage files and dataload process, upgrade path : 1) apt-get update 2)apt-get install apt-file 3)apt-file update 4) apt-get install vim or apt-get install nano.  
	d) chmod a+x dataload.sh
	e) ./dataload.sh >> dataLoad.log &


2) Pricing Service Container:

	a) goto the location ~ Pricing/src/Docker
	b) docker build -t price:1.0 -t price:latest .
	c) docker run -d -p 8080:8080 --name price --link mongodb price

3) Retail Service Container:

	 a) goto the location ~ RetailStore/src/Docker
	 b) docker build -t retail:1.0 -t retail:latest .
	 c) docker run -d -p 8081:8081 --name retail --link price  retail

Once these steps are complete the Application will be accessible at the host:8081 with operational access to GET and PUT URL as below, it can be tested with the any REST clients. Set Header - ContentType: Applciation/json.
 ```sh
  GET: http://localhost:8081/v1/products/13860428
  PUT: http://localhost:8081/v1/products/13860428
   Sample RequestBody for put call:
   {
    "value": 111,
    "currency_code": "USD"
   }
  ```
