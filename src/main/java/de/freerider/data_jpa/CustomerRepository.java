package de.freerider.data_jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.freerider.datamodel.Customer;


/**
 * Customer Repository interface that inherits methods from CrudRepository<T, ID>.
 * 
 * Methods added here are 'auto-generated' by Spring and require no separate
 * implementations.
 * 
 * 
 * @author sgra64
 */
public interface CustomerRepository extends CrudRepository<Customer, Long>, CustomerRepositoryExtender {


    /**
     * Find Customers by name attribute.
     * 
     * @param name Customer name.
     * @return Collection with Customer object or empty.
     */
    Iterable<Customer> findByName(String name);


    /**
     * Execute JPQ Query to find Customers by a name matcher that uses
     * a SQL match expressions, see:
     *  - https://www.baeldung.com/spring-data-jpa-query
     * 
     * For example, expression "Me%" matches names starting with "Me...".
     * "%, Stefan%" matches first names after "," containing "Stefan"
     * including "Stefanie".
     * 
     * @param nameMatcher SQL match-expression for Customer name.
     * @return Collection with matching Customer objects (or empty).
     */
    // @Query("SELECT c FROM Customer c WHERE c.name = ?1")
    @Query("SELECT c FROM Customer c WHERE c.name LIKE ?1")
    Iterable<Customer> findByNameMatch(String nameMatcher);

}
