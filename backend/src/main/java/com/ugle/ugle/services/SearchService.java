package com.ugle.ugle.services;

import com.ugle.ugle.entities.WebPage;
import com.ugle.ugle.repositories.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    @Autowired
    SearchRepository repository;
    public List<WebPage> search(String textSearch){
        return repository.search(textSearch);
    }
}
