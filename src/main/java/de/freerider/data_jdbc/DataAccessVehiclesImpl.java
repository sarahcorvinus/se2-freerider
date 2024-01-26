package de.freerider.data_jdbc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import de.freerider.datamodel.DataFactory;
import de.freerider.datamodel.Vehicle;

/**
 * DataAccessVehiclesImpl
 */
@Component
class DataAccessVehiclesImpl implements DataAccessVehicles {
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
    public long countVehicles() {
        List<Object> result = jdbcTemplate.query(
            /*
             * Run SQL statement:
             */
            "SELECT COUNT(ID) FROM VEHICLE",

            /*
             * Return ResultSet (rs) and extract COUNT value.
             */
            (rs, rowNum) -> {
                long count = rs.getInt(1);  // index[1]
                return count;
            }
        );
        //
        return result.size() > 0? (long)(result.get(0)) : 0;
    }

    @Override
    public Iterable<Vehicle> findAllVehicles() {
            //
        var result = jdbcTemplate.queryForStream(
            /*
             * Run SQL statement:
             */
            "SELECT * FROM VEHICLE",

            /*
             * Return ResultSet (rs) and map each row to Optional<VEHICLE>
             * depending on whether the object could be created from values
             * returned from the database or not (empty Optional is returned).
             */
            (rs, rowNum) -> {
                /*
                 * Extract values from ResultSet for each row.
                 */
                long id = rs.getInt("ID");
                String make = rs.getString("MAKE");
                String model = rs.getString("MODEL");
                int seats = rs.getInt("SEATS");
                String category = rs.getString("CATEGORY");
                String power = rs.getString("POWER");
                String status = rs.getString("STATUS");

                /*
                 * Attempt to create Vehicle object through dataFactory,
                 * which returns Optional<Vehicle>.
                 */
                return dataFactory.createVehicle(id, make, model, seats, category, power, status);
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

    @Override
    public Optional<Vehicle> findVehicleById(long id) {
        System.out.println("+++++" + id);
        List<Optional<Vehicle>> result = jdbcTemplate.query(
            /*
             * Prepare statement (ps) with "?"-augmented SQL query.
             */
            "SELECT * FROM VEHICLE WHERE ID = ?",
            ps -> {
                /*
                 * Insert id value of first occurence of "?" in SQL.
                 */
                ps.setInt(1, (int)id);
            },

            (rs, rowNum) -> {
                /*
                 * Extract values from ResultSet.
                 */
                long idR = rs.getInt("ID");
                String make = rs.getString("MAKE");
                String model = rs.getString("MODEL");
                int seats = rs.getInt("SEATS");
                String category = rs.getString("CATEGORY");
                String power = rs.getString("POWER");
                String status = rs.getString("STATUS");
                /*
                 * Create Optional<Vehicle> from values.
                 */
                System.out.println("*******" + idR);
                return dataFactory.createVehicle(idR, make, model, seats, category, power, status);
            }
        );
        /*
         * Probe List<Optional<Vehicle>> and return Optional<Vehicle> or
         * empty Optional for empty list.
         */
        return result.size() > 0? result.get(0) : Optional.empty();
    }

    @Override
    public Iterable<Vehicle> findAllVehiclesById(Iterable<Long> ids) {
        
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
            String.format("SELECT * FROM VEHICLE WHERE ID IN (%s)", idsStr),

            /*
             * Extract values from ResultSet for each row.
             */
            (rs, rowNum) -> {
                long id = rs.getInt("ID");
                String make = rs.getString("MAKE");
                String model = rs.getString("MODEL");
                int seats = rs.getInt("SEATS");
                String category = rs.getString("CATEGORY");
                String power = rs.getString("POWER");
                String status = rs.getString("STATUS");
                /*
                 * Create Optional<Vehicle> from values.
                 */
                return dataFactory.createVehicle(id, make, model, seats, category, power, status);
            }
        )
        /*
         * Remove empty results from stream of Optional<Vehicle>,
         * map remaining from Optional<Vehicle> to Vehicle and
         * collect result.
         */
        .filter(opt -> opt.isPresent())
        .map(opt -> opt.get())
        .collect(Collectors.toList());
        //
        return result;
    }
    
}