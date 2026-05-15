package org.zack.kitty.controllers;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import org.zack.kitty.dto.SttData;
import org.zack.kitty.io.AudioRecorder;
import org.zack.kitty.services.ServiceRegistry;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppController extends AnimationController {

	@FXML
	private TextArea inputLabel;

	@FXML
	private Button micButton;

	private boolean isListening = false;

	private final SttData sttData;

	private final AudioRecorder audioRecorder;

	private BlackboardController blackboard;

	private Stage auxStage;

	public AppController() {
		audioRecorder = new AudioRecorder("/tmp");
		sttData = new SttData();
	}


	@FXML
	public void initialize() {

		animateScale(micButton, Duration.seconds(0.4), new double[] {0.4, 0.4});
		animateFade(micButton, Duration.seconds(0.4), new double[] {1, 0.5});
	}



	@FXML
	protected void onMicClick() throws LineUnavailableException {
		if (isListening) {

			stopRecording();

		} else {
			startRecording();

		}
	}


	@FXML
	protected void onConfigClick() throws IOException {
		Stage stage = new Stage();
		stage.setTitle("Configurações");
		stage.setResizable(false);

		stage.initModality(Modality.APPLICATION_MODAL);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/zack/kitty/config-view.fxml"));

		Scene scene = new Scene(loader.load(), 400, 700);
		stage.setScene(scene);
		stage.show();
	}


	private void startRecording() throws LineUnavailableException {
		playScaleAnimation();
		playFadeAnimation();
		micButton.setText("...");
		isListening = true;
		inputLabel.getStyleClass().remove("error");
		inputLabel.setText("Estou ouvindo...");

		sttData.setAudioPath(audioRecorder.startRecord());
	}


	private void stopRecording() {


		stopScaleAnimation(micButton);
		stopFadeAnimation(micButton);

		micButton.setText("🎙️");
		isListening = false;

		inputLabel.setText("...");
		audioRecorder.stopRecord();
		try {
			sttData.setText(ServiceRegistry.INSTANCE.getSttService().transcript(sttData.getAudioPath()));
			inputLabel.setText(sttData.getText());
			openBackboard();

		} catch (Exception e) {
			System.out.println(e.getMessage());

			inputLabel.getStyleClass().add("error");
			inputLabel.setText("Configuração inválida por favor ajuste a configuração.");
		}

	}


	private void openBackboard() throws IOException {

		if (blackboard == null) {
			Stage mainStage = (Stage) micButton.getScene().getWindow();

			auxStage = new Stage();
			auxStage.setResizable(false);
			auxStage.initModality(Modality.NONE);

			auxStage.initOwner(mainStage);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/zack/kitty/blackboard-view.fxml"));
			Parent root = loader.load();
			blackboard = loader.getController();

			Scene scene = new Scene(root, 700, 400);

			auxStage.setScene(scene);

			auxStage.setOnHidden(event -> blackboard = null);
			auxStage.show();
		}
		blackboard.addMessage("me", sttData.getText());
		blackboard.update();

	}
}