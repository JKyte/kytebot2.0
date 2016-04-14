package io;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JKyte on 4/13/2016.
 */
public class YoutubeTitleJsonParser extends JsonParser {

    private String videoTitle;

    public YoutubeTitleJsonParser(String baseURL) {
        super(baseURL);
    }

    @Override
    protected void parseJsonResponse(JSONObject obj) {
        videoTitle = obj.getString("title");
    }

    @Override
    public ArrayList<String> buildResponse() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(videoTitle);
        return lines;
    }
}
