package org.zack.kitty.services;

import java.util.concurrent.atomic.AtomicReference;

import org.zack.kitty.core.BaseNode;
import org.zack.kitty.core.annotations.InjectNode;
import org.zack.kitty.core.annotations.Node;
import org.zack.kitty.core.annotations.OnCreate;
import org.zack.kitty.dto.ConfigData;
import org.zack.kitty.interfaces.Agent;
import org.zack.kitty.tools.PCTools;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

@Node
public class AgentService extends BaseNode {

	private final AtomicReference<Agent> agentRef = new AtomicReference<>();

	@InjectNode
	private ConfigService configService;


	@OnCreate
	private void init() {
		resetAgent();
	}


	private Agent buildAgent() {
		try {
			ConfigData configData = configService.getConfigurations();

		OpenAiChatModel model = OpenAiChatModel.builder().baseUrl("https://api.groq.com/openai/v1")
			.apiKey(configData.getLlmApiKey())
			.logRequests(true)
			.logResponses(true)
			.modelName(configData.getLlmModel()).build();

		return AiServices.builder(Agent.class).chatModel(model)
			.systemMessage(this.makeSystemPrompt(configData.getName(), configData.getSystemPrompt()))
			.chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
				.id(memoryId)
				.maxMessages(15).build()).tools(new PCTools()).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


	public Agent getAgent() {
		return agentRef.get();
	}


	public void resetAgent() {
		final Agent agent = buildAgent();
		agentRef.set(agent);
	}


	private String makeSystemPrompt(String agentName, String configSystemPrompt) {

		return String.format("""
			seu nome é %s
			%s
			""", agentName, configSystemPrompt);
	}

}