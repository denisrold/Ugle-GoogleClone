package com.ugle.ugle.services;
import com.ugle.ugle.entities.WebPage;
import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SpiderService {
    @Autowired
    private SearchService searchService;
    public void indexWebPages(){
       List<WebPage> linksToIndex = searchService.getLinksToIndex();
       linksToIndex.stream().parallel().forEach(webPage->{
           try{
               indexWebPage(webPage);
           }
           catch (Exception e){
               System.out.println(e.getMessage());
           }

       });
        /*
        <meta name="description" content="Breaking news, sport, TV, radio and a whole lot more.
        The BBC informs, educates and entertains - wherever you are, whatever your age.">
        <meta name="keywords" content="BBC, bbc.co.uk, bbc.com, Search, British Broadcasting Corporation, BBC iPlayer, BBCi">
        <title>BBC - Homepage</title>
        */

    }

    private void indexWebPage(WebPage webPage) throws Exception{
        String url = webPage.getUrl();
        String content = getWebContent(url);
        if(StringHelper.isBlank(content)){
            return;
        }
        indexAndSaveWebPage(webPage, content);
        saveLinks(getDomain(url), content);
    }

    private String getDomain(String url) {
        String[] aux = url.split("/");
        return aux[0] + "//"+ aux[2];
    }

    private void saveLinks(String domain,String content){
        List<String> links = getLinks(domain, content);
        links.stream().filter(link->!searchService.exist(link))
                    .map(link->new WebPage(link))
                    .forEach(webPage -> searchService.save(webPage));
    };
    private void indexAndSaveWebPage(WebPage webPage, String content) {
        String title = getTitle(content);
        String description = getDescription(content);
        webPage.setTitle(title);
        webPage.setDescription(description);
        searchService.save(webPage);
    }
    public List<String> getLinks(String domain, String content){
        List<String> links = new ArrayList<>();
        String[] splitHref = content.split("href=\"");
        List<String> listHref = Arrays.asList(splitHref);
        listHref.forEach(strHref->{
            String[] aux = strHref.split("\"");
            links.add(aux[0]);
        });

        return cleanLinks(domain,links);
    };

    private List<String> cleanLinks(String domain,List<String> links){
        //filtro las extensiones que no quiero guardar
        String[] excludeExtensions = new String[]{"css","js","jpg","png","woff2","json"};

      List<String>  resultLinks = links.stream()
                .filter(link -> Arrays.stream(excludeExtensions)
                .noneMatch(extension-> link.endsWith(extension)))
                .map(link-> link.startsWith("/") ? domain+link:link)
                .filter(link->link.startsWith("http"))
                .collect(Collectors.toList());
      List<String> uniqueLinks= new ArrayList<>();
      uniqueLinks.addAll(new HashSet<String>(resultLinks));

      return uniqueLinks;
    }
    public String getTitle(String content){
        String[] aux = content.split("<title>");
        String[] aux2 = aux[1].split("</title>");
        return aux2[0];
    };

    public String getDescription(String content){
        String[] aux = content.split("<meta name=\"description\" content=\"");
        String[] aux2 = aux[1].split("\">");
        return aux2[0];
    };
    private static String getWebContent(String link) {
        try{
            //objectUrl
            URL url = new URL(link);
            //Conexión:
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //download header content
            String encoding = conn.getContentEncoding();
            //download information
            InputStream input = conn.getInputStream();
            Stream<String> lines = new BufferedReader(new InputStreamReader(input))
                    .lines();

            return lines.collect(Collectors.joining());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        };
        return "";
    };
}
