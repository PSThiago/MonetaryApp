package com.example.monetary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Connection {

    public static String getDados(String uri){
        BufferedReader buffer = null;

        try {
            URL url = new URL(uri);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            StringBuilder stringBuilder = new StringBuilder();
            buffer = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

            String row;

            while((row = buffer.readLine()) != null) {
                stringBuilder.append(row).append("\n");
            }
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            if(buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
