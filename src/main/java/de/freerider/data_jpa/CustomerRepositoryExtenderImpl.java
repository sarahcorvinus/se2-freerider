package de.freerider.data_jpa;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import de.freerider.datamodel.Customer;


/**
 * Non-public implementation class of the Customer Repository extension interface
 * with methods declared there.
 * 
 * 
 * @author sgra64
 */
final class CustomerRepositoryExtenderImpl implements CustomerRepositoryExtender {

    /**
     * Auto-wired reference to CustomerRepository.
     */
    @Autowired
    @Lazy           // Lazy mitigates circular dependency error for autowiring
    private CustomerRepository customerRepository;

    /*
     * JPA also offer access to the database through EntityManager.
     *  
     * See examples in:
     * https://stackoverflow.com/questions/11880924/how-to-add-custom-method-to-spring-data-jpa
     */
    @PersistenceContext
    private EntityManager em;


    /**
     * Find Customers by name attribute starting with matcher.
     * 
     * @param lastNameStartsWith matcher for Customer name.
     * @return Collection with matching Customer objects (or empty).
     */
    @Override
    public Iterable<Customer> findByNameStartingWith(String lastNameStartsWith) {
        return StreamSupport.stream(
            customerRepository.findAll().spliterator(), false)
                .filter(c -> c.getName().startsWith(lastNameStartsWith))
                .collect(Collectors.toList());
        // return new ArrayList<Customer>();
    }
}
