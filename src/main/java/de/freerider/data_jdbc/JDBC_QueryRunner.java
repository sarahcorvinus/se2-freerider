package de.freerider.data_jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import de.freerider.datamodel.Reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Non-public query runner class to execute SQL/JDBC queries in database.
 * 
 * @author sgra64
 */
@Component
class JDBC_QueryRunner {

    /*
     * Logger instance for this class.
     */
    private static final Logger logger =
        LoggerFactory.getLogger(JDBC_QueryRunner.class);

    /**
     * DataAccess (object) DAO is a component to accesses data in the
     * database through SQL queries.
     */
    @Autowired
    private DataAccess dao;

    /**
     * Vehicle DAO.
     */
    @Autowired // requires implementation class of DataAccessVehicles interface
    private DataAccessVehicles vehicle_dao;


    /**
     * Method is called by Spring Container after container is ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runQueries() {
        //
        logger.info("--> JDBC QueryRunner");
        long count = 0L;
        long id = 0L;

        count = dao.countCustomers();
        logger.info(String.format("dao.countCustomers() -> %d", count));

        count = dao.count(dao.findAllCustomers());
        logger.info(String.format("dao.findAllCustomers() -> %d Customers found", count));

        id = 24;
        String result = dao.findCustomerById(id)
            .map(c -> String.format("dao.findCustomerById(%d) -> found: %s", c.getId(), c.getName()))
            .orElse(String.format("dao.findCustomerById() -> NOT FOUND"));
        //
        logger.info(result);

        var ids = List.of(23L, 48L,  92L, 167L, 174L);
        logger.info(String.format("dao.findAllCustomersById(%s):", ids));
        //
        dao.findAllCustomersById(ids)
            .forEach(c -> {
                logger.info(String.format(" - found: %d, %s", c.getId(), c.getName()));
            });

        id = 2L;
        logger.info(String.format("dao.findReservationsByCustomerId(%d):", id));
        //
        dao.findReservationsByCustomerId(id)
            .forEach(r -> {
                String begin = Reservation.dateTimeToStr(r.getBegin());
                String end = Reservation.dateTimeToStr(r.getEnd());
                String vehicle = "<UNKNOWN>";
                if(vehicle_dao != null) {
                    vehicle = vehicle_dao.findVehicleById(r.getVehicleId())
                        .map(v -> {
                            String veh = String.format("%s %s", v.getMake(), v.getModel());
                            System.out.println("Das ist das Model" + v.getModel());
                            return String.format("[id: %d, %-20s]", v.getId(), veh);
                        })
                        .orElse(vehicle);
                }
                logger.info(String.format(" - RES: %d, [%s - %s], %s, vehicle: %s",
                    r.getId(), begin, end, r.getStatus(), vehicle));
            });
    }
}
