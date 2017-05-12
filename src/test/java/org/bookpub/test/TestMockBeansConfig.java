package org.bookpub.test;

import org.bookpub.annotations.UsedForTesting;
import org.bookpub.repository.PublisherRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

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
