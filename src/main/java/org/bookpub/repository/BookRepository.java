package org.bookpub.repository;

import org.bookpub.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long>
{
    Book findBookByIsbn(final String aISBN);
}
