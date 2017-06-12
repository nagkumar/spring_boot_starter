package org.bookpub;

import org.apache.commons.logging.LogFactory;
import org.bookpub.annotations.UsedForTesting;
import org.bps.metrics.dbcount.EnableDBCounting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = @ComponentScan.Filter(UsedForTesting.class))
@EnableScheduling
@EnableDBCounting
public class BookPubApplication
{
    @Bean
    @Profile("logger")
    public StartupRunner scheduleRunner()
    {
	return new StartupRunner();
    }

    @Bean
    public CommandLineRunner configValuePrinter(@Value("${my.config.value:}") String aConfigValue)
    {
	return args -> LogFactory.getLog(getClass()).info("Value of my.config.value property is: " + aConfigValue);
    }

    public static void main(final String[] aArgs)
    {
	SpringApplication.run(BookPubApplication.class, aArgs);
    }
}
