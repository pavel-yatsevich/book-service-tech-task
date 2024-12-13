package com.ifortex.bookservice.repo;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;

import java.sql.*;
import java.util.*;

public class BookServiceRepository extends DBConfig {

    public Map<String, Long> getBooks() throws SQLException {
        String getAllBooksByGenreSQL = "SELECT UNNEST(genre) AS genres, COUNT(*) AS number_of_books " +
                "FROM books GROUP BY genres ORDER BY number_of_books DESC";
        Map<String, Long> books = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             ResultSet resultSet = connection.prepareStatement(getAllBooksByGenreSQL)
                     .executeQuery()) {
            while (resultSet.next()) {
                books.put(resultSet.getString("genre"), resultSet.getLong("number_of_books"));
            }
        }
        return books;
    }

    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) throws SQLException {
        String getBooksByCriteriaSQL = "SELECT * FROM books WHERE title ILIKE ? AND author ILIKE ? " +
                "AND array_to_string(genre, ',') ILIKE ? AND description ILIKE ? " +
                "AND publication_date BETWEEN ? and ? ORDER BY publication_date";
        List<Book> books = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(getBooksByCriteriaSQL)) {
            addSearchCriteriaParametersToStatement(statement, searchCriteria);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = getBookFromSet(resultSet);
                books.add(book);
            }
            resultSet.close();
        }
        return books;
    }

    private void addSearchCriteriaParametersToStatement(PreparedStatement statement, SearchCriteria searchCriteria) throws SQLException {
        if (searchCriteria.getTitle() != null) {
            statement.setString(1, "%" + searchCriteria.getTitle() + "%");
        }
        if (searchCriteria.getAuthor() != null) {
            statement.setString(2, "%" + searchCriteria.getAuthor() + "%");
        }
        if (searchCriteria.getGenre() != null) {
            statement.setString(3, "%" + searchCriteria.getGenre() + "%");
        }
        if (searchCriteria.getDescription() != null) {
            statement.setString(4, "%" + searchCriteria.getDescription() + "%");
        }
        if (searchCriteria.getYear() != null) {
            statement.setString(5, searchCriteria.getYear() + "-01-01");
            statement.setString(6, searchCriteria.getYear() + "-12-31");
        }
    }

    private static Book getBookFromSet(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setTitle(resultSet.getString("title"));
        book.setDescription(resultSet.getString("description"));
        book.setAuthor(resultSet.getString("author"));
        book.setPublicationDate(resultSet.getTimestamp("publication_date").toLocalDateTime());
        book.setGenres(Set.of(String.valueOf(resultSet.getArray("genre"))));
        return book;
    }
}
