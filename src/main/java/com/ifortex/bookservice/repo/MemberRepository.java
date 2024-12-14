package com.ifortex.bookservice.repo;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository extends DBConfig {

    public Member findMember() throws SQLException {
        String getMemberSQL = "SELECT member_id AS id, name, membership_date FROM member_books " +
                "JOIN books b ON b.id = book_id JOIN members m ON m.id = member_id " +
                "WHERE array_to_string(genre, ',') LIKE '%Romance' " +
                "ORDER BY publication_date, membership_date DESC LIMIT 1";
        Member member = new Member();
        
        try (Connection connection = DriverManager.getConnection(url, username, password);
             ResultSet resultSet = connection.prepareStatement(getMemberSQL)
                     .executeQuery()) {
            while (resultSet.next()) {
                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("name"));
                member.setMembershipDate(resultSet.getTimestamp("membership_date").toLocalDateTime());
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
            while (resultSet.next()) {
                Member member = new Member();
                member.setId(resultSet.getInt("id"));
                member.setName(resultSet.getString("name"));
                member.setMembershipDate(resultSet.getTimestamp("membership_date").toLocalDateTime());
                membersList.add(member);
            }
        }
        return membersList;
    }
}
