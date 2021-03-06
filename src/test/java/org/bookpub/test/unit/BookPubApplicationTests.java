package org.bookpub.test.unit;

import org.bookpub.entity.Book;
import org.bookpub.repository.BookRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookPubApplicationTests
{
    private static boolean loadDataFixtures = true;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Value("${local.server.port}")
    private int port;
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setupMockMvc()
    {
	mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Before
    public void loadDataFixtures()
    {
	if (loadDataFixtures)
	{
	    ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
		    context.getResource("classpath:/test-data.sql"));
	    DatabasePopulatorUtils.execute(populator, dataSource);
	    loadDataFixtures = false;
	}
    }

    @Test
    public void contextLoads()
    {
	Assert.assertEquals(2, bookRepository.count());
    }

    @Test
    public void webappBookIsbnApi()
    {
	Book book = restTemplate.getForObject("http://localhost:" + port + "/books/972-1-78528-415-1", Book.class);
	Assert.assertNotNull(book);
	Assert.assertEquals("Packt", book.getPublisher().getName());
    }

    @Test
    public void webappPublisherApi() throws Exception
    {
	mockMvc.perform(MockMvcRequestBuilders.get("/publishers/1")).
		andExpect(MockMvcResultMatchers.status().isOk()).
		andExpect(MockMvcResultMatchers.content().contentType(MediaType.parseMediaType("application/hal+json;charset=UTF-8"))).
		andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Packt"))).
		andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Packt"));
    }
}
