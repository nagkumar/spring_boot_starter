package org.bookpub.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.bookpub.entity.Reviewer;

@RepositoryRestResource
public interface ReviewerRepository extends PagingAndSortingRepository<Reviewer, Long>
{
}
