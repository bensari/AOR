package com.jive.server.location.store;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class AORProcessor implements AOR_CRUDHandler, AORPRovider {

	private static final Logger logger = LogManager.getLogger(AORProcessor.class);

	private Map<String, List<String>> aorTable;
	private AORLoader aORLoader;

	public AORProcessor(String aorFilePath) {
		aorTable = new ConcurrentHashMap<String, List<String>>();
		List<String> list = getAORList(aorFilePath);
		if (list != null) {
			feedTheMap(list);
		}
	}

	private void feedTheMap(List<String> list) {
		// now that we have the list we put it in a map
		list.forEach((line) -> {
			if (line != null) {
				String aor = getAOREntryKey(line);
				if (aor != null) {
					add(aor, line);
				} else {
					// could be an empty line
					logger.warn("having problem fetching aor key from record {}", line);
				}
			} // could be empty table
		});
	}

	private List<String> getAORList(String filePath) {
		// Instantiate the loader that will load in a list the AORs from
		// a file
		aORLoader = new AORLoader();
		List<String> list = aORLoader.getAORs(filePath);
		if (list == null) {
			// We can throw an exception to let the caller decide what to do
			logger.debug("Having problem to load registrations");
			// we also either exit or we keep going in order to receive AORs
			// to store for future use
			return null;
		}
		return list;
	}

	private String getAOREntryKey(String line) {
		JSONObject json = (JSONObject) JSONSerializer.toJSON(line);
		return (String) json.get("addressOfRecord");
	}

	@Override
	public List<String> find(String aorEntryKey) {
		return ((aorEntryKey != null) ? aorTable.get(aorEntryKey) : null);
	}

	@Override
	public void add(String aorEntryKey, String aorValue) {
		CopyOnWriteArrayList<String> list = null;
		if ((aorEntryKey != null) && (aorValue != null)) {
			if ((list = (CopyOnWriteArrayList<String>) find(aorEntryKey)) != null) {
				list.add(aorValue);
			} else {
				list = new CopyOnWriteArrayList<String>();
				list.add(aorValue);
			}
			aorTable.put(aorEntryKey, list);
		}
	}

	@Override
	public List<String> remove(String aorEntryKey) {
		// This is wildcard remove (we remove all the entries for this AOR)
		return ((aorEntryKey != null) ? aorTable.remove(aorEntryKey) : null);
	}

	@Override
	public List<String> getAOR(String aorEntryKey) {
		return find(aorEntryKey);
	}
}
