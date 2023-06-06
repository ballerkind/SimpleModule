package de.ballerkind.simplemodule.core.logging;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LogManager {

	private Logger logger;
	private SimpleDateFormat sdf;

	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_BLACK = "\u001B[30m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";

	public LogManager() {

		this.sdf = new SimpleDateFormat("HH:mm:ss dd.MM.yy");
		this.logger = Logger.getLogger("SimpleModule");
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);

		try {
			Formatter fileFormatter = new Formatter() {
				@Override
				public String format(LogRecord record) {
					return "[" + sdf.format(new Date()) + " " + record.getLevel().getName() + "] » " + record.getMessage() + "\n";
				}
			};
			Formatter consoleFormatter = new Formatter() {
				@Override
				public String format(LogRecord record) {
					String color;
					switch(record.getLevel().getName()) {
						case "INFO":
							color = ANSI_GREEN;
							break;
						case "WARNING":
							color = ANSI_YELLOW;
							break;
						case "SEVERE":
							color = ANSI_RED;
							break;
						default:
							color = ANSI_PURPLE;
							break;
					}

					return color + "[" + sdf.format(new Date()) + " " + record.getLevel().getName() + "]" + ANSI_RESET + " » " + record.getMessage() + "\n";
				}
			};

			File logDir = new File("logs/"); logDir.mkdir();
			FileHandler fileHandler = new FileHandler("logs/log-%g.txt", 5242880,100,false);
			fileHandler.setFormatter(fileFormatter);

			ConsoleHandler consoleHandler = new ConsoleHandler() {
				@Override
				protected synchronized void setOutputStream(OutputStream out) throws SecurityException {
					super.setOutputStream(System.out);
				}
			};
			consoleHandler.setLevel(Level.ALL);
			consoleHandler.setFormatter(consoleFormatter);

			logger.addHandler(fileHandler);
			logger.addHandler(consoleHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("");
		logger.info("────────────────────────────────────────────────────────────────────────────────");
		logger.info("          _____ _                 _      __  __           _       _");
		logger.info("         / ____(_)               | |    |  \\/  |         | |     | |");
		logger.info("        | (___  _ _ __ ___  _ __ | | ___| \\  / | ___   __| |_   _| | ___");
		logger.info("         \\___ \\| | '_ ` _ \\| '_ \\| |/ _ \\ |\\/| |/ _ \\ / _` | | | | |/ _ \\");
		logger.info("         ____) | | | | | | | |_) | |  __/ |  | | (_) | (_| | |_| | |  __/");
		logger.info("        |_____/|_|_| |_| |_| .__/|_|\\___|_|  |_|\\___/ \\__,_|\\__,_|_|\\___|");
		logger.info("                           | |");
		logger.info("                           |_|");
		logger.info("────────────────────────────────────────────────────────────────────────────────");
		logger.info("");

//		logger.severe("1");
//		logger.warning("2");
//		logger.info("3");
//		logger.config("4");
//		logger.fine("5");
//		logger.finer("6");
//		logger.finest("7");

	}

	public Logger getLogger() {
		return logger;
	}

}
