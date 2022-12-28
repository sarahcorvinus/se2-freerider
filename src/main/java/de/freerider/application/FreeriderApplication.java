package de.freerider.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

import org.springframework.boot.context.event.ApplicationReadyEvent;


/**
 * Application class with main() method. Starts the Spring Container.
 * 
 * To run this class:
 * <pre>
 *  - mvn package
 *  - java -jar target/freerider-application-0.0.1-SNAPSHOT.jar
 * </pre>
 * 
 * @author sgra64
 *
 */
@SpringBootApplication
public class FreeriderApplication {

    /**
     * Local logger instance registered under class name.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(FreeriderApplication.class);

    /**
     * Non-public constructor, required by javadoc.
     */
    FreeriderApplication() {
        logger.info("(1.) FreeriderApplication instance created.");
    }

    /**
     * main() method that starts the Spring Container.
     * 
     * @param args arguments passed from command line.
     */
    public static void main(String[] args) {
        logger.info("(0.) Spring Container starting.");
        //
        // start Spring Container, wait until ready.
        SpringApplication.run(FreeriderApplication.class, args);
        //
        logger.info("(3.) Spring Container exited.");
    }


    /**
     * Method is called by Spring Container after container is ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterSpringStartup() {
        //
        logger.info("(2.) Spring Container ready.");
        //
        var msg = String.format("Hello %s!",
                FreeriderApplication.class.getSimpleName());
        //
        System.out.println(msg);
    }

}
