package org.zack.kitty.services;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class HtmlConvertService {

	private final Parser parser;

	private final HtmlRenderer renderer;

	private static final Logger log = LoggerFactory.getLogger(HtmlConvertService.class);


	public HtmlConvertService() {
		MutableDataSet options = new MutableDataSet();

		options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));


		options.set(TablesExtension.WITH_CAPTION, false);
		options.set(TablesExtension.COLUMN_SPANS, false);
		options.set(TablesExtension.MIN_HEADER_ROWS, 1);
		options.set(TablesExtension.MAX_HEADER_ROWS, 1);
		options.set(TablesExtension.APPEND_MISSING_COLUMNS, true);
		options.set(TablesExtension.DISCARD_EXTRA_COLUMNS, true);
		options.set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true);

		this.parser = Parser.builder(options).build();
		this.renderer = HtmlRenderer.builder(options).build();
	}


	public String mdToHtml(String markdown) {
		return renderer.render(parser.parse(markdown));

	}


	public String addHead(String html) {
		return """
			<!DOCTYPE html>
			<html lang="pt">
			<head>
			    <meta charset="UTF-8">
			    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			
			    <style>
			        %s
			    </style>
			</head>
			<body>
			%s
		
			</body>
			</html>
			""".formatted(getStyle(), html);
	}


	private String getStyle() {
		try {

			URL styleUrl = getClass().getResource("/org/zack/kitty/styles/blackboard.css");
			assert styleUrl != null;
			return Files.readString(Path.of(styleUrl.getFile()));
		} catch (IOException e) {
			log.error("Error in get style:", e);
			return "";
		}
	}

}