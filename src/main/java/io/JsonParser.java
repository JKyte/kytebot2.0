package io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class JsonParser {

    private final Logger log = LogManager.getLogger(getClass());
    String baseUrl = "https://politicsandwar.com/api/tradeprice/resource=";
	String resource;
	String avgPrice;
	String lowBuyDate;
	String lowBuyAmount;
	String highestBuyDate;
	String highestBuyAmount;

	public JsonParser( String resource ){
		this.resource = resource;
	}

	public void fetchJson(){
        log.info("Start Fetch.");
        String fullUrl = baseUrl + resource;
		try {
			
			URL url = new URL(fullUrl);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection(); 
			httpcon.addRequestProperty("User-Agent", "Mozilla/4.76"); 
			
			Scanner scan = new Scanner(httpcon.getInputStream());
            log.info("Response code: " + httpcon.getResponseCode());

			if( httpcon.getResponseCode() != 200 ){
				scan.close();	//	Prevent mem leaks
				return;
			}

            StringBuilder sb = new StringBuilder();
            while (scan.hasNext())
                sb.append(scan.nextLine());
            scan.close();

            log.info(sb.toString());

            // build a JSON object
            JSONObject obj = new JSONObject(sb.toString());

            avgPrice = obj.getString("avgprice");
			
			// get the first result
			JSONObject highestbuy = obj.getJSONObject("highestbuy");
			highestBuyDate = highestbuy.getString("date");
			highestBuyAmount = highestbuy.getString("price");
			JSONObject lowestbuy = obj.getJSONObject("lowestbuy");
			lowBuyDate = lowestbuy.getString("date");
			lowBuyAmount = lowestbuy.getString("price");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        log.info("End Fetch.");
    }
	
	public String getResourceAverage(){
		return resource + ":" + avgPrice;
	}
	
	public ArrayList<String> buildResponse(){
        ArrayList<String> lines = new ArrayList<>();
        lines.add( resource.toUpperCase() + "\t$" + avgPrice );
	//	lines.add( "HI: $"+highestBuyAmount+" @ "+highestBuyDate);
	//	lines.add( "LO: $"+lowBuyAmount+" @ "+lowBuyDate);
		return lines;
	}
}
