package org.bookpub.formatters;

import org.bookpub.entity.Book;
import org.bookpub.repository.BookRepository;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class BookFormatter implements Formatter<Book>
{
    private BookRepository bookRepository;

    public BookFormatter(final BookRepository aBookRepository)
    {
	bookRepository = aBookRepository;
    }

    @Override
    public Book parse(String bookIdentifier, Locale locale) throws ParseException
    {
	Book book = bookRepository.findBookByIsbn(bookIdentifier);
	return book != null ? book : bookRepository.findOne(Long.valueOf(bookIdentifier));
    }

    @Override
    public String print(final Book aBook, final Locale aLocale)
    {
	return aBook.getIsbn();
    }
}
