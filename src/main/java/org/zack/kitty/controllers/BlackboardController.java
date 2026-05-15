package org.zack.kitty.controllers;

import org.zack.kitty.interfaces.Agent;
import org.zack.kitty.services.ServiceRegistry;

import javafx.scene.layout.VBox;
import one.jpro.platform.mdfx.MarkdownView;

public class BlackboardController {

	public VBox contentBox;


	public void sendPrompt(String message) {

		Agent agent = ServiceRegistry.INSTANCE.getAgentService().getAgent();


		String response = agent.chat("default-conversation", message);

		MarkdownView mdView = new MarkdownView(response);
		mdView.getStylesheets().add(getClass().getResource("/org/zack/kitty/styles/main.css").toExternalForm());

		mdView.setMaxWidth(Double.MAX_VALUE);
		contentBox.getChildren().setAll(mdView);
	}
}