package com.jive.server.location.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AORLoader {

	private static final Logger logger = LogManager.getLogger(AORLoader.class);

	public LinkedList<String> getAORs(String fileName) {

		LinkedList<String> list = new LinkedList<String>();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach((line) -> {
				list.add(line);
			});

		} catch (IOException e) {
			logger.error(e);
		}
		return list;
	}

}
