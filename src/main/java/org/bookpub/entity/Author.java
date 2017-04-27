package org.bookpub.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Author
{
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "author")
    private List<Book> books;

    protected Author()
    {
    }

    public Author(final String aFirstName, final String aLastName)
    {
        firstName = aFirstName;
        lastName = aLastName;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long aId)
    {
        id = aId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(final String aFirstName)
    {
        firstName = aFirstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(final String aLastName)
    {
        lastName = aLastName;
    }
}
