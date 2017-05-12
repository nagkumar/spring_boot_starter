package org.bookpub;

import org.apache.catalina.connector.Connector;
import org.bookpub.formatters.BookFormatter;
import org.bookpub.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
@PropertySource("classpath:/tomcat.https.properties")
@EnableConfigurationProperties(WebConfiguration.TomcatSslConnectorProperties.class)
public class WebConfiguration extends WebMvcConfigurerAdapter
{
    private final BookRepository bookRepository;

    @Autowired
    public WebConfiguration(final BookRepository aBookRepository)
    {
        bookRepository = aBookRepository;
    }

    @Override
    public void addFormatters(final FormatterRegistry aFormatterRegistry)
    {
        aFormatterRegistry.addFormatter(new BookFormatter(bookRepository));
    }

    @Override
    public void configurePathMatch(final PathMatchConfigurer aPathMatchConfigurer)
    {
        aPathMatchConfigurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(true);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry aResourceHandlerRegistry)
    {
        aResourceHandlerRegistry.addResourceHandler("/internal/**").addResourceLocations("classpath:/");
    }

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer()
    {
        return new EmbeddedServletContainerCustomizer()
        {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container)
            {
                container.setSessionTimeout(1, TimeUnit.MINUTES);
            }
        };
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory(final TomcatSslConnectorProperties aTomcatSslConnectorProperties)
    {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(createSslConnector(aTomcatSslConnectorProperties));
        return tomcat;
    }

    private Connector createSslConnector(final TomcatSslConnectorProperties aTomcatSslConnectorProperties)
    {
        Connector connector = new Connector();
        aTomcatSslConnectorProperties.configureConnector(connector);
        return connector;
    }

    @ConfigurationProperties(prefix = "custom.tomcat.https")
    public static class TomcatSslConnectorProperties
    {
        private Integer port;
        private Boolean ssl = true;
        private Boolean secure = true;
        private String scheme = "https";
        private File keystore;
        private String keystorePassword;

        public Integer getPort()
        {
            return port;
        }

        public void setPort(final Integer aPort)
        {
            port = aPort;
        }

        public Boolean getSsl()
        {
            return ssl;
        }

        public void setSsl(final Boolean aSSL)
        {
            ssl = aSSL;
        }

        public Boolean getSecure()
        {
            return secure;
        }

        public void setSecure(final Boolean aSecure)
        {
            secure = aSecure;
        }

        public String getScheme()
        {
            return scheme;
        }

        public void setScheme(final String aScheme)
        {
            scheme = aScheme;
        }

        public File getKeystore()
        {
            return keystore;
        }

        public void setKeystore(final File aKeystore)
        {
            keystore = aKeystore;
        }

        public String getKeystorePassword()
        {
            return keystorePassword;
        }

        public void setKeystorePassword(final String aKeystorePassword)
        {
            keystorePassword = aKeystorePassword;
        }

        void configureConnector(final Connector aConnector)
        {
            if (port != null) aConnector.setPort(port);
            if (secure != null) aConnector.setSecure(secure);
            if (scheme != null) aConnector.setScheme(scheme);
            if (ssl != null) aConnector.setProperty("SSLEnable", ssl.toString());
            if (keystore != null && keystore.exists())
            {
                aConnector.setProperty("keystoreFile", keystore.getAbsolutePath());
                aConnector.setProperty("keystorePassword", keystorePassword);
            }
        }
    }
}
