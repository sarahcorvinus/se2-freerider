package de.freerider.data_jdbc;

import java.util.Optional;

import de.freerider.datamodel.Customer;
import de.freerider.datamodel.Reservation;
import de.freerider.datamodel.Vehicle;


public interface DataAccess {

    /*
     * from Spring Interface CrudRepository<T,ID>
     * https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
     */
    Iterable<Customer> findAllCustomers();
    Optional<Customer> findCustomerById(long id);
    Iterable<Customer> findAllCustomerById(Iterable<Long> ids);

    Optional<Vehicle> findVehicleById(long id);

    Optional<Reservation> findReservationById(long id);

    Iterable<Long> findReservationIdsByCustomerId(long customer_id);

    Iterable<Reservation> findReservationsByCustomerId(long customer_id);

    <T> int count(Iterable<T> iter);
}
