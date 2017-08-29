package test.com.jive.location.store;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jive.server.location.store.AORProcessor;
import com.jive.server.location.store.AOR_CRUDHandler;

public class AORProcessorTest {

	private String aorFilePath = "./src/main/resources/registrations.json";
	private AOR_CRUDHandler aorCRUDHandler;

	@Before
	public void setup() {
		aorCRUDHandler = new AORProcessor(aorFilePath);
	}

	@Test
	public void testApp() {

		String recordToAdd = null;
		// test find
		List<String> list = aorCRUDHandler.find("0155eaf81e9b6389e2000100620009");
		assertTrue(((list != null) && (list.size() == 1)));
		recordToAdd = list.get(0);

		// Test remove record
		list = aorCRUDHandler.remove("0155eaf81e9b6389e2000100620009");
		assertTrue(((list != null) && (list.size() == 1)));
		// then check that is does not exist
		list = aorCRUDHandler.find("0155eaf81e9b6389e2000100620009");
		assertTrue(list == null);

		// test remove list of record for one AOR Key
		list = aorCRUDHandler.remove("01552608338f396a57000100610001");
		assertTrue(((list != null) && (list.size() == 3)));
		// then check that is does not exist
		list = aorCRUDHandler.find("01552608338f396a57000100610001");
		assertTrue(list == null);

		// then test add
		aorCRUDHandler.add("0155eaf81e9b6389e2000100620009", recordToAdd);
		// then check that is does exist
		list = aorCRUDHandler.find("0155eaf81e9b6389e2000100620009");
		assertTrue(((list != null) && (list.size() == 1)));

	}
}
