package se.kth.csc.moderndb.cbexplorer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by mhotan on 5/7/14.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"se.kth.csc.moderndb.cbexplorer.rest.controller"})
public class MVCConfig {
}
