module org.zack.kitty {
	requires org.slf4j;
	requires org.kordamp.bootstrapfx.core;
	requires langchain4j.open.ai;
	requires langchain4j.core;
	requires com.fasterxml.jackson.databind;
	requires langchain4j;

	requires javafx.fxml;
	requires flexmark;
	requires flexmark.util;
	requires javafx.web;
	requires java.desktop;
	requires flexmark.ext.tables;
	requires flexmark.ext.gfm.strikethrough;
	requires flexmark.util.data;
	requires flexmark.util.misc;
	requires flexmark.ext.emoji;

	opens org.zack.kitty to javafx.fxml;
	exports org.zack.kitty;
	exports org.zack.kitty.controllers;
	opens org.zack.kitty.controllers to javafx.fxml;
	opens org.zack.kitty.dto to com.fasterxml.jackson.databind;
}