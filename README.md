# E2: JDBC Database Access &nbsp; (<span style="color:red"> 12 Pts </span>)

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
### Challenges
1. [Challenge 1:](#1-start-the-database-server) Start the database server - (1 Pt)
2. [Challenge 2:](#2-enable-spring-for-jdbc-access-to-the-database) Enable Spring for JDBC access to the database - (1 Pt)
3. [Challenge 3:](#3-understand-the-dao-dataaccess-interface) Understand the DAO DataAccess interface - (2 Pts)
4. [Challenge 4:](#4-understand-dao-queries) Understand DAO Queries - (2 Pts)
5. [Challenge 5:](#5-understand-jdbc-code) Understand JDBC Code - (1 Pt)
6. [Challenge 6:](#6-complete-the-vehicle-data-model-class) Complete the Vehicle data model class - (2 Pts)
7. [Challenge 7:](#7-implement-the-dataaccessvehicles-dao-interface) Implement the DataAccessVehicles DAO interface - (3 Pts)


&nbsp;

---
## 1. Start the database server

Start the database server. Make sure the database is ready and has data.

Run some queries:

```
mysql> SELECT COUNT(*) FROM CUSTOMER;
mysql> SELECT COUNT(*) FROM VEHICLE;
mysql> SELECT COUNT(*) FROM RESERVATION;
```

Output:
```
CUSTOMER:            VEHICLE:             RESERVATION:
+----------+         +----------+         +----------+
| COUNT(*) |         | COUNT(*) |         | COUNT(*) |
+----------+         +----------+         +----------+
|      214 |         |      265 |         |        5 |
+----------+         +----------+         +----------+
```


&nbsp;

---
## 2. Enable Spring for JDBC access to the database

Pull packages from [jdbc](https://github.com/sgra64/se2-freerider/tree/jdbc) branch:

- `de.freerider.data_jdbc`,

- `de.freerider.datamodel`

and add to the `se2-freerider` project.

You can pull packages separately or checkout the entire `jdbc` branch
in a separate directory:

```
git clone --branch jdbc https://github.com/sgra64/se2-freerider.git
```

Check the dependency `spring-boot-starter-data-jpa` in `pom.xml` and add if
needed (see above).

Check the presence of the database connection information in
`src/main/resources/application.yaml`
and add if needed (see above).

Source, build and run the cloned project:

```py
source .env.sh      # set environment variables
mvn package         # full build and package
.run.sh             # run with run-script or with
java -jar target/se2-freerider-0.0.1-SNAPSHOT.jar
```

Output:
```
(0.) Spring Container starting.

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.0.1)

Starting FreeriderApplication v0.0.1-SNAPSHOT using Java 19 with PID 12180 (C:\S
ven1\svgr\tmp\se2-freerider\target\se2-freerider-0.0.1-SNAPSHOT.jar started by s
vgr2 in C:\Sven1\svgr\tmp\se2-freerider)
No active profile set, falling back to 1 default profile: "default"

(1.) FreeriderApplication instance created.
DataFactoryImpl() constructor: instance created
Started FreeriderApplication in 2.342 seconds (process running for 3.23)

(2.) Spring Container ready.
Hello FreeriderApplication!
--> JDBC QueryRunner
Executing SQL query [SELECT COUNT(ID) FROM CUSTOMER]
dao.countCustomers() -> 214
dao.findAllCustomers() -> 214 Customers found
Executing prepared SQL query
Executing prepared SQL statement [SELECT * FROM CUSTOMER WHERE ID = ?]
dao.findCustomerById(24) -> found: Hübner, Kathrin
dao.findAllCustomersById([23, 48, 9600, 92]):
 - found: 23, Kohl, Gero
 - found: 48, Wimmer, Kathleen
 - found: 92, Kärner, Klaus-Jürgen
dao.findReservationsByCustomerId(2):
Executing prepared SQL statement [SELECT RESERVATION.* FROM CUSTOMER JOIN RESERV
ATION ON RESERVATION.CUSTOMER_ID = CUSTOMER.ID WHERE CUSTOMER.ID = ?]
 - RES: 145373, [2022-12-04 20:00:00 - 2022-12-04 23:00:00], Inquired, vehicle:
<UNKNOWN>
 - RES: 351682, [2023-01-07 18:03:26 - 2023-01-07 20:03:26], Inquired, vehicle:
<UNKNOWN>
 - RES: 382565, [2022-12-18 18:00:00 - 2022-12-18 18:10:00], Inquired, vehicle:
<UNKNOWN>
 - RES: 682351, [2022-12-18 10:00:00 - 2022-12-18 16:00:00], Inquired, vehicle:
<UNKNOWN>

(3.) Spring Container exited.
```

The program runs a couple of SQL Queries that are logged to the console with
other output:
```
Executing SQL query [SELECT COUNT(ID) FROM CUSTOMER]
Executing prepared SQL query
Executing prepared SQL statement [SELECT * FROM CUSTOMER WHERE ID = ?]
Executing prepared SQL statement [SELECT RESERVATION.* FROM CUSTOMER JOIN RESERVATION ON RESERVATION.CUSTOMER_ID = CUSTOMER.ID WHERE CUSTOMER.ID = ?]
```

Spring can now connect to the database and run SQL queries.


&nbsp;

---
## 3. Understand the DAO DataAccess interface

DAO (Data Access Object) has emerged as term for "an access object" through which
data in a database are queried.

The DAO offers suitable query methods defined in an interface (`DataAccess.java`)
that are then implemented in an implementation class (`DataAccessImpl.java`).

The implementation class hides and encapsulates the "raw" SQL statements issued
to the database.

It also performs a mapping of data returned from the database (as records with numbers
and Strings) into objects of
[datamodel](https://github.com/sgra64/se2-freerider/tree/jdbc/src/main/java/de/freerider/datamodel)
classes: `Customer.java`, `Vehicle.java` and `Reservation.java`.

The DAO interface
([DataAccess.java](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/java/de/freerider/data_jdbc/DataAccess.java))
therefore presents an abstraction for database access.

Compare the raw SQL statements given in comments with the signature of the methods,
including a complex JOIN-Query in `findReservationsByCustomerId(id)`:

```java
public interface DataAccess {

    /**
     * Run query that returns the number of Customers in the database:
     * - query: SELECT COUNT(ID) FROM CUSTOMER;
     * - returns number extracted from ResultSet.
     * 
     * @return number of Customer records in the database.
     */
    long countCustomers();

    /**
     * Run query that returns one Customers with a given id.
     * - query: SELECT * FROM CUSTOMER WHERE ID = 10;
     * - returns Customer object created from ResultSet row.
     * 
     * @param id Customer id (WHERE ID = ?id)
     * @return Optional with Customer or empty if not found.
     */
    Optional<Customer> findCustomerById(long id);


    /**
     * Run query that returns all Customers with matching id in ids.
     * - query: SELECT * FROM CUSTOMER WHERE ID IN (10, 20, 30000, 40);
     * - returns Customer objects created from ResultSet rows.
     * 
     * @param ids Customer ids (WHERE IN (?ids))
     * @return Customers with matching ids.
     */
    Iterable<Customer> findAllCustomersById(Iterable<Long> ids);


    /**
     * Run query that returns all reservations held by a customer.
     * This is a JOIN-query between Reservation and Customer:
     * - query:
     *     SELECT RESERVATION.* FROM CUSTOMER
     *     JOIN RESERVATION ON RESERVATION.CUSTOMER_ID = CUSTOMER.ID
     *     WHERE CUSTOMER.ID = ?"
     * 
     * @param customer_id id of owning Customer.
     * @return Reservations with matching customer_id.
     */
    Iterable<Reservation> findReservationsByCustomerId(long customer_id);

}
```

Answer questions:

1. How is in `findCustomerById(id)` indicated that a customer does not
    exist for an `id`?

1. Why is `Iterable<T>` used as return type over `List<T>` for some methods?

1. Formulate an SQL-Query that supports a method:
    ```java
    long numberOfReservationsByCustomer(long customer_id);
    ```
    Try the query in `mysql` to see if it returns the desired result. Make sure
    the query **returns only the need data (a number)**, not an entire list.

1. Formulate an SQL-Query that supports a method:
    ```java
    Iterable<Vehicle> findUsableElectricCars();
    ```
    with "usable" refering to electric cars that are not retired or in service.
    Try the query in `mysql`.

1.  Formulate an SQL-Query that supports a method:
    ```java
    Iterable<Reservation> findReservations(long from, long to);
    ```
    with `from` and `to` referring to a timeframe within which all returned
    reservations must be.


&nbsp;

---
## 4. Understand DAO Queries

Read code in `JDBC_QueryRunner.java` and answer question:

1. Why does `JDBC_QueryRunner.java` execute although method
    `runQueries()` is called nowhere?

1. Class `JDBC_QueryRunner.java` uses a local variable `dao` to reference
    the "DataAccess-Object" (DAO) that implements the interface.
    ```java
    @Autowired
    private DataAccess dao;
    ```
    How many objects exist for the interface and who creates them?

    Who initializes this variable? What is the effect of `@Autowired`?

1. Understand queries in the `runQueries()` method and which output they
    log to the console, e.g.:
    ```java
    var ids = List.of(23L, 48L, 9600L, 92L);
    logger.info(String.format("dao.findAllCustomersById(%s):", ids));
    //
    dao.findAllCustomersById(ids)
        .forEach(c -> {
            logger.info(String.format(" - found: %d, %s", c.getId(), c.getName()));
        });
    ```
    Output logged to the console:
    ```perl
    dao.findAllCustomersById([23, 48, 9600, 92]):
     - found: 23, Kohl, Gero
     - found: 48, Wimmer, Kathleen          # Customer id: 9600 does not exist
     - found: 92, Kärner, Klaus-Jürgen
    ```
    Run the query for two more customers: *"Urban, Cordula"* and *"Geiger, Valerie"*.

1. Which data type does `findAllCustomersById(ids)` return?


&nbsp;

---
## 5. Understand JDBC Code

[DataAccessImpl.java](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/java/de/freerider/data_jdbc/DataAccessImpl.java)
contains the code that implements the DAO interface methods with *"JDBC"-Code*,
which consists of five main parts:

1. opening a connection to the database providing a *"connection object"* through
    which actual database operations are performed.
    Spring creates a bean of type
    [JdbcTemplate.java](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html)
    for this.

1. Running SQL Operations in the database with:

    a.) "prepare" SQL statements with data derived from
    [PreparedStatement.java](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/PreparedStatement.html)
    , which is injecting values into the SQL Query such as a customer_id.
    Placeholders `"?"` are used to indicate where values are injected, e.g.:
    ```perl
    WHERE CUSTOMER.ID = ?   # <-- replace "?" with actual ID value
    ```

    b.) send prepared statements to the database for execution and wait for the result.

    c.) Wait for an answer from the database, which is returned of type
    [ResultSet.java](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/ResultSet.html).

    - A `ResultSet` corresponds to a table with `rows` and indexed `fields`
    within `rows`. ResultSet tables can differ from database tables and include
    computed fields (SQL functions) or fields combined from tables (joins).

    - Data can be extracted from a `ResultSet` and, for instance, Customer - objects
    created.

1. close the database connection.

Answer questions:

1. What does the `@Component` annotation mean? Why is it needed for the implementation class?
    ```java
    @Component
    class DataAccessImpl implements DataAccess {
        ...
    }
    ```

1. Explain the code in
    [DataAccessImpl.java](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/java/de/freerider/data_jdbc/DataAccessImpl.java).

    - Where is the SQL statement "prepared"? What happens?

    - Where is the ResultSet "received"? In which form?

    - Where are values extracted from the ResultSet?

    - What is done with values extracted from the ResultSet?


    ```java
    /**
     * Run query that returns all Customers with matching id in ids.
     * - query: SELECT * FROM CUSTOMER WHERE ID IN (10, 20, 30000, 40);
     * - returns Customer objects created from ResultSet rows.
     * 
     * @param ids Customer ids (WHERE IN (?ids))
     * @return Customers with matching ids.
     */
    @Override
    public Iterable<Customer> findAllCustomersById(Iterable<Long> ids) {

        /*
         * Map ids (23, 48, 96) to idsStr: "23, 48, 96"
         */
        String idsStr = StreamSupport.stream(ids.spliterator(), false)
            .map(id -> String.valueOf(id))
            .collect(Collectors.joining(", "));
        //
        var result = jdbcTemplate.queryForStream(
            /*
             * Prepare statement (ps) with "?"-augmented SQL query.
             */
            String.format("SELECT * FROM CUSTOMER WHERE ID IN (%s)", idsStr),

            /*
             * Extract values from ResultSet for each row.
             */
            (rs, rowNum) -> {
                long id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String contact = rs.getString("CONTACT");
                String status = rs.getString("STATUS");
                /*
                 * Create Optional<Customer> from values.
                 */
                return dataFactory.createCustomer(id, name, contact, status);
            }
        )
        /*
         * Remove empty results from stream of Optional<Customer>,
         * map remaining from Optional<Customer> to Customer and
         * collect result.
         */
        .filter(opt -> opt.isPresent())
        .map(opt -> opt.get())
        .collect(Collectors.toList());
        //
        return result;
    }
    ```

1. Explain the final stream operations and what the type is of `result`?

    ```java
    public Iterable<Customer> findAllCustomersById(Iterable<Long> ids) {
        var result = jdbcTemplate.queryForStream( ... )
            .filter(opt -> opt.isPresent())
            .map(opt -> opt.get())
            .collect(Collectors.toList());
        //
        return result;
    }
    ```


&nbsp;

---
## 6. Complete the Vehicle data model class

Output for Vehicles in the four Reservations found for Customer `id: 2` is still
`<UNKNOWN>`. Reason is that datamodel class `Vehicle.java` is not complete.

```
dao.findReservationsByCustomerId(2):
Executing prepared SQL statement [SELECT RESERVATION.* FROM CUSTOMER JOIN RESERV
ATION ON RESERVATION.CUSTOMER_ID = CUSTOMER.ID WHERE CUSTOMER.ID = ?]
 - RES: 145373, [2022-12-04 20:00:00 - 2022-12-04 23:00:00], Inquired, vehicle:
<UNKNOWN>
 - RES: 351682, [2023-01-07 18:03:26 - 2023-01-07 20:03:26], Inquired, vehicle:
<UNKNOWN>
 - RES: 382565, [2022-12-18 18:00:00 - 2022-12-18 18:10:00], Inquired, vehicle:
<UNKNOWN>
 - RES: 682351, [2022-12-18 10:00:00 - 2022-12-18 16:00:00], Inquired, vehicle:
<UNKNOWN>
```

Inspect the other (complete) datamodel classes (Customer, Reservation) and understand
how attributes relate to the structure of corresponding tables.

Understand in particular how data types:

- DATETIME for begin and end of a reservation (in `Reservation.java`) and

- and `enum`, e.g. for `enum Status`,

are mapped between SQL types and Java.

Also refer to the `datamodel` factory interface
[DataFactory.java](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/java/de/freerider/datamodel/DataFactory.java) and implementation
[DataFactoryImpl.java](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/java/de/freerider/datamodel/DataFactoryImpl.java)
that is used to create Customer, Vehicle and Reservation objects for `ResultSet`
processing (see
[DataAccessImpl.java](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/java/de/freerider/data_jdbc/DataAccessImpl.java)
) rather than using Constructors.

Compare types passed in the `createVehicle(....)` - method(), particularly for the
`enum` types `Category`, `Power` and `Status`:

```java
/**
 * Public interface of a factory that creates objects for datamodel classes <T>.
 * 
 * Factory create() methods return type Optional<T> that only contain objects
 * of type T when valid objects could be created from valid parameters.
 * Otherwise, the returned Optional is empty.
 * 
 * @author sgra64
 */
public interface DataFactory {

    Optional<Customer> createCustomer(long id, String name, String contacts, String status);

    /**
     * Create new Vehicle object from parameters.
     * 
     * @param id unique identifier, PRIMARY KEY in database.
     * @param make brand name of Vehicle, e.g. "VW" or "Tesla"
     * @param model model name of Vehicle, e.g. "ID.4"
     * @param seats number of seats in Vehicle.
     * @param category category of Vehicle (String must match Vehicle.Category enum).
     * @param power power source of Vehicle (String must match Vehicle.Power enum).
     * @param status status of Vehicle (String must match Vehicle.Status enum).
     * @return Optional with object or empty when no object could be created from parameters.
     */
    Optional<Vehicle> createVehicle(
        long id, String make, String model, int seats,
        String category, String power, String status
    );

    Optional<Reservation> createReservation(
        long id, long customer_id, long vehicle_id,
        String begin, String end, String pickup, String dropoff, String status
    );
}
```


&nbsp;

---
## 7. Implement the DataAccessVehicles DAO interface

After completing class `Vehicle.java`, the DAO interface
[DataAccessVehicles.java](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/java/de/freerider/data_jdbc/DataAccessVehicles.java)
must be completed to run specified queries. Use
[DataAccessImpl.java](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/java/de/freerider/data_jdbc/DataAccessImpl.java) as reference.

Test code in
[JDBC_QueryRunner.java](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/java/de/freerider/data_jdbc/JDBC_QueryRunner.java)
:

```java
id = 2L;
logger.info(String.format("dao.findReservationsByCustomerId(%d):", id));
//
dao.findReservationsByCustomerId(id)
    .forEach(r -> {
        String begin = Reservation.dateTimeToStr(r.getBegin());
        String end = Reservation.dateTimeToStr(r.getEnd());
        String vehicle = "<UNKNOWN>";
        if(vehicle_dao != null) {

            /*
             * completed vehicle_dao query for Vehicles:
             */
            vehicle = vehicle_dao.findVehicleById(r.getVehicleId())
                .map(v -> {
                    String veh = String.format("%s %s", v.getMake(), v.getModel());
                    return String.format("[id: %d, %-20s]", v.getId(), veh);
                })
                .orElse(vehicle);
        }
        logger.info(String.format(" - RES: %d, [%s - %s], %s, vehicle: %s",
            r.getId(), begin, end, r.getStatus(), vehicle));
    });
```

After disabling SQL logging in
[application.yaml](https://github.com/sgra64/se2-freerider/blob/jdbc/src/main/resources/application.yaml)

```yaml
'[org.springframework.jdbc.core]': OFF  # log SQL statements -> to OFF
```

output shows vehicles for all reservations of Customer `id: 2L`:

```sh
dao.findReservationsByCustomerId(2):
 - RES: 145373, [2022-12-04 20:00:00 - 2022-12-04 23:00:00], Inquired, vehicle:
[id: 1009, VW ID.3             ]    # <-- vehicle of RES: 145373

 - RES: 351682, [2023-01-07 18:03:26 - 2023-01-07 20:03:26], Inquired, vehicle:
[id: 8000, EMCO Novi           ]    # <-- vehicle of RES: 351682

 - RES: 382565, [2022-12-18 18:00:00 - 2022-12-18 18:10:00], Inquired, vehicle:
[id: 3000, Tesla Model 3       ]    # <-- vehicle of RES: 382565

 - RES: 682351, [2022-12-18 10:00:00 - 2022-12-18 16:00:00], Inquired, vehicle:
[id: 8001, EMCO Novi           ]    # <-- vehicle of RES: 682351
```

