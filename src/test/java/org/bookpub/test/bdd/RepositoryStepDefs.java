package org.bookpub.test.bdd;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import org.bookpub.BookPubApplication;
import org.bookpub.entity.Book;
import org.bookpub.repository.BookRepository;
import org.bookpub.test.unit.TestMockBeansConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@WebAppConfiguration
@ContextConfiguration(classes = {BookPubApplication.class, TestMockBeansConfig.class},
	loader = SpringBootContextLoader.class)
public class RepositoryStepDefs
{
    @Autowired
    private WebApplicationContext context;
    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;
    @Autowired
    private BookRepository bookRepository;
    private Book loadedBook;

    @Given("^([^\\\"]*) fixture is loaded$")
    public void data_fixture_is_loaded(final String aFixtureName) throws Throwable
    {
	ResourceDatabasePopulator popular = new
		ResourceDatabasePopulator(context.getResource("classpath:/" + aFixtureName + ".sql"));
	DatabasePopulatorUtils.execute(popular, dataSource);
    }

    @Given("^(\\d+) books available in the catalogue$")
    public void books_available_in_the_catalogue(final int aBookCount) throws Throwable
    {
	assertEquals(aBookCount, bookRepository.count());
    }

    @When("^searching for book by isbn ([\\d-]+)$")
    public void searching_for_book_by_isbn(final String aISBN) throws Throwable
    {
	loadedBook = bookRepository.findBookByIsbn(aISBN);
	assertNotNull(loadedBook);
	assertEquals(aISBN, loadedBook.getIsbn());
    }

    @Then("^book title will be ([^\"]*)$")
    public void book_title_will_be(final String aBookTitle) throws Throwable
    {
	assertNotNull(loadedBook);
	assertEquals(aBookTitle, loadedBook.getTitle());
    }
}
