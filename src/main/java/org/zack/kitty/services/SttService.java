package org.zack.kitty.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.zack.kitty.dto.ConfigData;

import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.model.audio.AudioTranscriptionRequest;
import dev.langchain4j.model.openai.OpenAiAudioTranscriptionModel;

public class SttService {

	private final OpenAiAudioTranscriptionModel transcriptionModel;


	public SttService(final ConfigData config) {

		transcriptionModel = OpenAiAudioTranscriptionModel.builder().baseUrl("https://api.groq.com/openai/v1")
			.apiKey(config.getSttApiKey()).modelName(config.getSttModel()).build();
	}


	public String transcript(String path) throws IOException {
		byte[] audioData = Files.readAllBytes(Path.of(path));
		Audio audio = Audio.builder().binaryData(audioData).mimeType("audio/wav").build();

		AudioTranscriptionRequest request = AudioTranscriptionRequest.builder().audio(audio).build();

		return transcriptionModel.transcribe(request).text();
	}
}