package com.ugle.ugle.services;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.stream.Collectors;
@Service
public class SpiderService {
    public String indexWebPage(){
        String url = "https://www.bbc.com/";
        String content = getWebContent(url);
        /*
        <meta name="description" content="Breaking news, sport, TV, radio and a whole lot more.
        The BBC informs, educates and entertains - wherever you are, whatever your age.">
        <meta name="keywords" content="BBC, bbc.co.uk, bbc.com, Search, British Broadcasting Corporation, BBC iPlayer, BBCi">
        <title>BBC - Homepage</title>
        */
        String title = getTitle(content);
        return title;
    }
    public String getTitle(String content){
        String[] aux = content.split("<title>");
        String[] aux2 = aux[1].split("</title>");
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
            String result = new BufferedReader(new InputStreamReader(input))
                    .lines().collect(Collectors.joining());
            return result;
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        };
        return "";
    };
}
