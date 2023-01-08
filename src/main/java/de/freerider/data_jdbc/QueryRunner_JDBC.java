package de.freerider.data_jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class QueryRunner_JDBC {

    /*
     * Logger instance for this class.
     */
    private static final Logger logger =
        LoggerFactory.getLogger(QueryRunner_JDBC.class);

    /**
     * DataAccess (object) DAO is a component to accesses data in the
     * database through SQL queries.
     */
    @Autowired
    private DataAccess dao;


    /**
     * Method is called by Spring Container after container is ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runQueries() {
        //
        logger.info("JDBC QueryRunner");

        int customer_id = 2;
        // for(var rid : dao.findReservationIdsByCustomerId(customer_id)) {
        //     System.out.println("reservation: ---" + rid);
        // }
        // for(var r : dao.findReservationsByCustomerId(customer_id)) {
        //     System.out.println("reservation: ---" + r.getStatus());
        // }

        customer_id = 64;
        var customer = dao.findCustomerById(customer_id);
        if(customer.isPresent()) {
            System.out.println(String.format("Customer %d found.", customer_id));
        } else {
            System.out.println(String.format("Customer %d NOT FOUND.", customer_id));
        }

        var customers = dao.findAllCustomers();
        System.out.println(String.format("%d Customer found.", dao.count(customers)));

        dao.findAllCustomerById(List.of(23L, 48L, 96L, 92L))
            .forEach(c -> {
                System.out.println(String.format("Customer: %d, %s", c.getId(), c.getName()));
            });
    }
}
