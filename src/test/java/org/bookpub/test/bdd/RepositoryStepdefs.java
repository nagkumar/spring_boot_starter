package org.bookpub.test.bdd;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.bookpub.BookPubApplication;
import org.bookpub.entity.Book;
import org.bookpub.repository.BookRepository;
import org.bookpub.test.unit.TestMockBeansConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by reddaiah on 6/12/2017.
 */
@WebAppConfiguration
@ContextConfiguration(classes = {BookPubApplication.class, TestMockBeansConfig.class},
        loader = SpringBootContextLoader.class)
public class RepositoryStepdefs {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private DataSource ds;
    @Autowired
    private BookRepository bookRepository;
    private Book loadedBook;

    @Given("^([^\\\"]*) fixture is loaded$")
    public void data_fixture_is_loaded(String fixtureName) throws
            Throwable {
        ResourceDatabasePopulator populator = new
                ResourceDatabasePopulator(context.getResource("classpath:/" +
                fixtureName + ".sql"));
        DatabasePopulatorUtils.execute(populator, ds);
    }

    @Given("^(\\d+) books available in the catalogue$")
    public void books_available_in_the_catalogue(int bookCount) throws
            Throwable {
        assertEquals(bookCount, bookRepository.count());
    }

    @When("^searching for book by isbn ([\\d-]+)$")
    public void searching_for_book_by_isbn(String isbn) throws
            Throwable {
        loadedBook = bookRepository.findBookByIsbn(isbn);
        assertNotNull(loadedBook);
        assertEquals(isbn, loadedBook.getIsbn());
    }

    @Then("^book title will be ([^\"]*)$")
    public void book_title_will_be(String bookTitle) throws Throwable {
        assertNotNull(loadedBook);
        assertEquals(bookTitle, loadedBook.getTitle());
    }
}
