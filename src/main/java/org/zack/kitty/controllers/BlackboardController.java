package org.zack.kitty.controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


	@FXML
	public void initialize() {
		agentExecutor = Executors.newSingleThreadExecutor();
	}


	public void sendPrompt(String message) {
		this.sendPrompt(message, "default-conversation");
	}


	public void sendPrompt(String message, String conversation) {

		final Agent agent = ServiceRegistry.INSTANCE.getAgentService().getAgent();

		final WebEngine engine = webView.getEngine();

		final HtmlConvertService htmlConvertService = ServiceRegistry.INSTANCE.getHtmlConvertService();

		CompletableFuture.supplyAsync(() -> agent.chat(conversation, message), agentExecutor)
			.thenApply(htmlConvertService::mdToHtml).thenApply(htmlConvertService::addHead)
			.thenAccept(response -> Platform.runLater(() -> engine.loadContent(response)));

		engine.loadContent(htmlConvertService.addHead("<p>pensando</p>"));


	}

}