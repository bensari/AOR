package test.com.jive.location.store;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jive.server.location.store.AORPRovider;
import com.jive.server.location.store.AORProcessor;

public class AORPRoviderTest {

	private String aorFilePath = "./src/main/resources/registrations.json";
	private AORPRovider aORProvider;

	@Before
	public void setup() {
		aORProvider = new AORProcessor(aorFilePath);
	}

	@Test
	public void test() {
		// test one record
		List<String> list = aORProvider.getAOR("0155eaf81e9b6389e2000100620009");
		assertTrue(((list != null) && (list.size() == 1)));
		// test 3 record for one AOR Key
		list = aORProvider.getAOR("01552608338f396a57000100610001");
		assertTrue(((list != null) && (list.size() == 3)));
		// test no record for not found AOR key
		list = aORProvider.getAOR("AA");
		assertTrue(list == null);
		// test empty key
		list = aORProvider.getAOR("");
		assertTrue(list == null);
		// test null key
		list = aORProvider.getAOR(null);
		assertTrue(list == null);
	}

}
