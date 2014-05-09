package se.kth.csc.moderndb.cbexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

/**
 * Main class that the spring framework will find and run.  Spring will automatically
 * find this class.  {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration} Is applied to this Application
 * class that is placed in the root directory of the application.  This allows for the ability to
 * scan for classes within sub directories.
 *
 * Created by mhotan on 4/8/14.
 */
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        // Print out all the bean names to be used.
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

}
