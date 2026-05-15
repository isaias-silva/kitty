package org.zack.kitty.services;

import java.io.IOException;

import org.zack.kitty.dto.ConfigData;

public class ServiceRegistry {

	public static final ServiceRegistry INSTANCE = new ServiceRegistry();

	private final ConfigService configService;

	private final SttService sttService;
	private final AgentService agentService;

	public ServiceRegistry() {
		this.configService = new ConfigService();
		try {
			ConfigData configData = configService.getConfigurations();

			this.sttService = new SttService(configData);
			this.agentService = new AgentService(configData);

		} catch (IOException e) {

			throw new RuntimeException("error in load kitty-config.json");
		}
	}


	public ConfigService getConfigService() {
		return configService;
	}


	public SttService getSttService() {
		return sttService;
	}


	public AgentService getAgentService() {
		return agentService;
	}
}