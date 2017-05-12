package org.bookpub;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bookpub.repository.AuthorRepository;
import org.bookpub.repository.BookRepository;
import org.bookpub.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;

@Order(Ordered.LOWEST_PRECEDENCE - 15)
public class StartupRunner implements CommandLineRunner
{
    protected final Log logger = LogFactory.getLog(getClass());

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private PublisherRepository publisherRepository;

    public StartupRunner()
    {
    }

    @Autowired
    public StartupRunner(final BookRepository aBookRepository,
                         final AuthorRepository aAuthorRepository,
                         final PublisherRepository aPublisherRepository)
    {
        bookRepository = aBookRepository;
        authorRepository = aAuthorRepository;
        publisherRepository = aPublisherRepository;
    }

    @Override
    public void run(final String... aArgs) throws Exception
    {
        logger.info("Welcome to Book Catalogue System!");
        /*Author author = new Author("Alex", "Antonov");
        author = authorRepository.save(author);
        Publisher publisher = new Publisher("Packt");
        publisher = publisherRepository.save(publisher);
        Book book = new Book("972-1-78528-415-1", "Spring Boot Recipies",
                author, publisher);
        bookRepository.save(book);*/
    }

    @Scheduled(initialDelayString = "${book.counter.delay}", fixedRateString = "${book.counter.rate}")
    public void run()
    {
        logger.info("Number of books: " + bookRepository.count());
    }
}
