package com.ifortex.bookservice.repo;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BookServiceRepository extends DBConfig {

    public Map<String, Long> getBooks() throws SQLException {
        String getAllBooksByGenre = "SELECT UNNEST(genre) AS genres, COUNT(*) AS number_of_books " +
                "FROM books GROUP BY genres ORDER BY number_of_books DESC";
        Map<String, Long> books = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(getAllBooksByGenre);
            while (rs.next()) {
                books.put(rs.getString("genre"), rs.getLong("number_of_books"));
            }
        }
        return books;
    }
}
