package org.zack.kitty.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zack.kitty.core.BaseNode;
import org.zack.kitty.core.annotations.Node;
import org.zack.kitty.dto.ConfigData;

import com.fasterxml.jackson.databind.ObjectMapper;

@Node
public class ConfigService extends BaseNode {

	private static final Path CONFIG_DIR = Path.of(
		System.getProperty("user.home"), ".config", "kitty"
	);

	private static final Path CONFIG_FILE = CONFIG_DIR.resolve("kitty-config.json");

	private final ObjectMapper objectMapper;
	private static final Logger log = LoggerFactory.getLogger(ConfigService.class);


	public ConfigService() {
		objectMapper = new ObjectMapper();
	}


	public ConfigData getConfigurations() {
		try {


		if (!Files.exists(CONFIG_FILE)) {
			generateConfig(new ConfigData());
		}

		String configFile = Files.readString(CONFIG_FILE);
			return objectMapper.readValue(configFile, ConfigData.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public void generateConfig(ConfigData configData) {

		try {

		Files.createDirectories(CONFIG_DIR);

		byte[] parsed = objectMapper.writeValueAsBytes(configData);

		Files.write(CONFIG_FILE, parsed);
			log.debug("Config file generated");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}