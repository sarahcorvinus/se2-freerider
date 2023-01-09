package de.freerider.data_jpa;

import org.springframework.data.repository.CrudRepository;

import de.freerider.datamodel.Reservation;


/**
 * Reservation Repository interface that inherits methods from CrudRepository<T, ID>.
 * 
 * Methods added here are 'auto-generated' by Spring and require no separate
 * implementations.
 * 
 * 
 * @author sgra64
 */
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    

}
