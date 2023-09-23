package com.ugle.ugle.controlllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SearchController {
    @RequestMapping(value="api/search",method = RequestMethod.GET)

    public List<WebPage> search(){
        List<WebPage> result = new ArrayList<>();
    }
}
