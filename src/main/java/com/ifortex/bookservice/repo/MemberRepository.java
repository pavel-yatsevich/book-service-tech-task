package com.ifortex.bookservice.repo;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class MemberRepository extends DBConfig {

    public Member findMember() throws SQLException {
        String getMemberSQL = "SELECT member_id AS id, name, membership_date FROM member_books " +
                "JOIN books b ON b.id = book_id JOIN members m ON m.id = member_id " +
                "WHERE array_to_string(genre, ',') LIKE '%Romance' " +
                "ORDER BY publication_date, membership_date DESC LIMIT 1";
        Member member = null;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             ResultSet resultSet = connection.prepareStatement(getMemberSQL)
                     .executeQuery()) {
            while (resultSet.next()) {
                member = getMember(resultSet);
                member.setBorrowedBooks(getUserBooks(member.getId()));
            }
        }
        return member;
    }

    public List<Member> findMembers() throws SQLException {
        String getMemberSQL = "select * from members " +
                "where membership_date between '2023-01-01 00:00:00.000000' and '2023-12-31 23:59:59.999999' " +
                "and not exists(select member_books.member_id from member_books where member_id = members.id)";
        List<Member> membersList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             ResultSet resultSet = connection.prepareStatement(getMemberSQL)
                     .executeQuery()) {
            Member member;
            while (resultSet.next()) {
                member = getMember(resultSet);
                member.setBorrowedBooks(getUserBooks(member.getId()));
                membersList.add(member);
            }
        }
        return membersList;
    }

    private static Member getMember(ResultSet resultSet) throws SQLException {
        Member member = new Member();
        member.setId(resultSet.getLong("id"));
        member.setName(resultSet.getString("name"));
        member.setMembershipDate(resultSet.getTimestamp("membership_date").toLocalDateTime());
        return member;
    }

    private List<Book> getUserBooks(long userId) throws SQLException {
        String getBookSQL = "SELECT id, title, author, description, publication_date, genre " +
                "FROM books JOIN book_service.member_books mb ON books.id = mb.book_id " +
                "WHERE member_id = ? ORDER BY publication_date";
        List<Book> booksList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getBookSQL)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                booksList.add(getBookFromSet(resultSet));
            }
        }
        return booksList;
    }

    private static Book getBookFromSet(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        book.setDescription(resultSet.getString("description"));
        book.setPublicationDate(resultSet.getTimestamp("publication_date").toLocalDateTime());
        book.setGenres(Set.of(String.valueOf(resultSet.getArray("genre"))));
        return book;
    }
}
