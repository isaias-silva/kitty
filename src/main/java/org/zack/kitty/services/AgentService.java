package org.zack.kitty.services;

import org.zack.kitty.dto.ConfigData;
import org.zack.kitty.interfaces.Agent;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class AgentService {

	private final Agent agent;


	public AgentService(ConfigData configData) {

		OpenAiChatModel model = OpenAiChatModel.builder().baseUrl("https://api.groq.com/openai/v1")
			.apiKey(configData.getLlmApiKey()).modelName(configData.getLlmModel()).build();

		agent = AiServices.builder(Agent.class).chatModel(model)
			.chatMemory(MessageWindowChatMemory.withMaxMessages(10))
			.tools().build();
	}


	public Agent getAgent() {
		return agent;
	}
}