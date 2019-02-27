package sample;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class EbayItem {

    String key;
    String queryName;
    String response;

    JsonObject rawData;
    ArrayList<EbayItemParsed> parsedData = new ArrayList<EbayItemParsed>();


    EbayItem(String queryName, String key) {

        this.queryName = queryName;
        this.key = key;

        RunQuery();

    }

    void RunQuery() {

        queryName = queryName.replace(" ", "+");

        try {
            URL url = new URL("https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SECURITY-APPNAME=" + key +"&keywords=" + queryName +"&RESPONSE-DATA-FORMAT=JSON&GLOBAL-ID=EBAY-GB&buyerPostalCode=SE1+0AD&itemFilter.name=LocatedIn&itemFilter.value=WorldWide");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200){
                throw new RuntimeException("HTTP Error code : " + connection.getResponseCode());
            }
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            response = bufferedReader.readLine();

        }
        catch (Exception e){
            System.out.println("Exception in Connection: " +e);
        }


        JsonElement jsonElement = new JsonParser().parse(response);
        JsonObject jobject = jsonElement.getAsJsonObject();
        JsonArray findItemsByKeywordsResponse = jobject.getAsJsonArray("findItemsByKeywordsResponse");

        parsedData.clear();
        rawData = jobject;

        for (int i = 0; i < findItemsByKeywordsResponse.size(); i++){
                        jobject = findItemsByKeywordsResponse.get(i).getAsJsonObject();
                        JsonArray searchResult = jobject.getAsJsonArray("searchResult");
                        JsonArray paginationOutput = jobject.getAsJsonArray("paginationOutput");

                        if (searchResult.get(0).getAsJsonObject().get("@count").getAsInt() != 0) {

                            for (int j = 0; j < searchResult.size(); j++) {
                                jobject = searchResult.get(j).getAsJsonObject();
                                JsonArray item = jobject.getAsJsonArray("item");

                                for (int k = 0; k < item.size(); k++) {
                                    EbayItemParsed parsed = new EbayItemParsed();
                                    jobject = item.get(k).getAsJsonObject();

                                    JsonArray condition = jobject.getAsJsonArray("condition");
                                    JsonArray listingInfo = jobject.getAsJsonArray("listingInfo");
                                    JsonArray sellingStatus = jobject.getAsJsonArray("sellingStatus");
                                    JsonArray shippingInfo = jobject.getAsJsonArray("shippingInfo");

                                    parsed.name = jobject.get("title").getAsString();
                                    parsed.url = jobject.get("viewItemURL").getAsString();
                                    parsed.price = sellingStatus.get(0).getAsJsonObject().getAsJsonArray("currentPrice").get(0).getAsJsonObject().get("__value__").getAsFloat();
                                    parsed.condition = condition.get(0).getAsJsonObject().get("conditionDisplayName").getAsString();
                                    parsed.shippingPrice = shippingInfo.get(0).getAsJsonObject().getAsJsonArray("shippingServiceCost").get(0).getAsJsonObject().get("__value__").getAsFloat();
                                    parsed.status = listingInfo.get(0).getAsJsonObject().get("listingType").getAsString();
                                    parsed.totalresults = paginationOutput.get(0).getAsJsonObject().get("totalEntries").getAsInt();

                                    parsedData.add(parsed);
                                }
                            }
                        }
        }
    }
}