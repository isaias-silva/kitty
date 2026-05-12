package org.zack.kitty.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.zack.kitty.dto.ConfigData;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigService {

	private final String CONFIG_PATH = "/tmp/kitty-config.json";

	private final ObjectMapper objectMapper;


	public ConfigService() {
		objectMapper = new ObjectMapper();
	}


	public ConfigData getConfigurations() throws IOException {

		String configFile = Files.readString(Path.of(CONFIG_PATH));
		return objectMapper.readValue(configFile, ConfigData.class);
	}


	public void generateConfig(ConfigData configData) throws IOException {
		byte[] parse = objectMapper.writeValueAsBytes(configData);

		Files.write(Path.of(CONFIG_PATH), parse);
	}
}