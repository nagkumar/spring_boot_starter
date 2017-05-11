package org.bps.metrics.dbcount;

import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

@Configuration
public class DbCountAutoConfiguration
{
    @Autowired
    private HealthAggregator healthAggregator;

    @Bean
    @ConditionalOnMissingBean
    public DbCountRunner dbCountRunner(final Collection<CrudRepository> aCrudRepositories)
    {
        return new DbCountRunner(aCrudRepositories);
    }

    @Bean
    public HealthIndicator dbCountHealthIndicator(final Collection<CrudRepository> aCrudRepositories)
    {
        CompositeHealthIndicator compositeHealthIndicator = new CompositeHealthIndicator(healthAggregator);
        aCrudRepositories.forEach(r ->
                compositeHealthIndicator.addHealthIndicator(DbCountRunner.getRepositoryName(r.getClass()),
                        new DbCountHealthIndicator(r))
        );
        return compositeHealthIndicator;
    }

    @Bean
    public DbCountMetrics dbCountMetrics(final Collection<CrudRepository> aCrudRepositories,
                                         final MetricRegistry aMetricRegistry)
    {
        DbCountMetrics dbCountMetrics = new DbCountMetrics(aCrudRepositories);
        aMetricRegistry.registerAll(dbCountMetrics);
        return dbCountMetrics;
    }
}
