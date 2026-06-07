package org.zack.kitty.interfaces;

import org.zack.kitty.dto.AgentResponse;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface Agent {

	AgentResponse chat(@MemoryId String conversationId, @UserMessage String userMessage);
}