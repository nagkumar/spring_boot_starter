package org.bookpub.test.spec

import org.bookpub.BookPubApplication
import org.bookpub.entity.Author
import org.bookpub.entity.Book
import org.bookpub.entity.Publisher
import org.bookpub.repository.BookRepository
import org.bookpub.repository.PublisherRepository
import org.bookpub.test.unit.TestMockBeansConfig
import org.hamcrest.CoreMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.ConfigurableWebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

@WebAppConfiguration
@ContextConfiguration(classes = [BookPubApplication.class, TestMockBeansConfig], loader = SpringBootContextLoader.class)
class SpockBookRepositorySpec extends Specification
{
  @Autowired
  ConfigurableWebApplicationContext context

  @Shared
  boolean sharedSetupDone = false

  @Autowired
  private DataSource dataSource

  @Autowired
  private BookRepository bookRepository

  @Shared
  private MockMvc mockMvc

  void setup()
  {
    if (!sharedSetupDone)
    {
      sharedSetupDone = mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }
    ResourceDatabasePopulator popular = new ResourceDatabasePopulator(context.getResource("classpath:/test-data.sql"))
    DatabasePopulatorUtils.execute(popular, dataSource)
  }

  @Transactional
  def "Test RESTful GET"()
  {
    when:
    def result = mockMvc.perform(MockMvcRequestBuilders.get("/books/${isbn}"))

    then:
    result.with {
      andExpect(MockMvcResultMatchers.status().isOk())
      andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString(title)))
    }

    where:
    isbn                | title
    "978-1-78439-302-1" | "Learning Spring Boot"
    "972-1-78528-415-1" | "Spring Boot Recipes"
  }

  @Transactional
  def "Insert another book"()
  {
    setup:
    def existingBook = bookRepository.findBookByIsbn("972-1-78528-415-1")
    def newBook = new Book("978-1-78528-415-1", "Some Future Book",
                           existingBook.getAuthor(), existingBook.getPublisher())

    expect:
    bookRepository.count() == 2

    when:
    def savedBook = bookRepository.save(newBook)

    then:
    bookRepository.count() == 3
    savedBook.id > -1
  }

  @Autowired
  private PublisherRepository publisherRepository

  def "Test RESTful GET books by publisher"()
  {
    setup:
    Publisher publisher = new Publisher("Strange Books")
    publisher.setId(999)
    Book book = new Book("978-1-98765-432-1", "Mystery Book",
                         new Author("John", "Doe"), publisher)
    publisher.setBooks([book])
    Mockito.when(publisherRepository.count()).thenReturn(1L)
    Mockito.when(publisherRepository.findOne(1L)).thenReturn(publisher)

    when:
    def result = mockMvc.perform(MockMvcRequestBuilders.get("/books/publisher/1"))

    then:
    result.with {
      andExpect(MockMvcResultMatchers.status().isOk())
      andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("Strange Books")))
    }

    cleanup:
    Mockito.reset(publisherRepository)
  }
}