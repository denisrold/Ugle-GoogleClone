package com.ugle.ugle.repositories;

import com.ugle.ugle.entities.WebPage;

import java.util.List;

public interface SearchRepository {
    List<WebPage> search(String textSearch);
}
