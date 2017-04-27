package org.bookpub.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id",
        scope = Publisher.class)
public class Publisher
{
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "publisher")
    private List<Book> books;

    protected Publisher()
    {
    }

    public Publisher(final String aName)
    {
        name = aName;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long aId)
    {
        id = aId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String aName)
    {
        name = aName;
    }

    public List<Book> getBooks()
    {
        return books;
    }

    public void setBooks(final List<Book> aBooks)
    {
        books = aBooks;
    }
}
