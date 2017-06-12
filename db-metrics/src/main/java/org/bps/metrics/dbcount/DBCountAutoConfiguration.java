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
public class DBCountAutoConfiguration
{
    private final HealthAggregator healthAggregator;

    @Autowired
    public DBCountAutoConfiguration(final HealthAggregator aHealthAggregator)
    {
	healthAggregator = aHealthAggregator;
    }

    @Bean
    @ConditionalOnMissingBean
    public DBCountRunner dbCountRunner(final Collection<CrudRepository> aCrudRepositories)
    {
	return new DBCountRunner(aCrudRepositories);
    }

    @Bean
    public HealthIndicator dbCountHealthIndicator(final Collection<CrudRepository> aCrudRepositories)
    {
	CompositeHealthIndicator compositeHealthIndicator = new CompositeHealthIndicator(healthAggregator);
	aCrudRepositories.forEach(r ->
					  compositeHealthIndicator.addHealthIndicator(
						  DBCountRunner.getRepositoryName(r.getClass()), new DBCountHealthIndicator(r))
	);
	return compositeHealthIndicator;
    }

    @Bean
    public DBCountMetrics dbCountMetrics(final Collection<CrudRepository> aCrudRepositories,
					 final MetricRegistry aMetricRegistry)
    {
	DBCountMetrics lDBCountMetrics = new DBCountMetrics(aCrudRepositories);
	aMetricRegistry.registerAll(lDBCountMetrics);
	return lDBCountMetrics;
    }
}
