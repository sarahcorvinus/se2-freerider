package de.freerider.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * Application configuration class.
 * 
 * @ComponentScan annotation specifies the package below which Spring
 * scans packages for annotations.
 * 
 * @author sgra64
 */
@ComponentScan(basePackages = {"de.freerider"})
@Configuration
public class AppConfig {

}
