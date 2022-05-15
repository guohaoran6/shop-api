# Shop Backend APIs

* This is a Maven Project. Ensure, Maven is installed on your system.

## How to run in local
1. Change the Application Properties (E.g. username/password of DB) present in ``resources/application.yml`` according to your local mysql-server.
1. Create a database called `shop` with ``CHARACTER SET utf8``.
1. Import `schema-mysql.sql` in it, which have included some dummy data.
1. To run the application, you can use the command ``mvn -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true spring-boot:run``
1. After starting application, go to swagger API doc: http://localhost:8080/swagger-ui.html#/

## APIs Manual Request Samples
### User APIs
You need to use login API to generate user token, either admin user or customer user
```
curl --location --request POST 'http://localhost:8080/api/v1/user/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userName": "admin",
    "passwordMd5": "e10adc3949ba59abbe56e057f20f883e"
}'
```
OR
```
curl --location --request POST 'http://localhost:8080/api/v1/user/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userName": "user",
    "passwordMd5": "e10adc3949ba59abbe56e057f20f883e"
}'
```
After that, you can get the response with user token. eg. `89b7e228113ce1a562bdb9521c210847`, which will use for other APIs.

### Product APIs
#### [GET] Fetch a product info
<Notice> Replace the token in Authorization param.

```
curl --location --request GET 'http://localhost:8080/api/v1/products/1' \
--header 'Authorization: fcea99281fd536c110e6818fd50635d1'
```

#### [GET] Search products
```
curl --location --request GET 'http://localhost:8080/api/v1/products/search?keyword=iPhone&pageNumber=1' \
--header 'Authorization: fcea99281fd536c110e6818fd50635d1'
```

#### [POST] Add a new product
```
curl --location --request POST 'http://localhost:8080/api/v1/products' \
--header 'Authorization: fcea99281fd536c110e6818fd50635d1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Macbook Pro",
    "desc": "manual",
    "imgUrl": "/test/test.img",
    "price": 12000.0,
    "stockNumber": 1228,
    "tag": "pc"
}'
```

#### [PUT] Update a product info
```
curl --location --request PUT 'http://localhost:8080/api/v1/products/6' \
--header 'Authorization: fcea99281fd536c110e6818fd50635d1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Macbook Air",
    "desc": "manual",
    "imgUrl": "/test/test.img",
    "price": 3000.0,
    "stockNumber": 320,
    "tag": "pc",
    "version": 2
}'
```

#### [PUT] Delete products
```
curl --location --request PUT 'http://localhost:8080/api/v1/products/delete?productIds=3,4,5' \
--header 'Authorization: fcea99281fd536c110e6818fd50635d1' \
--data-raw ''
```


### Shopping Cart APIs
#### [POST] Add Products to Shopping Cart
```
curl --location --request POST 'http://localhost:8080/api/v1/shopping-carts' \
--header 'Authorization: 89b7e228113ce1a562bdb9521c210847' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productCount": 3,
    "productId": 1
}'
```

#### [GET] Get Shopping Cart Items
```
curl --location --request GET 'http://localhost:8080/api/v1/shopping-carts/page?pageNumber=1' \
--header 'Authorization: 89b7e228113ce1a562bdb9521c210847'
```

### Order APIs
#### [POST] Save / Generate Order
```
curl --location --request POST 'http://localhost:8080/api/v1/orders/save?cartItemIds=1,3' \
--header 'Authorization: 89b7e228113ce1a562bdb9521c210847' \
```


## How to use prod profile
mvn package
