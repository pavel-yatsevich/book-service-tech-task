package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repo.BookRepository;
import com.ifortex.bookservice.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
@Primary
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Map<String, Long> getBooks() {
        try {
            return bookRepository.getBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        try {
            return bookRepository.getAllByCriteria(searchCriteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
