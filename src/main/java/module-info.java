module org.zack.kitty {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.net.http;

	requires org.kordamp.bootstrapfx.core;
	requires langchain4j.open.ai;
	requires langchain4j.core;
	requires com.fasterxml.jackson.databind;
	requires java.desktop;
	requires langchain4j;

	opens org.zack.kitty to javafx.fxml;
	exports org.zack.kitty;
	exports org.zack.kitty.controllers;
	opens org.zack.kitty.controllers to javafx.fxml;
	opens org.zack.kitty.dto to com.fasterxml.jackson.databind;
}