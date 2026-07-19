package org.zack.kitty;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import org.zack.kitty.core.ExecutorsManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppView  extends Application {

	private Stage primaryStage;
	private TrayIcon trayIcon;

	@Override
	public void start(Stage stage) throws IOException {
		this.primaryStage = stage;

		final FXMLLoader fxmlLoader = new FXMLLoader(Bootstrap.class.getResource("app-view.fxml"));
		final Scene scene = new Scene(fxmlLoader.load(), 570, 650);

		final Properties props = new Properties();
		props.load(getClass().getResourceAsStream("/application.properties"));
		final String version = props.getProperty("app.version");


		stage.setTitle(String.format("Kitty %s", version));
		stage.getIcons().add(new Image(
			Objects.requireNonNull(getClass().getResourceAsStream("/org/zack/kitty/assets/icon.png"))));
		stage.setScene(scene);
		stage.show();

		Platform.setImplicitExit(false);

		setupSystemTray(version);

	}
	private void setupSystemTray(String version) {
		if (!SystemTray.isSupported()) {
			System.out.println("System tray não suportado");
			return;
		}

		java.awt.Image awtIcon = Toolkit.getDefaultToolkit()
			.getImage(getClass().getResource("/org/zack/kitty/assets/icon-64.png"));

		trayIcon = new TrayIcon(awtIcon, String.format("Kitty %s", version));


		trayIcon.addActionListener(e -> Platform.runLater(this::showStage));
		trayIcon.setImageAutoSize(true);
		PopupMenu popup = new PopupMenu();

		MenuItem itemOpen = new MenuItem("Abrir");
		itemOpen.addActionListener(e -> Platform.runLater(this::showStage));

		MenuItem itemClose = new MenuItem("Sair");
		itemClose.addActionListener(e -> {
			ExecutorsManager.INSTANCE.shutdown();
			removeTrayIcon();
			Platform.exit();
		});

		popup.add(itemOpen);
		popup.addSeparator();
		popup.add(itemClose);
		trayIcon.setPopupMenu(popup);

		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e) {
			System.err.println("Erro ao adicionar ícone na bandeja: " + e.getMessage());
		}
	}
	private void showStage() {
		if (primaryStage != null) {
			primaryStage.show();
			primaryStage.toFront();
		}
	}

	private void removeTrayIcon() {
		if (trayIcon != null && SystemTray.isSupported()) {
			SystemTray.getSystemTray().remove(trayIcon);
		}
	}
}