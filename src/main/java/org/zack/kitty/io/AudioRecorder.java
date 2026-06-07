package org.zack.kitty.io;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.zack.kitty.utils.ExecutorsManager;

public class AudioRecorder {

	private final String path;

	private final AudioFormat format;

	private TargetDataLine line;


	public AudioRecorder(String pathVoice) {
		format = new AudioFormat(44100.0f, 16, 1, true, true);
		path = pathVoice;
	}


	public String startRecord() throws LineUnavailableException {

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		line = (TargetDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();

		final String dateTime = LocalDateTime.now().toString();
		final String filePath = String.format("%s/%s.wav", path, dateTime);
		final File wavFile = new File(filePath);

		ExecutorsManager.INSTANCE.getExecutor().submit(() -> {
			try {
				AudioSystem.write(new AudioInputStream(line), AudioFileFormat.Type.WAVE, wavFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});


		return filePath;
	}


	public void stopRecord() {

		line.stop();
		line.close();
	}
}