package com.example.testc2;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpTest {
    @Test
    public void test1(){
        try {
            URL url = new URL("http://www.bing.com");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            String webPage  = readStream(httpCon.getInputStream());
            System.out.println(webPage);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static final String line = System.getProperty("line.separator");
    private static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine +line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
