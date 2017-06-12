package org.bps.metrics.dbcount;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricSet;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

public class DBCountMetrics implements PublicMetrics, MetricSet
{
    private Collection<CrudRepository> crudRepositories;

    DBCountMetrics(final Collection<CrudRepository> aCrudRepositories)
    {
	crudRepositories = aCrudRepositories;
    }

    @Override
    public Collection<Metric<?>> metrics()
    {
	List<Metric<?>> metrics = new LinkedList<>();
	crudRepositories.forEach(repository ->
				 {
				     String bName = DBCountRunner.getRepositoryName(repository.getClass());
				     String bMetricName = "counter.datasource." + bName;
				     metrics.add(new Metric<>(bMetricName, repository.count()));
				 });
	return metrics;
    }

    @Override
    public Map<String, com.codahale.metrics.Metric> getMetrics()
    {
	final Map<String, com.codahale.metrics.Metric> gauges = new HashMap<>();
	metrics().forEach(metric ->
			  {
			      gauges.put(metric.getName(), (Gauge<Number>) metric::getValue);
			  });
	return gauges;
    }
}
