package db;

import java.util.List;
import java.util.Set;

import entity.Item;

public interface DBConnection {
	/**
	 * 	Close the connection
	 */
	public void close();
	
	/**
	 * 	Insert the favorite items for a user
	 * 
	 * 	@param userId
	 * 	@param itemIds
	 */
	public void setFavoriteItems (String userId, List<String> itemIds);
	
	/**
	 * 	Delete the favorite items for a user
	 * 
	 * 	@param userId
	 * 	@param itemIds
	 */
	public void unsetFacoriteItems (String userId, List<String> itemIds);
	
	/**
	 * 	Get the favorite item id for a user
	 * 
	 * 	@param userId
	 * 	@param items
	 */
	public Set<Item> getFacoriteItems (String userId);
	
	/**
	 * 	Gets categories based on item id
	 * 
	 * 	@param itemId
	 * 	@return set of categories
	 */
	public Set<String> getCategories (String itemId);
	
	/**
	 * 	Search items near a geolocation and a term 
	 * 
	 * 	@param userId
	 * 	@param lat
	 * 	@param lon
	 * 	@param term (Nullable)
	 * 	@return list of terms
	 */
	public List<Item> searchItems (String userId, double latitude, double longitude, String term);
	
	/**
	 * 	Save item into db
	 * 
	 * 	@param item
	 */
	public void saveItem (Item item);
}
