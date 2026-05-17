package org.zack.kitty.services;

import java.util.concurrent.atomic.AtomicReference;

import org.zack.kitty.dto.ConfigData;
import org.zack.kitty.interfaces.Agent;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class AgentService {

	private final AtomicReference<Agent> agentRef = new AtomicReference<>();

	private ConfigData config;

	public AgentService(ConfigData configData) {

		config = configData;
		final Agent agent = buildAgent(config);
		agentRef.set(agent);
	}


	private Agent buildAgent(final ConfigData configData) {

		OpenAiChatModel model = OpenAiChatModel.builder().baseUrl("https://api.groq.com/openai/v1")
			.apiKey(configData.getLlmApiKey()).modelName(configData.getLlmModel()).build();

		return AiServices.builder(Agent.class).chatModel(model)
			.systemMessage(this.makeSystemPrompt(configData.getName(), configData.getSystemPrompt()))
			.chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
				.id(memoryId)
				.maxMessages(10)
				.build())
			.tools()
			.build();

	}


	public Agent getAgent() {
		return agentRef.get();
	}


	public void resetAgent() {
		final Agent agent = buildAgent(config);
		agentRef.set(agent);
	}


	public void setConfig(final ConfigData config) {
		this.config = config;
	}


	private String makeSystemPrompt(String agentName, String configSystemPrompt) {

		return String.format("""
			seu nome é %s
			%s
			""", agentName, configSystemPrompt);
	}

}