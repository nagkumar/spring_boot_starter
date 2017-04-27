package org.bookpub.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.bookpub.entity.Author;

@RepositoryRestResource
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long>
{
}
