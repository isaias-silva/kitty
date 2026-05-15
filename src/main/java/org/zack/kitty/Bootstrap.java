package org.zack.kitty;

import java.io.IOException;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Bootstrap extends Application {

	@Override
	public void start(Stage stage) throws IOException {

		final FXMLLoader fxmlLoader = new FXMLLoader(Bootstrap.class.getResource("app-view.fxml"));
		final Scene scene = new Scene(fxmlLoader.load(), 520, 550);

		final Properties props = new Properties();
		props.load(getClass().getResourceAsStream("/application.properties"));

		final String version = props.getProperty("app.version");

		stage.setResizable(false);
		stage.setTitle(String.format("Kitty %s", version));
		stage.setScene(scene);
		stage.show();
	}


	public static void main(String[] args) {
		launch();
	}
}