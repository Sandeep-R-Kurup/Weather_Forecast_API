package org.SandeepRKurup;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
        try {
            callWeatherAPPAPI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void callWeatherAPPAPI() throws URISyntaxException, IOException {
        System.out.println("Please enter the location for which you need to check the weather forcast information");

        Scanner scn= new Scanner(System.in);
        String location= scn.nextLine();

        URIBuilder builder= new URIBuilder("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast");

        builder.setParameter("aggregateHours","24");
        builder.setParameter("contentType","json");
        builder.setParameter("unitGroup","metric");
        builder.setParameter("locationMode","single");
        builder.setParameter("key","1PYNQ6AWUDJE9AFERDCHJHSXK");
        builder.setParameter("locations",location);

        HttpGet getData= new HttpGet(builder.build());
        CloseableHttpClient httpClient= HttpClients.createDefault();
        CloseableHttpResponse response= httpClient.execute(getData);
        httpClient.close();

        if (response.getStatusLine().getStatusCode()==200) {
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);

            JSONObject responseObject= new JSONObject(result);

            JSONObject locationObject= responseObject.getJSONObject("location");
            JSONArray valueArray= locationObject.getJSONArray("values");

            System.out.println("datetimeStr \t \t \t \t mint \t  maxt \t  visibility \t humidity");
            for(int idx=0; idx<valueArray.length(); idx++){
                JSONObject value= valueArray.getJSONObject(idx);
                String dateTime=value.getString("datetimeStr");
                Double minTemp=value.getDouble("mint");
                Double maxTemp=value.getDouble("maxt");
                Double humidity=value.getDouble("humidity");
                Double visibility=value.getDouble("visibility");
                System.out.println();
                System.out.printf("%s %f %f %f %f \n",dateTime, minTemp, maxTemp, humidity,visibility);
            }
        }
        else{
            System.out.println("Something went wrong!");
            throw new RuntimeException();
        }
    }
}