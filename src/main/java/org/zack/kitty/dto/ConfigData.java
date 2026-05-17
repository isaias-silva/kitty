package org.zack.kitty.dto;

public class ConfigData {

	private String name;

	private String sttApiKey;

	private String sttModel;

	private String llmApiKey;

	private String llmModel;

	private String systemPrompt;

	public ConfigData() {}


	public String getName() {
		return name;
	}


	public void setName(final String name) {
		this.name = name;
	}


	public String getSttApiKey() {
		return sttApiKey;
	}


	public void setSttApiKey(final String sttApiKey) {
		this.sttApiKey = sttApiKey;
	}


	public String getSttModel() {
		return sttModel;
	}


	public void setSttModel(final String sttModel) {
		this.sttModel = sttModel;
	}


	public String getLlmApiKey() {
		return llmApiKey;
	}


	public void setLlmApiKey(final String llmApiKey) {
		this.llmApiKey = llmApiKey;
	}


	public String getLlmModel() {
		return llmModel;
	}


	public void setLlmModel(final String llmModel) {
		this.llmModel = llmModel;
	}


	public String getSystemPrompt() {
		return systemPrompt;
	}


	public void setSystemPrompt(final String systemPrompt) {
		this.systemPrompt = systemPrompt;
	}
}