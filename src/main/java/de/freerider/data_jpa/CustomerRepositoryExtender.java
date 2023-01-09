package de.freerider.data_jpa;

import de.freerider.datamodel.Customer;


/**
 * Public Customer Repository extension interface that is added to CustomerRepository
 * interface in order to provide methods to implement in an implementation class.
 * 
 * @author sgra64
 */
public interface CustomerRepositoryExtender {

    /**
     * Find Customers by name attribute starting with matcher.
     * 
     * @param lastNameStartsWith matcher for Customer name.
     * @return Collection with matching Customer objects (or empty).
     */
    Iterable<Customer> findByNameStartingWith(String lastNameStartsWith);

}
