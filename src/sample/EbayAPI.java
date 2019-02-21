package sample;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class EbayAPI {
    public Main main;
    String paramValue;
    String result;
    String response;
    int resultCount;


    public void Create() {

        paramValue = main.input;

        String key = "";
        try {
            Scanner input = new Scanner(new File ("ebay_key.txt"));
            String answer = input.nextLine();
            key = answer;
        }
        catch (Exception e ) {

            System.out.println(e);
        }

    System.out.println(key);
        try {

            paramValue = paramValue.replace(" ", "+");

            URL url = new URL("https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SECURITY-APPNAME=" + key +"&keywords=" + paramValue +"&RESPONSE-DATA-FORMAT=JSON&GLOBAL-ID=EBAY-GB&buyerPostalCode=SE1+0AD&itemFilter.name=LocatedIn&itemFilter.value=WorldWide");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
           //conn.setRequestProperty("Authorization", "Bearer <" + key + ">");
            conn.setRequestProperty("Accept", "application/json");


            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            response = br.readLine();
            System.out.println(response);
            for (int i = 0; i < getNames(response).size(); i++){
                //System.out.println("result " + i + " : " +getNames(response).get(i) + " | " + getCondition(response).get(i) + " | " + getPrices(response).get(i) + " | " + getShippingPrices(response).get(i)) + " | " + getUrl(response).get(i);
                result = "result " + i + " : " +getNames(response).get(i) + " | " + getCondition(response).get(i) + " | " + getPrices(response).get(i) + " | " + getShippingPrices(response).get(i)+ " | " + getUrl(response).get(i);
                resultCount = i;
                //System.out.println("result " + getNames(response).get(i));

            }




        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }



    }
    public static ArrayList getPrices(String r){
        ArrayList<Double> result = new ArrayList<Double>();
        JsonElement jsonElement = new JsonParser().parse(r);
        JsonObject jobject = jsonElement.getAsJsonObject();
        JsonArray findItemsByKeywordsResponse = jobject.getAsJsonArray("findItemsByKeywordsResponse");
        for (int i = 0; i < findItemsByKeywordsResponse.size(); i++){
            jobject = findItemsByKeywordsResponse.get(i).getAsJsonObject();
            JsonArray searchResults = jobject.getAsJsonArray("searchResult");
            for (int j = 0; j < searchResults.size(); j++) {
                jobject = searchResults.get(j).getAsJsonObject();
                JsonArray item = jobject.getAsJsonArray("item");
                for (int k = 0; k < item.size(); k++) {
                    jobject = item.get(k).getAsJsonObject();
                    JsonArray sellingStatus = jobject.getAsJsonArray("sellingStatus");
                    for(int l = 0; l < sellingStatus.size(); l++){
                        jobject = sellingStatus.get(l).getAsJsonObject();
                        JsonArray currentPrice = jobject.getAsJsonArray("currentPrice");
                        for(int m = 0; m < currentPrice.size(); m++){
                            jobject = currentPrice.get(m).getAsJsonObject();
                            result.add(jobject.get("__value__").getAsDouble());
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ArrayList getShippingPrices(String r){
        ArrayList<Double> result = new ArrayList<Double>();
        JsonElement jsonElement = new JsonParser().parse(r);
        JsonObject jobject = jsonElement.getAsJsonObject();
        JsonArray findItemsByKeywordsResponse = jobject.getAsJsonArray("findItemsByKeywordsResponse");
        for (int i = 0; i < findItemsByKeywordsResponse.size(); i++){
            jobject = findItemsByKeywordsResponse.get(i).getAsJsonObject();
            JsonArray searchResults = jobject.getAsJsonArray("searchResult");
            for (int j = 0; j < searchResults.size(); j++) {
                jobject = searchResults.get(j).getAsJsonObject();
                JsonArray item = jobject.getAsJsonArray("item");
                for (int k = 0; k < item.size(); k++) {
                    jobject = item.get(k).getAsJsonObject();
                    JsonArray shippingInfo = jobject.getAsJsonArray("shippingInfo");
                    for(int l = 0; l < shippingInfo.size(); l++){
                        jobject = shippingInfo.get(l).getAsJsonObject();
                        JsonArray shippingServiceCost = jobject.getAsJsonArray("shippingServiceCost");
                        for(int m = 0; m < shippingServiceCost.size(); m++){
                            jobject = shippingServiceCost.get(m).getAsJsonObject();
                            result.add(jobject.get("__value__").getAsDouble());
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ArrayList getNames(String r){
        ArrayList<String> result = new ArrayList<String>();
        JsonElement jsonElement = new JsonParser().parse(r);
        JsonObject jobject = jsonElement.getAsJsonObject(); // man rods zinau , ka daryt

        JsonArray findItemsByKeywordsResponse = jobject.getAsJsonArray("findItemsByKeywordsResponse");
        for (int i = 0; i < findItemsByKeywordsResponse.size(); i++){
            jobject = findItemsByKeywordsResponse.get(i).getAsJsonObject();
            JsonArray searchResults = jobject.getAsJsonArray("searchResult");
            for (int j = 0; j < searchResults.size(); j++) {
                jobject = searchResults.get(j).getAsJsonObject();
                JsonArray item = jobject.getAsJsonArray("item");
                for (int k = 0; k < item.size(); k++) {
                    jobject = item.get(k).getAsJsonObject();
                    result.add(jobject.get("title").getAsString());
                }
            }
        }
        return result;
    }

    public static ArrayList getUrl(String r){
        ArrayList<String> result = new ArrayList<String>();
        JsonElement jsonElement = new JsonParser().parse(r);
        JsonObject jobject = jsonElement.getAsJsonObject(); // man rods zinau , ka daryt

        JsonArray findItemsByKeywordsResponse = jobject.getAsJsonArray("findItemsByKeywordsResponse");
        for (int i = 0; i < findItemsByKeywordsResponse.size(); i++){
            jobject = findItemsByKeywordsResponse.get(i).getAsJsonObject();
            JsonArray searchResults = jobject.getAsJsonArray("searchResult");
            for (int j = 0; j < searchResults.size(); j++) {
                jobject = searchResults.get(j).getAsJsonObject();
                JsonArray item = jobject.getAsJsonArray("item");
                for (int k = 0; k < item.size(); k++) {
                    jobject = item.get(k).getAsJsonObject();
                    result.add(jobject.get("viewItemURL").getAsString());
                }
            }
        }
        return result;
    }

    public static ArrayList getCondition(String r){
        ArrayList<String> result = new ArrayList<String>();
        JsonElement jsonElement = new JsonParser().parse(r);
        JsonObject jobject = jsonElement.getAsJsonObject();
        JsonArray findItemsByKeywordsResponse = jobject.getAsJsonArray("findItemsByKeywordsResponse");
        for (int i = 0; i < findItemsByKeywordsResponse.size(); i++) {
            jobject = findItemsByKeywordsResponse.get(i).getAsJsonObject();
            JsonArray searchResults = jobject.getAsJsonArray("searchResult");
            for (int j = 0; j < searchResults.size(); j++) {
                jobject = searchResults.get(j).getAsJsonObject();
                JsonArray item = jobject.getAsJsonArray("item");
                for (int k = 0; k < item.size(); k++) {
                    jobject = item.get(k).getAsJsonObject();
                    JsonArray condition = jobject.getAsJsonArray("condition");
                    for (int l = 0; l < condition.size(); l++) {
                        jobject = condition.get(l).getAsJsonObject();

                        result.add(jobject.get("conditionDisplayName").getAsString());

                    }
                }
            }
        }
        return result;
    }
}
