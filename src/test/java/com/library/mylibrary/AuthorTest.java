package com.library.mylibrary;

import com.library.mylibrary.data.entity.Author;
import com.library.mylibrary.data.entity.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorTest {

    @Test
    public void testConstructorAndGetters() {
        Author author = new Author("Zofia", "Kowalczyk", 1980, "Czech Republic");

        assertEquals("Zofia", author.getFirstname());
        assertEquals("Kowalczyk", author.getLastname());
        assertEquals(1980, author.getYearOfBirth());
        assertEquals("Czech Republic", author.getCountry());
    }

    @Test
    public void testSetterAndGetters() {
        Author author = new Author();

        author.setFirstname("Jacek");
        author.setLastname("Murek");
        author.setYearOfBirth(1990);
        author.setCountry("Poland");

        assertEquals("Jacek", author.getFirstname());
        assertEquals("Murek", author.getLastname());
        assertEquals(1990, author.getYearOfBirth());
        assertEquals("Poland", author.getCountry());
    }

    @Test
    public void testAddBook() {
        Author author = new Author("Michał", "Paluch", 1980, "Germany");

        Book book = new Book();
        author.getBooks().add(book);

        assertTrue(author.getBooks().contains(book));
    }

    @Test
    public void testRemoveBook() {
        Author author = new Author("Robert", "Szpak", 1980, "USA");

        Book book = new Book();
        author.getBooks().add(book);
        author.getBooks().remove(book);

        assertFalse(author.getBooks().contains(book));
    }
}