package com.ifortex.bookservice.repo;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class BookRepository extends DBConfig {

    public Map<String, Long> getBooks() throws SQLException {
        String getAllBooksByGenreSQL = "SELECT UNNEST(genre) AS genres, COUNT(*) AS number_of_books " +
                "FROM books GROUP BY genres ORDER BY number_of_books DESC;";
        Map<String, Long> books = new LinkedHashMap<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             ResultSet resultSet = connection.prepareStatement(getAllBooksByGenreSQL)
                     .executeQuery()) {
            while (resultSet.next()) {
                books.put(resultSet.getString("genres"), resultSet.getLong("number_of_books"));
            }
        }
        return books;
    }

    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) throws SQLException {
        String getBooksByCriteriaSQL = "SELECT * FROM books WHERE title ILIKE ? AND author ILIKE ? " +
                "AND array_to_string(genre, ',') ILIKE ? AND description ILIKE ? " +
                "AND publication_date BETWEEN CAST( ? AS TIMESTAMP) AND CAST( ? AS TIMESTAMP) ORDER BY publication_date";
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
        addCriteriaParameter(searchCriteria.getTitle(), statement, 1);
        addCriteriaParameter(searchCriteria.getAuthor(), statement, 2);
        addCriteriaParameter(searchCriteria.getGenre(), statement, 3);
        addCriteriaParameter(searchCriteria.getDescription(), statement, 4);
        addCriteriaYearParameter(statement, searchCriteria);
    }

    private static void addCriteriaYearParameter(PreparedStatement statement, SearchCriteria searchCriteria) throws SQLException {
        if (searchCriteria.getYear() > 0) {
            statement.setString(5, getStartDate(searchCriteria));
            statement.setString(6, getEndDate(searchCriteria));
        } else {
            String startDateWhenEmptyCriteria = "0001-01-01 00:00:00.000000";
            String endDateWhenEmptyCriteria = "9999-12-31 23:59:59.999999'";
            statement.setString(5, startDateWhenEmptyCriteria);
            statement.setString(6, endDateWhenEmptyCriteria);
        }
    }

    private static void addCriteriaParameter(String searchCriteria, PreparedStatement statement, int parameterIndex) throws SQLException {
        if (!searchCriteria.isEmpty()) {
            statement.setString(parameterIndex, "%" + searchCriteria + "%");
        } else statement.setString(parameterIndex, "%%");
    }

    private static String getEndDate(SearchCriteria searchCriteria) {
        return searchCriteria.getYear() + "-12-31 23:59:59.999999'";
    }

    private static String getStartDate(SearchCriteria searchCriteria) {
        return searchCriteria.getYear() + "-01-01 00:00:00.000000";
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
