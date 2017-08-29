package com.jive.server.location.store;

import java.util.List;

public interface AOR_CRUDHandler {

	List<String> find(String aorEntryKey);

	void add(String aorEntryKey, String aorValue);

	List<String> remove(String aorEntryKey);

}
