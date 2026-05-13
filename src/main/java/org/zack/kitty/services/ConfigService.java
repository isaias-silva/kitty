package org.zack.kitty.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.zack.kitty.dto.ConfigData;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigService {

	private final String CONFIG_PATH = "kitty-config.json";

	private final ObjectMapper objectMapper;

	private int tentatives = 0;

	public ConfigService() {
		objectMapper = new ObjectMapper();
	}


	public ConfigData getConfigurations() throws IOException {
		try {
		String configFile = Files.readString(Path.of(CONFIG_PATH));
			tentatives = 0;

			return objectMapper.readValue(configFile, ConfigData.class);

		} catch (IOException e) {

			generateConfig(new ConfigData());
			tentatives += 1;
			if (tentatives < 5) {
				return getConfigurations();
			}
			throw new RuntimeException("Max tentatives to load configurations");
		}
	}


	public void generateConfig(ConfigData configData) throws IOException {
		byte[] parse = objectMapper.writeValueAsBytes(configData);

		Files.write(Path.of(CONFIG_PATH), parse);
	}
}