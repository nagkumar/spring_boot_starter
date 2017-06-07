package org.bookpub.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
	property = "id",
	scope = Book.class)
public class Book
{
    @Id
    @GeneratedValue
    private Long id;
    private String isbn;
    private String title;
    private String description;

    @ManyToOne
    private Author author;
    @ManyToOne
    private Publisher publisher;

    @ManyToMany
    private List<Reviewer> reviewers;

    protected Book()
    {
    }

    public Book(final String aISBN, final String aTitle, final Author aAuthor, final Publisher aPublisher)
    {
	isbn = aISBN;
	title = aTitle;
	author = aAuthor;
	publisher = aPublisher;
    }

    public Long getId()
    {
	return id;
    }

    public void setId(final Long aId)
    {
	id = aId;
    }

    public String getIsbn()
    {
	return isbn;
    }

    public void setIsbn(final String aISBN)
    {
	isbn = aISBN;
    }

    public String getTitle()
    {
	return title;
    }

    public void setTitle(final String aTitle)
    {
	title = aTitle;
    }

    public String getDescription()
    {
	return description;
    }

    public void setDescription(final String aDescription)
    {
	description = aDescription;
    }

    public Author getAuthor()
    {
	return author;
    }

    public void setAuthor(final Author aAuthor)
    {
	author = aAuthor;
    }

    public Publisher getPublisher()
    {
	return publisher;
    }

    public void setPublisher(final Publisher aPublisher)
    {
	publisher = aPublisher;
    }

    public List<Reviewer> getReviewers()
    {
	return reviewers;
    }

    public void setReviewers(final List<Reviewer> aReviewers)
    {
	reviewers = aReviewers;
    }
}
