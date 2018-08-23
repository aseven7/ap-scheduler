package com.tanuputra.apsch.runtime;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.tanuputra.apsch.core.ApAgent;
import com.tanuputra.apsch.core.ApHttpClientAgent;
import com.tanuputra.apsch.core.ApJobManager;
import com.tanuputra.apsch.core.ApWatcher;
import com.tanuputra.apsch.util.ApUtil;

public class ApMain implements Runnable {
	private static Logger _logger;
	private static ApJobManager _apJobManager;
	private static String _apEnv = "Development";
	public static Thread apMainThread;
	public static Thread apAgentThread;
	public static Thread apWatcherThread;
	public static boolean isRestarted = false;
	public static Properties _apProp;

	public static void main(String[] args) {
		// Running AP Main
		apMainThread = (new Thread(new ApMain()));
		apMainThread.start();
	}

	private void loadApScheduleList() {
		_logger.info("Loading Job Manager");
		_apJobManager = ApUtil.getJobManager(_logger, _apProp);
	}

	public void loadApProperties() {
		ApMain._apEnv = _apProp.getProperty("env");
	}

	public void initLogging() {
		_logger = LogManager.getLogger();
	}

	@Override
	public void run() {
		
		_apProp = ApUtil.getApProp();

		initLogging();
		
		if (ApMain.isRestarted) {
			_logger.info("AP (" + _apEnv + ") schedule re-started !");
			ApMain.isRestarted = false;
		} else {
			_logger.info("AP (" + _apEnv + ") schedule started !");
		}

		// Loading Ap Properties
		loadApProperties();

		// Loading AP Schedule List
		loadApScheduleList();

		// Running AP Agent
		apAgentThread = (new Thread(new ApAgent(_logger, _apJobManager)));
		apAgentThread.start();

		// Running AP Watcher
		apWatcherThread = (new Thread(new ApWatcher(_logger)));
		apWatcherThread.start();

		// Running Http Client
		final String ipAddr = _apProp.getProperty("ap.httpclient.ip", "8080");
		final ApHttpClientAgent apHttpAgent = new ApHttpClientAgent(ipAddr, _logger);
		final Thread apHttpClientAgent = (new Thread(apHttpAgent));
		apHttpClientAgent.start();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				apHttpAgent.stopHttp();
				_logger.info("Exit!");
			}
		}));
	}
}
