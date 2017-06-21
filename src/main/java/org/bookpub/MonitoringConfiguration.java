package org.bookpub;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Configuration
public class MonitoringConfiguration
{
    @Bean
    public Graphite graphite(@Value("${graphite.host}") final String aGraphiteHost,
			     @Value("${graphite.port}") final int aGraphitePort)
    {
	return new Graphite(new InetSocketAddress(aGraphiteHost, aGraphitePort));
    }

    @Bean
    public GraphiteReporter graphiteReporter(final Graphite aGraphite, final MetricRegistry aMetricRegistry)
    {
	GraphiteReporter reporter = GraphiteReporter.forRegistry(aMetricRegistry)
		.prefixedWith("bookpub.app")
		.convertRatesTo(TimeUnit.SECONDS)
		.convertDurationsTo(TimeUnit.MILLISECONDS)
		.filter(MetricFilter.ALL)
		.build(aGraphite);
	reporter.start(1, TimeUnit.MILLISECONDS);
	return reporter;
    }

    @Bean
    public ThreadStatesGaugeSet threadStatesGaugeSet(final MetricRegistry aMetricRegistry)
    {
	ThreadStatesGaugeSet threadStatesGaugeSet = new ThreadStatesGaugeSet();
	aMetricRegistry.register("threads", threadStatesGaugeSet);
	return threadStatesGaugeSet;
    }
}
