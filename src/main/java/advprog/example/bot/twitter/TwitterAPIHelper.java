package advprog.example.bot.twitter;

import advprog.example.bot.twitter.objects.Tweet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TwitterAPIHelper {
    private static TwitterAPIHelper instance;
    private static String bearerToken;

    public static TwitterAPIHelper getInstance() {
        if (instance != null) {
            instance.authenticate();
            return instance;
        } else {
            instance = new TwitterAPIHelper();
            instance.authenticate();
            return instance;
        }
    }

    public String requestGet(String url) {
        try {
            // Parse parameters to the URL
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.addRequestProperty("authorization", "Bearer " + bearerToken);

            // Execute and get the response
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void authenticate() {
        try {
            String accessToken = "UEM1RzZhUGdPYkN4TU8yNWFoWUZJTmFTSDoyU0dPaG5VeVVNdFFGWmV3WWFCendjb"
                    + "Dg3bk0xUkgwbEo1UWxOSFVnYUNTUzhHZXpXbg==";
            String urlToken = "https://api.twitter.com/oauth2/token/?grant_type=client_credentials";

            URL obj = new URL(urlToken);

            HttpURLConnection bearer = (HttpURLConnection) obj.openConnection();

            bearer.setRequestMethod("POST");
            bearer.setRequestProperty("Authorization", "Basic " + accessToken);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(bearer.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            JSONObject response_bearer = new JSONObject(content.toString());
            bearerToken = response_bearer.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Tweet> getRecentTweets(String username) {
        String response = requestGet("https://api.twitter.com/1.1/statuses/user_timeline.json?"
                + "screen_name=" + username + "&count=5");
        JSONArray json = new JSONArray(response);
        return Tweet.parseJsonList(json);
    }
}