package com.jive.server.location;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jive.server.location.io.network.LocationService;
import com.jive.server.location.store.AOR_CRUDHandler;
import com.jive.server.location.store.AORPRovider;
import com.jive.server.location.store.AORProcessor;

public class ServiceLauncher {

	private static final Logger logger = LogManager.getLogger(ServiceLauncher.class);

	private AOR_CRUDHandler aorCRUDHandler;
	private LocationService locationService;
	private int defaultPort = 5555;
	private String defaultIPAddress = "localhost";
	private String aorFilePath = "./src/main/resources/registrations.json";

	public static void main(String[] args) {
		logger.debug("Launching app {}", ServiceLauncher.class);
		new ServiceLauncher();
	}

	public ServiceLauncher() {
		aorCRUDHandler = new AORProcessor(aorFilePath);
		startRequestListener();
	}

	private void startRequestListener() {
		locationService = new LocationService((AORPRovider) aorCRUDHandler, defaultIPAddress, defaultPort);
		try {
			locationService.start();
		} catch (Exception e) {
			logger.error(e);
			// listener did not start as expected
			System.exit(0);
		}
	}
}
