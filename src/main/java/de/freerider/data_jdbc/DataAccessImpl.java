package de.freerider.data_jdbc;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.freerider.datamodel.Customer;
import de.freerider.datamodel.DataFactory;
import de.freerider.datamodel.Reservation;
import de.freerider.datamodel.Vehicle;


@Component
class DataAccessImpl implements DataAccess {

    /*
     * Logger instance for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(DataAccessImpl.class);

    /**
     * Datafactory is a component that creates datamodel objects.
     */
    @Autowired
    private DataFactory dataFactory;

    /*
     * JdbcTemplate is the central class in the JDBC core package for SQL
     * database access.
     * - https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html
     * 
     * Examples:
     * - https://mkyong.com/spring-boot/spring-boot-jdbc-examples
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Iterable<Customer> findAllCustomers() {
        //
        var result = jdbcTemplate.queryForStream(
            //
            "SELECT * FROM CUSTOMER",
            /*
             * map SQL Result Set (RS) with values for each row to object of type T
             */
            (rs, rowNum) -> {
                long id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String contact = rs.getString("CONTACT");
                String status = rs.getString("STATUS");
                //
                return dataFactory.createCustomer(id, name, contact, status);
            }
        )
        // queryForStream() returns stream of Optional<Customer>, which
        // must be filtered for empty ones and mapped to objects.
        .filter(opt -> opt.isPresent())
        .map(opt -> opt.get())
        .collect(Collectors.toList());
        //
        return result;
    }


    @Override
    public Optional<Customer> findCustomerById(long id) {
        //
        List<Optional<Customer>> result = jdbcTemplate.query(
            "SELECT * FROM CUSTOMER WHERE ID = ?",
            ps -> {    // ps: prepare statement to repace '?' in SQL String
                ps.setInt(1, (int)id);
            },
            // old style:
            // new RowMapper<Optional<Customer>>() {
            //     /**
            //      * Function to map SQL Result Set (RS) with values for each row to
            //      * object of type T.
            //      * 
            //      * @param rs result set.
            //      * @param rowNum row number.
            //      * @return object of type T.
            //      * @throws SQLException
            //      */
            //     public Optional<Customer> mapRow(ResultSet rs, int rowNum) throws SQLException {
            //         String name = rs.getString("NAME");
            //         String contact = rs.getString("CONTACT");
            //         String status = rs.getString("STATUS");
            //         return dataFactory.createCustomer(id, name, contact, status);
            //     }
            // }

            /*
             * replaced with functional style: map SQL Result Set (RS) with
             * values for each row to object of type T
             */
            (rs, rowNum) -> {   // rs: result set, rowNum: row number
                String name = rs.getString("NAME");
                String contact = rs.getString("CONTACT");
                String status = rs.getString("STATUS");
                //
                return dataFactory.createCustomer(id, name, contact, status);
            }
        );
        return result.size() > 0? result.get(0) : Optional.empty();
    }


    @Override
    public Iterable<Customer> findAllCustomerById(Iterable<Long> ids) {
        //
        // map ids (23, 48, 96) to idsStr: "23, 48, 96"
        String idsStr = StreamSupport.stream(ids.spliterator(), false)
            .map(id -> String.valueOf(id))
            .collect(Collectors.joining(", "));
        //
        var result = jdbcTemplate.queryForStream(
            //
            String.format("SELECT * FROM CUSTOMER WHERE ID IN (%s)", idsStr),
            /*
             * map SQL Result Set (RS) with values for each row to object of type T
             */
            (rs, rowNum) -> {
                long id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String contact = rs.getString("CONTACT");
                String status = rs.getString("STATUS");
                //
                return dataFactory.createCustomer(id, name, contact, status);
            }
        )
        // queryForStream() returns stream of Optional<Customer>, which
        // must be filtered for empty ones and mapped to objects.
        .filter(opt -> opt.isPresent())
        .map(opt -> opt.get())
        .collect(Collectors.toList());
        //
        return result;
    }


    @Override
    public Optional<Vehicle> findVehicleById(long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }


    @Override
    public Optional<Reservation> findReservationById(long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }


    private final String SQL_findReservationsByCustomerId =
        "SELECT RESERVATION.* FROM CUSTOMER " +
        "JOIN RESERVATION ON RESERVATION.CUSTOMER_ID = CUSTOMER.ID " +
        "WHERE CUSTOMER.ID = ?";

    @Override
    public Iterable<Long> findReservationIdsByCustomerId(long customer_id) {
        //
        Iterable<Long> result = jdbcTemplate.query(
            SQL_findReservationsByCustomerId,
            ps -> {    // ps: prepare statement to repace '?' in SQL String
                ps.setInt(1, (int)customer_id);
            },
            (rs, rowNum) -> {   // rs: result set
                long rid = rs.getInt("ID");   // RESERVATION.ID
                return rid;
            }
        );
        return result;
    }


    @Override
    public Iterable<Reservation> findReservationsByCustomerId(long customer_id) {
        //
        return jdbcTemplate.queryForStream(
            SQL_findReservationsByCustomerId,
            ps -> {    // ps: prepare statement to repace '?' in SQL String
                ps.setInt(1, (int)customer_id);
            },
            (rs, rowNum) -> {   // rs: result set mapped to Reservation object
                long rid = rs.getInt("ID");   // RESERVATION.ID
                long vehicle_id = rs.getInt("VEHICLE_ID");
                String begin = rs.getString("BEGIN");
                String end = rs.getString("END");
                String pickup = rs.getString("PICKUP");
                String dropoff = rs.getString("DROPOFF");
                String status = rs.getString("STATUS");
                //
                // return Optional<Reservation> when no Reservation object
                // could be created from result set
                var opt = dataFactory.createReservation(
                    rid, customer_id, vehicle_id, begin, end, pickup, dropoff, status
                );
                if(opt.isEmpty()) {
                    // log warning when no valid Reservation object could be created
                    // from database result set
                    logger.warn(String.format("dropping reservation id: %d"));
                }
                return opt;
            })
            // queryForStream() returns stream of Optional<Reservation>, which
            // must be filtered for empty ones and mapped to objects.
            .filter(opt -> opt.isPresent())
            .map(opt -> opt.get())
            .collect(Collectors.toList());
    }


    @Override
    public <T> int count(Iterable<T> iter) {
        return iter instanceof Collection? ((Collection<?>) iter).size() : -1;
    }
}
