package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class TicketMasterAPI {
	// http://developer.ticketmaster.com/products-and-docs/apis/discovery-api/v2/
	private static final String API_HOST = "app.ticketmaster.com";
	private static final String SEARCH_PATH = "/discovery/v2/event.json";
	private static final String DEFAULT_TERM = ""; // no restriction
	private static final String API_KEY = "TPnKGZ8BE4pJmpRd5KEMlTH3aYfAHy8P";
	
	/**
	 *  Creates and sends a request to the TickeMaster API by term and location
	 */
	public JSONArray search (double latitude, double longitude, String term) {
		String url = "http://" + API_HOST + SEARCH_PATH;
		String latlon = latitude + "," + longitude;
		if (term ==  null) {
			term = DEFAULT_TERM;
		}
		term = urlEncodeHelper(term);
		String query = String.format("apikey=%s&latlon=%s&keyword=%s", API_KEY, latlon, term);
		try {
			// Create a URL connection instance that represents a connection to the remote object referred to by the URL. The HttpUrlConnection class allows us to perform basic HTTP requests without the use of any additional libraries. Note that this method only creates a connection object, but does not establish the connection yet
			HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + query).openConnection();
			// Tell what HTTP method to use. GET by default. The HttpUrlConnection class is used for all types of requests: GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE
			connection.setRequestMethod("GET");
			
			// Get the status code from an HTTP reponse message. To execute the request we can sue the getRespnseCode(), connect(), getInputStream(), or getOutputStream() method
			int responseCode = connection.getResponseCode(); // Return an inputstream that reads response data from this open connection. Then we need to parse the inputstream
			System.out.println("\nSending 'GET' request to URL: " + url + "?" + query);
			System.out.println("Response Code: " + responseCode);
			
			// Create a BufferedReader to help read text from a character-input stream. Provide for the efficient reading of characters, arrays, and lines.
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
			// Append response data to response StringBuffer instance line by line
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			// Close the BufferedReader after reading the inputstream.response data
			in.close();
			
			// Extract events array only
			JSONObject responseJson = new JSONObject(response.toString()); // Create a Json object out of the response string
			// Obtain part of the Json object - a Json array that represents events
			JSONObject embedded = (JSONObject) responseJson.get("_embedded");
			JSONArray  events = (JSONArray) embedded.get("events");
			return events;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void queryAPI (double latitude, double longitude) {
		JSONArray  events = search(latitude, longitude, null);
		try {
			for (int i = 0; i < events.length(); i++) {
				JSONObject event = events.getJSONObject(i);
				System.out.println(event);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  Main entry for sample TicketMaster API requests
	 */
	public static void main(String[] args) {
		TicketMasterAPI tmApi = new TicketMasterAPI();
		tmApi.queryAPI(37.38, -122.08);
	}
}
