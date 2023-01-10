# E2: JDBC Database Access &nbsp; (<span style="color:red"> X Pts </span>)

*JDBC (Java DataBase Connectivity)* is the most basic interface to access data in a database.

Spring JDBC consists of:

- a *database connector* to establish a connection to the database with

    - a Maven dependency of the `spring-boot-starter-data-jpa` package:

        ```
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        ```

    - the configuration of the database connection in `src/main/resources/application.yaml`:

        ```sh
        # connection to database (assumed running on localhost, listening on port 3306)
        #
        spring:
          datasource:
            url: jdbc:mysql://localhost:3306/FREERIDER_DB
            username: freerider
            password: free.ride
        ```

- an access class `JdbcTemplate` ( [javadoc](`https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html`) )
that offers database operations to:

    - `execute` SQL statements expecting no data returned.

    - `query` SQL statements that expect data being returned as `ResultSet`.

    - `update` SQL statements that alter data (SQL INSERT, UPDATE, DELETE) 

- a data structure called `ResultSet` that contains results from a query structured
    as a set of rows (no order is guaranteed) of indexed arrays of data for each
    row with data types as stored in the database.

    ```
    Query: SELECT * FROM CUSTOMER WHERE ID <= 3;
    
    ResultSet:
    index:
     1.(int)  2. (String)          3. (String)          4. (String)
    +-------+---------------------+--------------------+--------------+
    |  ID   | NAME                | CONTACT            | STATUS       |
    +-------+---------------------+--------------------+--------------+
    |   1   | Meyer, Eric         | eme22@gmail.com    | Active       | <- row 1
    |   2   | Sommer, Tina        | 030 22458 29425    | Active       | <- row 2
    |   3   | Schulze, Tim        | +49 171 2358124    | Active       | <- row 3
    +-------+---------------------+--------------------+--------------+
    ```

    The `ResultSet` returned for the query is comprised of three rows
    with each row containing data:

    Example of the indexed structure for row 1:
    - index[1]: type `int`, value: `1`,
    - index[2]: type `String`, value: `"Meyer, Eric"`,
    - index[3]: type `String`, value: `"eme22@gmail.com"`,
    - index[4]: type `String`, value: `"Active"`.

Row-data must explictely be converted into objects from a `ResultSet`.



&nbsp;

---
## 1. Enable JDBC database access

Pull from branch [jdbc](https://github.com/sgra64/se2-freerider/tree/jdbc) packages

- `de.freerider.data_jdbc`,

- `de.freerider.datamodel`

and add to the `se2-freerider` project.

Pull package [de.freerider.datamodel]()
and add to the project (datamodel classes: Customer.java, Vehicle.java, Reservation.java).

Add the dependency `spring-boot-starter-data-jpa`
and set the database connection in `src/main/resources/application.yaml`
as descibed above.

Validate that the project compiles with the `de.freerider.data_jdbc` package.

```
mvn compile
```











<!-- [](https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch01.html)

Terms *Inversion of Control (IoC)* and *Dependency Injection (DI)* are often
used interchangeably.



[database](https://github.com/sgra64/db-freerider)
and provide REST endpoints for a car sharing reservation system.

Spring Boot is a modular framework suitable to build Java applications that expose REST endpoints
and require access to databases.

Creating a Spring Boot application requires using a build-tool (*maven* or *gradle*) and
follows certain steps. A Spring Boot project also has a certain structure.

### Challenges
1. [Challenge 1:](#1-initialize-new-spring-boot-project) Initialize new Spring Boot Project - (2 Pts)
2. [Challenge 2:](#2-build-and-run-the-application) Build and Run the Application - (2 Pts)
3. [Challenge 3:](#3-build-and-run-tests) Build and Run Tests - (2 Pts)
4. [Challenge 4:](#4-build-and-run-test-reports) Build and Run Test Reports - (1 Pts)
5. [Challenge 5:](#5-check-project-into-remote-repository) Check Project into your own Remote Repository - (2 Pts)


&nbsp;

---
## 1. Initialize new Spring Boot Project

Configure  -->