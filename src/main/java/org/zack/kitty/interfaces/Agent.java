package org.zack.kitty.interfaces;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface Agent {

	String chat(@MemoryId String conversationId, @UserMessage String userMessage);
}