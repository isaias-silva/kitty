package org.zack.kitty.controllers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.zack.kitty.dto.AgentResponse;
import org.zack.kitty.interfaces.Agent;
import org.zack.kitty.services.HtmlConvertService;
import org.zack.kitty.services.ServiceRegistry;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class BlackboardController {

	public VBox contentBox;

	public WebView webView;

	private ExecutorService agentExecutor;

	private HtmlConvertService htmlConvertService;


	@FXML
	public void initialize() {
		AtomicInteger threadCount = new AtomicInteger(1);

		agentExecutor = Executors.newFixedThreadPool(2, r -> {
			Thread t = new Thread(r);
			t.setName("KittyAgent-Pool-Thread-" + threadCount.getAndIncrement());
			t.setDaemon(false);
			return t;
		});

		htmlConvertService = ServiceRegistry.INSTANCE.getHtmlConvertService();

	}


	public void sendPrompt(String message) {
		this.sendPrompt(message, "default-conversation");
	}


	public void sendPrompt(String message, String conversation) {

		final Agent agent = ServiceRegistry.INSTANCE.getAgentService().getAgent();

		final WebEngine engine = webView.getEngine();
		engine.loadContent(htmlConvertService.addHead("<p>pensando...</p>"),"text/html");

		CompletableFuture.supplyAsync(() -> agent.chat(conversation, message), agentExecutor)
			.exceptionally(ex -> new AgentResponse(ex.getMessage(), null))
			.thenApply(r -> htmlConvertService.mdToHtml(r.content().isBlank() ? r.reasoning() : r.content()))
			.thenApply(htmlConvertService::addHead)
			.thenApply(v -> Base64.getEncoder().encodeToString(v.getBytes(StandardCharsets.UTF_8))).thenAccept(
				response -> Platform.runLater(() -> engine.load("data:text/html;charset=utf-8;base64," + response)));



	}




}