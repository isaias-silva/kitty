package org.zack.kitty.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.zack.kitty.core.ContextManager;
import org.zack.kitty.dto.ConfigData;
import org.zack.kitty.dto.ResizedImage;
import org.zack.kitty.services.ConfigService;
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
	public void initialize() throws Exception {

		data = new ConfigData();

		loadConfig();
	}


	public void onSave(ActionEvent event) throws Exception {

		data.setName(assistantName.getText());
		data.setLlmModel(llmModel.getText());
		data.setLlmApiKey(llmKey.getText());
		data.setSttModel(sttModel.getText());
		data.setSttApiKey(sttKey.getText());
		data.setSystemPrompt(systemPrompt.getText());

		getConfigService().generateConfig(data);

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}



	private void loadConfig() throws Exception {

		data = getConfigService().getConfigurations();
			assistantName.setText(data.getName());
			llmModel.setText(data.getLlmModel());
			llmKey.setText(data.getLlmApiKey());
			sttModel.setText(data.getSttModel());
			sttKey.setText(data.getSttApiKey());
			systemPrompt.setText(data.getSystemPrompt());

			if(Files.exists(Path.of(data.getProfilePath().replace("file:/","/"))))
				profileImage.setImage(new Image(data.getProfilePath()));


	}

	private ConfigService getConfigService() throws Exception {
		return ContextManager.Context.getNode(ConfigService.class);
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