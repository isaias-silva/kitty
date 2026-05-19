package org.zack.kitty.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

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
			boolean endWithTime = process.waitFor(60, TimeUnit.SECONDS);
			if (!endWithTime) {
				process.destroyForcibly();
				return "Erro: O comando excedeu o limite de tempo e foi abortado.\n"
					+ "Output parcial:\n" + response.toString();
			}
			int exitCode = process.exitValue();
			if (exitCode != 0) {
				response.append("\n[exitCode: ").append(exitCode).append("]");
			}
			return response.toString();

		} catch (IOException | InterruptedException e) {
			return e.getMessage();
		}
	}
}