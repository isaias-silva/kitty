package org.zack.kitty.services;

import java.io.IOException;

import org.zack.kitty.dto.ConfigData;

public class ServiceRegistry {

	public static final ServiceRegistry INSTANCE = new ServiceRegistry();

	private final ConfigService configService;
	private final SttService sttService;
	private final AgentService agentService;
	private final HtmlConvertService htmlConvertService;

	public ServiceRegistry() {
		this.configService = new ConfigService();
		try {
			ConfigData configData = configService.getConfigurations();

			this.sttService = new SttService(configData);
			this.agentService = new AgentService(configData);
			this.htmlConvertService = new HtmlConvertService();


		} catch (IOException e) {

			throw new RuntimeException("error in load kitty-config.json", e);
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


	public HtmlConvertService getHtmlConvertService() {
		return htmlConvertService;
	}
}