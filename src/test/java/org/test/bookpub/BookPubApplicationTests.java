package org.test.bookpub;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
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
import org.test.bookpub.entity.Book;
import org.test.bookpub.repository.BookRepository;

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
                andExpect(MockMvcResultMatchers.content().contentType(MediaType.parseMediaType("application/hal+json"))).
                andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Packt"))).
                andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Packt"));
    }
}
