package io;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JKyte on 4/13/2016.
 */
public class PWTradeJsonParser extends JsonParser {

    String resource;
    String avgPrice;
    String lowBuyDate;
    String lowBuyAmount;
    String highestBuyDate;
    String highestBuyAmount;

    public PWTradeJsonParser(String baseURL) {
        super(baseURL);
    }

    //  The extension is also the resource
    @Override
    public void fetchJsonWithExtension(String urlExtension) {
        this.resource = urlExtension;
        super.fetchJsonWithExtension(urlExtension);
    }

    @Override
    protected void parseJsonResponse(JSONObject obj) {
        avgPrice = obj.getString("avgprice");

        // get the first result
        JSONObject highestbuy = obj.getJSONObject("highestbuy");
        highestBuyDate = highestbuy.getString("date");
        highestBuyAmount = highestbuy.getString("price");
        JSONObject lowestbuy = obj.getJSONObject("lowestbuy");
        lowBuyDate = lowestbuy.getString("date");
        lowBuyAmount = lowestbuy.getString("price");
    }

    @Override
    public ArrayList<String> buildResponse() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(resource.toUpperCase() + "\t$" + avgPrice);
        //	lines.add( "HI: $"+highestBuyAmount+" @ "+highestBuyDate);
        //	lines.add( "LO: $"+lowBuyAmount+" @ "+lowBuyDate);
        return lines;
    }
}
