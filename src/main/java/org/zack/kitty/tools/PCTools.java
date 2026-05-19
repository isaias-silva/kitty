package org.zack.kitty.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class PCTools {



	@Tool("verifica o sistema operacional da máquina")
	public String getSystemInfo() {
		return System.getProperty("os.name");
	}


	@Tool("Executa comandos no sistema e retorna o resultado")
	public String executeCommand(@P("shell do sistema operacional") String shell,
		@P("parâmetros do shell do sistema operacional") String shellParams,
		@P("comando a ser executado pelo shell") String command) {

		ProcessBuilder builder = new ProcessBuilder(shell, shellParams, command);
		builder.redirectErrorStream(true);

		try {

			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			StringBuilder response = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				response.append(line).append("\n");
			}
			int exitCode = process.waitFor();
			response.append(String.format("\nexitCode:%s", exitCode));

			return response.toString();
		} catch (IOException | InterruptedException e) {
			return e.getMessage();
		}
	}
}