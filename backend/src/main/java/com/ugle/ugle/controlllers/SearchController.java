package com.ugle.ugle.controlllers;

import com.ugle.ugle.entities.WebPage;
import com.ugle.ugle.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SearchController {
    //AutoWired crea un objeto cuando se levanta el servicio- crea un objeto SearchService sin necesidad de usar = new Searchservice;
    @Autowired
    private SearchService service;
    @RequestMapping(value="api/search",method = RequestMethod.GET)
    public List<WebPage> search(@RequestParam Map<String,String> params){
        //api/search?query=Download&lang=en
        //params.get("query"); puedo recibir asi los parametros.
        //params.get("lang");
        String query = params.get("query");
        return service.search(query);
    }
}
