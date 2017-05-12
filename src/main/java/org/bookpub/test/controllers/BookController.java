package org.bookpub.test.controllers;

import org.bookpub.test.entity.Book;
import org.bookpub.test.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.bookpub.test.entity.Publisher;
import org.bookpub.test.entity.Reviewer;
import org.bookpub.test.repository.BookRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController
{
    private final BookRepository bookRepository;

    private final PublisherRepository publisherRepository;

    @Autowired
    public BookController(BookRepository bookRepository, PublisherRepository publisherRepository)
    {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Book> getAllBooks()
    {
        return bookRepository.findAll();
    }

    @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
    public Book getBook(@PathVariable String isbn)
    {
        return bookRepository.findBookByIsbn(isbn);
    }

    @RequestMapping(value = "/{isbn}/reviewers", method = RequestMethod.GET)
    public List<Reviewer> getReviewers(@PathVariable("isbn") Book book)
    {
        return book.getReviewers();
    }

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public String getSessionId(HttpServletRequest request)
    {
        return request.getSession().getId();
    }

    @RequestMapping(value = "/publisher/{id}", method = RequestMethod.GET)
    public List<Book> getBooksByPublisher(@PathVariable("id") Long id)
    {
        Publisher publisher = publisherRepository.findOne(id);
        Assert.notNull(publisher,"The publisher must not be null");
        return publisher.getBooks();
    }
}
