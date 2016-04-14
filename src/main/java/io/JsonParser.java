package io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class JsonParser {

    private final Logger log = LogManager.getLogger(getClass());

    private String BASE_URL;

    public JsonParser(String baseURL) {
        BASE_URL = baseURL;
    }

    public void fetchJsonWithExtension(String urlExtension) {
        urlExtension = BASE_URL + urlExtension;
        fetchJson(urlExtension);
    }

    public void fetchJson(String fullURL) {
        log.info("Start Fetch.");
		try {

            URL url = new URL(fullURL);
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
            parseJsonResponse(obj);

        } catch (IOException e) {
			e.printStackTrace();
		}
        log.info("End Fetch.");
    }

    protected abstract void parseJsonResponse(JSONObject obj);

    public abstract ArrayList<String> buildResponse();
}
