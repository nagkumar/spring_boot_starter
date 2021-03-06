package org.bps.metrics.dbcount;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.repository.CrudRepository;

public class DBCountHealthIndicator implements HealthIndicator
{
    private CrudRepository crudRepository;

    DBCountHealthIndicator(final CrudRepository aCrudRepository)
    {
	crudRepository = aCrudRepository;
    }

    @Override
    public Health health()
    {
	try
	{
	    long count = crudRepository.count();
	    if (count >= 0)
	    {
		return Health.up().withDetail("count", count).build();
	    }
	    return Health.unknown().withDetail("count", count).build();
	}
	catch (final Exception aException)
	{
	    return Health.down(aException).build();
	}
    }
}
