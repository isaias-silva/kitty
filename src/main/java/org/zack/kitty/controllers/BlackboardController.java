package org.zack.kitty.controllers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.zack.kitty.interfaces.Agent;
import org.zack.kitty.services.ServiceRegistry;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;


public class BlackboardController {

	public VBox contentBox;

	public TextArea textArea;

	private ExecutorService agentExecutor;


	@FXML
	public void initialize() {
		textArea.setWrapText(true);
		textArea.setEditable(false);

		agentExecutor = Executors.newSingleThreadExecutor();
	}


	public void sendPrompt(String message) {
		this.sendPrompt(message, "default-conversation");
	}


	public void sendPrompt(String message, String conversation) {

		Agent agent = ServiceRegistry.INSTANCE.getAgentService().getAgent();

		textArea.setText("pensando...");

		agentExecutor.execute(() -> {
			String response = agent.chat(conversation, message);
			textArea.setText(response.replace("\r\n", "\n"));
		});

	}
}