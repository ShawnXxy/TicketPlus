package external;

import java.util.List;

import entity.Item;

public interface ExternalAPI {
	public List<Item> search (double latitude, double longitude, String term);
}
