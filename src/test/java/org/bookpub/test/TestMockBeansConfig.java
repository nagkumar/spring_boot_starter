package org.bookpub.test;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.bookpub.annotations.UsedForTesting;
import org.bookpub.repository.PublisherRepository;

@UsedForTesting
public class TestMockBeansConfig
{
    @Bean
    @Primary
    public PublisherRepository createMockPublisherRepository()
    {
        return Mockito.mock(PublisherRepository.class);
    }
}
