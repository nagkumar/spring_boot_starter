package org.test.bookpubstarter.dbcount;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.repository.CrudRepository;

public class DbCountHealthIndicator implements HealthIndicator
{
    private CrudRepository crudRepository;

    public DbCountHealthIndicator(final CrudRepository aCrudRepository)
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
