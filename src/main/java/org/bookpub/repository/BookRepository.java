package org.bookpub.repository;

import org.bookpub.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>
{
    Book findBookByIsbn(final String aISBN);
}
