package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.service.BookService;

import java.util.List;
import java.util.Map;

public class BookServiceImpl implements BookService {
    @Override
    public Map<String, Long> getBooks() {
        return Map.of();
    }

    @Override
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        return List.of();
    }
}
