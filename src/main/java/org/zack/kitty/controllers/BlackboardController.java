package org.zack.kitty.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BlackboardController {

	public VBox mensagensBox;

	private final List<String[]> messages = new ArrayList<>();


	public void addMessage(String author, String content) {
		messages.add(new String[] {author, content});
	}

	public List<String[]> getMessages() {
		return messages;
	}

	public void update(){
		System.out.println(messages.size());
		for(String[] message: messages){

			Label label = new Label(message[1]);

			label.getStyleClass().add(message[0]);

			label.setWrapText(true);

			mensagensBox.getChildren().add(label);
		}

	}
}