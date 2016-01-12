package io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.*;

public class JsonParser {

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
		System.out.println("Start fetch");
		String fullUrl = baseUrl + resource;
		try {
			
			URL url = new URL(fullUrl);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection(); 
			httpcon.addRequestProperty("User-Agent", "Mozilla/4.76"); 
			
			Scanner scan = new Scanner(httpcon.getInputStream());
			System.out.println("ResponseCode: " + httpcon.getResponseCode());
			if( httpcon.getResponseCode() != 200 ){
				scan.close();	//	Prevent mem leaks
				return;
			}
			
			String str = new String();
			while (scan.hasNext())
				str += scan.nextLine();
			scan.close();
			System.out.println("Canary");
			
			// build a JSON object
			JSONObject obj = new JSONObject(str);
			System.out.println("Canary4.5: " + obj.getString("resource"));
			
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
		System.out.println("End fetch");
	}
	
	public String getResourceAverage(){
		return resource + ":" + avgPrice;
	}
	
	public ArrayList<String> buildResponse(){
		ArrayList<String> lines = new ArrayList<String>();
		lines.add( resource.toUpperCase() );	
		lines.add( "HI: $"+highestBuyAmount+" @ "+highestBuyDate);
		lines.add( "LO: $"+lowBuyAmount+" @ "+lowBuyDate);
		return lines;
	}
}
