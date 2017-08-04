package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

public class GeoRecommendation implements Recommendation {

	@Override
	public List<Item> recommendItems(String userId, double latitude, double longitude) {
		// TODO Auto-generated method stub
//		return null;
		
		DBConnection conn = DBConnectionFactory.getDBConnection();
		// Step 1: fetch all the events(ids	) this user has visited/added to favorite
		Set<String> favoriteItems = conn.getFavoriteItemIds(userId);
		// Step 2: given all these visited events, fetch all categories
		Set<String> allCategories = new HashSet<>();
		for (String item : favoriteItems) {
			allCategories.addAll(conn.getCategories(item));
		}
		// Step 3: given these fetched categories, find the events with above categories in category table
		Set<Item> recommendedItems = new HashSet<>();
		for (String category : allCategories) {
			List<Item> items = conn.searchItems(userId, latitude, longitude, category);
			recommendedItems.addAll(items);
		}
			// Step 4: Filter items that user visited
		List<Item> filteredItems = new ArrayList<>();
		for (Item item : recommendedItems) {
			if (!favoriteItems.contains(item.getItemId())) {
				filteredItems.add(item);
			}
		}
			// Step 5: perfom ranking of these items based on distance
		Collections.sort(filteredItems, new Comparator<Item>() {
			@Override
			public int compare (Item item1, Item item2) {
				double distance1 = getDistance(item1.getLatitude(), item1.getLongitude(), latitude, longitude);
				double distance2 = getDistance(item2.getLatitude(), item2.getLongitude(), latitude, longitude);
				// return the increasing order of distance
				return (int) (distance1 - distance2);
			}
		});
			
		return filteredItems;
	}
		
		// Calculate the distances between two geolocations
		// http://andrew.hedges.name/experiments/haversine/
		private static double getDistance (double lat1, double lon1, double lat2, double lon2) {
			double dlon = lon2 - lon1;
			double dlat = lat2 - lat1;
			double a = Math.sin(dlat / 2 / 180 * Math.PI) * Math.sin(dlat / 2 / 180 * Math.PI) + Math.cos(lat1 / 180 * Math.PI) * Math.cos(lat2 / 180 * Math.PI) * Math.sin(dlon / 2 / 180 * Math.PI) * Math.sin(dlon / 2 / 180 * Math.PI);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			// Radius of earth in miles
			double R = 3961;
			return R * c;
		}
}


