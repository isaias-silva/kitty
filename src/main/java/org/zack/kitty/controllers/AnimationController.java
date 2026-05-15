package org.zack.kitty.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Labeled;
import javafx.util.Duration;

public abstract class AnimationController {

	private ScaleTransition scaleAnimation;
	private FadeTransition fadeAnimation;


	protected void animateScale(Labeled lb, Duration duration, double[] scale) {
		scaleAnimation = new ScaleTransition(duration, lb);
		scaleAnimation.setByX(scale[0]);
		scaleAnimation.setByY(scale[1]);
		scaleAnimation.setCycleCount(ScaleTransition.INDEFINITE);
		scaleAnimation.setAutoReverse(true);
	}

	protected void playScaleAnimation(){
		scaleAnimation.play();
	}
	protected void stopScaleAnimation(Labeled lb){
		scaleAnimation.stop();
		lb.setScaleX(1.0);
		lb.setScaleY(1.0);
	}



	protected void animateFade(Labeled lb, Duration duration, double[] variations) {
		fadeAnimation  = new FadeTransition(duration, lb);
		fadeAnimation.setFromValue(variations[1]);
		fadeAnimation.setToValue(variations[0]);
		fadeAnimation.setCycleCount(ScaleTransition.INDEFINITE);
		fadeAnimation.setAutoReverse(true);
	}

	protected void playFadeAnimation(){
		fadeAnimation.play();
	}
	protected void stopFadeAnimation(Labeled lb){
		fadeAnimation.stop();
		lb.setOpacity(1.0);


	}
}