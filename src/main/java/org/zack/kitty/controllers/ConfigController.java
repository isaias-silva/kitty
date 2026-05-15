package org.zack.kitty.controllers;

import java.io.IOException;

import org.zack.kitty.dto.ConfigData;
import org.zack.kitty.services.ConfigService;
import org.zack.kitty.services.ServiceRegistry;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConfigController {

	public TextField sttKey;

	public TextField assistantName;

	public TextField sttModel;

	public TextField llmKey;

	public TextField llmModel;

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

		ServiceRegistry.INSTANCE.getConfigService().generateConfig(data);

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

		} catch (IOException e) {
			System.out.println(e.getMessage());

		}
	}
}