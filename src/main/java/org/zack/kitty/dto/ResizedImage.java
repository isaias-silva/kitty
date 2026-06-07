package org.zack.kitty.dto;



import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public record ResizedImage(String imageUrl, Image image, Rectangle2D rectangle) {

}