package org.bookpub;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class ApacheCommonsConfigurationApplicationRunListener implements SpringApplicationRunListener
{
    public ApacheCommonsConfigurationApplicationRunListener(final SpringApplication aSpringApplication,
							    final String[] aArgs)
    {
    }

    @Override
    public void starting()
    {

    }

    @Override
    public void environmentPrepared(final ConfigurableEnvironment aConfigurableEnvironment)
    {
	try
	{
	    ApacheCommonsConfigurationPropertySource.addToEnvironment(aConfigurableEnvironment,
								      new XMLConfiguration("commons-config.xml"));
	}
	catch (final ConfigurationException aConfigurationException)
	{
	    throw new RuntimeException("Unable to load commons-config.xml", aConfigurationException);
	}
    }

    @Override
    public void contextPrepared(final ConfigurableApplicationContext aConfigurableApplicationContext)
    {
    }

    @Override
    public void contextLoaded(final ConfigurableApplicationContext aConfigurableApplicationContext)
    {
    }

    @Override
    public void finished(final ConfigurableApplicationContext aConfigurableApplicationContext, final Throwable aThrowable)
    {
    }
}
