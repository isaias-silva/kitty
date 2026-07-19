package org.zack.kitty.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.zack.kitty.core.BaseNode;
import org.zack.kitty.core.annotations.InjectNode;
import org.zack.kitty.core.annotations.Node;
import org.zack.kitty.dto.ConfigData;

import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.model.audio.AudioTranscriptionRequest;
import dev.langchain4j.model.openai.OpenAiAudioTranscriptionModel;

@Node
public class SttService extends BaseNode {



	@InjectNode
	private ConfigService configService;


	public String transcript(String path) throws IOException {

		ConfigData config = configService.getConfigurations();

		OpenAiAudioTranscriptionModel transcriptionModel = OpenAiAudioTranscriptionModel.builder().baseUrl("https://api.groq.com/openai/v1")
			.apiKey(config.getSttApiKey()).modelName(config.getSttModel()).build();

		byte[] audioData = Files.readAllBytes(Path.of(path));
		Audio audio = Audio.builder().binaryData(audioData).mimeType("audio/wav").build();

		AudioTranscriptionRequest request = AudioTranscriptionRequest.builder().audio(audio).build();

		return transcriptionModel.transcribe(request).text();
	}
}