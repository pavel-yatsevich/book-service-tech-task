package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repo.BookServiceRepository;
import com.ifortex.bookservice.service.BookService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    private final BookServiceRepository bookServiceRepository = new BookServiceRepository();

    @Override
    public Map<String, Long> getBooks() {
        try {
            return bookServiceRepository.getBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        try {
            return bookServiceRepository.getAllByCriteria(searchCriteria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
