package org.zack.kitty.controllers;

import java.io.File;
import java.io.IOException;

import org.zack.kitty.dto.ConfigData;
import org.zack.kitty.dto.ResizedImage;
import org.zack.kitty.services.AgentService;
import org.zack.kitty.services.ConfigService;
import org.zack.kitty.services.ServiceRegistry;
import org.zack.kitty.utils.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ConfigController {

	public TextField sttKey;

	public TextField assistantName;

	public TextField sttModel;

	public TextField llmKey;

	public TextField llmModel;

	public TextArea systemPrompt;

	public ImageView profileImage;

	private ConfigData data;


	@FXML
	public void initialize(){

		data = new ConfigData();

		loadConfig();
	}


	public void onSave(ActionEvent event) throws IOException {

		data.setName(assistantName.getText());
		data.setLlmModel(llmModel.getText());
		data.setLlmApiKey(llmKey.getText());
		data.setSttModel(sttModel.getText());
		data.setSttApiKey(sttKey.getText());
		data.setSystemPrompt(systemPrompt.getText());

		ServiceRegistry.INSTANCE.getConfigService().generateConfig(data);

		final AgentService agentService = ServiceRegistry.INSTANCE.getAgentService();

		agentService.setConfig(data);
		agentService.resetAgent();

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}


	private void loadConfig(){
		ConfigService configService = ServiceRegistry.INSTANCE.getConfigService();
		try {
			data = configService.getConfigurations();
			assistantName.setText(data.getName());
			llmModel.setText(data.getLlmModel());
			llmKey.setText(data.getLlmApiKey());
			sttModel.setText(data.getSttModel());
			sttKey.setText(data.getSttApiKey());
			systemPrompt.setText(data.getSystemPrompt());
			profileImage.setImage(new Image(data.getProfilePath()));

		} catch (IOException e) {
			System.out.println(e.getMessage());

		}
	}


	public void changeProfile(final MouseEvent mouseEvent) {
		if (mouseEvent.getButton() == MouseButton.PRIMARY) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Selecionar perfil");

			fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg"));

			Stage mainStage = (Stage) assistantName.getScene().getWindow();

			File file = fileChooser.showOpenDialog(mainStage);

			if (file != null) {

				ResizedImage resizedImage = Utils.resizeImage(file);

				profileImage.setViewport(resizedImage.rectangle());

				profileImage.setImage(resizedImage.image());

				data.setProfilePath(resizedImage.imageUrl());
			}
		}
	}
}