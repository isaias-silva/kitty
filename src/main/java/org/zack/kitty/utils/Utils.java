package org.zack.kitty.utils;

import java.io.File;

import org.zack.kitty.dto.ResizedImage;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Utils {

	public static ResizedImage resizeImage(File file) {

		String profileUrl = file.toURI().toString();
		Image image = new Image(profileUrl);
		double size = Math.min(image.getWidth(), image.getHeight());
		double x = (image.getWidth() - size) / 2;
		double y = (image.getHeight() - size) / 2;

		return new ResizedImage(profileUrl, new Image(profileUrl), new Rectangle2D(x, y, size, size));
	}

}