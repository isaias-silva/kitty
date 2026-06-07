package org.zack.kitty.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.zack.kitty.dto.AgentResponse;
import org.zack.kitty.interfaces.Agent;
import org.zack.kitty.services.HtmlConvertService;
import org.zack.kitty.services.ServiceRegistry;
import org.zack.kitty.utils.ExecutorsManager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class BlackboardController {

	public VBox contentBox;

	public WebView webView;

	private HtmlConvertService htmlConvertService;

	private HashMap<String, String> conversations = new HashMap<>();

	@FXML
	public void initialize() {

		htmlConvertService = ServiceRegistry.INSTANCE.getHtmlConvertService();

	}


	public void sendPrompt(String message) {
		this.sendPrompt(message, "default-conversation");
	}


	public void sendPrompt(String message, String conversation) {

		final Agent agent = ServiceRegistry.INSTANCE.getAgentService().getAgent();

		final WebEngine engine = webView.getEngine();

		String chatContent = conversations.get(conversation);
		String messageContent = String.format("<p class=\"me\">%s</p>", message);
		if (chatContent != null) {

			messageContent = chatContent + "\n" + messageContent;

		}
		conversations.put(conversation, messageContent);

		engine.loadContent(htmlConvertService.addHead(messageContent), "text/html");

		CompletableFuture.supplyAsync(() -> agent.chat(conversation, message), ExecutorsManager.INSTANCE.getExecutor())
			.exceptionally(ex -> new AgentResponse(ex.getMessage(), null))
			.thenApply(r -> htmlConvertService.mdToHtml(r.content().isBlank() ? r.reasoning() : r.content()))
			.thenApply(convertedResponse -> {
				String oldContent = conversations.get(conversation);
				String newContent = oldContent + "\n" + String.format("<div class=\"agent\"> %s </div>", convertedResponse);

				conversations.put(conversation, newContent);
				return newContent;
			})
			.thenApply(htmlConvertService::addHead)
			.thenApply(v -> Base64.getEncoder().encodeToString(v.getBytes(StandardCharsets.UTF_8))).thenAccept(
				response -> Platform.runLater(() -> engine.load("data:text/html;charset=utf-8;base64," + response)));


	}




}