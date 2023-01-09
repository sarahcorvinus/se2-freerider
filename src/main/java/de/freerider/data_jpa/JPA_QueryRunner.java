package de.freerider.data_jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Non-public query runner class to access {@code @Entity} TABLES in database.
 * 
 * @author sgra64
 */
@Component
final class JPA_QueryRunner {

    /*
     * Logger instance for this class.
     */
    private static final Logger logger =
        LoggerFactory.getLogger(JPA_QueryRunner.class);

    /**
     * Repository that implements access methods for objects of the
     * Customer entity class in the database.
     */
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Repository that implements access methods for objects of the
     * Vehicle entity class in the database.
     */
    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Repository that implements access methods for objects of the
     * Reservation entity class in the database.
     */
    @Autowired
    private ReservationRepository reservationRepository;


    /**
     * Method is called by Spring Container after container is ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runQueries() {
        //
        logger.info("--> JPA QueryRunner");

        var customersCount = customerRepository.count();
        System.out.println(String.format("CustomerRepository.count(): %d objects found", customersCount));

        customerRepository.findById(120L)
            .ifPresent(c -> System.out.println("Customer 120 found: " + c.getName() + ", " + c.getId()));

        customerRepository.findByName("Meier, Diethard")
            .forEach(c -> {
                System.out.println(String.format("Customer.findByName(): %d, %s", c.getId(), c.getName()));
            });

        customerRepository.findByNameStartingWith("Ba")
            .forEach(c -> {
                System.out.println(String.format("Customer.findByNameStartingWith(): %d, %s", c.getId(), c.getName()));
            });

        customerRepository.findByNameMatch("%, Stefan%")   // "Me%"
            .forEach(c -> {
                System.out.println(String.format("Customer.findByNameMatch(): %d, %s", c.getId(), c.getName()));
            });

        var vehiclesCount = vehicleRepository.count();
        System.out.println(String.format("VehicleRepository.count(): %d objects found", vehiclesCount));

        vehicleRepository.findAllById(List.of(1018L, 300L, 3010L, 4060L))
            .forEach(v -> {
                System.out.println(String.format("Vehicle.findAllById(): %d, %s, %s, %s, %s", v.getId(), v.getMake(), v.getMake(), v.getPower(), v.getStatus()));
            });

        var reservationsCount = reservationRepository.count();
        System.out.println(String.format("ReservationRepository.count(): %d objects found", reservationsCount));

        // reservationRepository.findAllById(List.of(145373L, 351682L, 682351L))
        //     .forEach(r -> {
        //         System.out.println(String.format("Reservation.findAllById(): %d, %d, %d", r.getId(), r.getCustomerId(), r.getVehicleId()));
        //     });
    }
}
