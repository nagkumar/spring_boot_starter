package org.bps.metrics.dbcount;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public class DBCountRunner implements CommandLineRunner
{
    private final Log logger = LogFactory.getLog(getClass());
    private Collection<CrudRepository> crudRepositories;

    DBCountRunner(final Collection<CrudRepository> aCrudRepositories)
    {
        crudRepositories = aCrudRepositories;
    }

    static String getRepositoryName(final Class aCrudRepositoryClass)
    {
        for (Class repositoryInterface : aCrudRepositoryClass.getInterfaces())
        {
            if (repositoryInterface.getName().startsWith("org.bookpub.repository"))
            {
                return repositoryInterface.getSimpleName();
            }
        }
        return "UnknownRepository";
    }

    @Override
    public void run(final String... aArgs) throws Exception
    {
        crudRepositories.forEach(crudRepository ->
                logger.info(String.format("%s has %s entries.",
                        getRepositoryName(crudRepository.getClass()),
                        crudRepository.count())));
    }
}
