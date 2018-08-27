package com.tanuputra.apsch.runtime;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.core.ApAgent;
import com.tanuputra.apsch.core.ApHttpClientAgent;
import com.tanuputra.apsch.core.ApJobManager;
import com.tanuputra.apsch.core.ApWatcher;
import com.tanuputra.apsch.util.ApUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class ApMain.
 */
public class ApMain implements Runnable {
	
	/** The logger. */
	private static Logger _logger;
	
	/** The ap job manager. */
	private static ApJobManager _apJobManager;
	
	/** The ap main thread. */
	public static Thread _apMainThread;
	
	/** The ap agent thread. */
	public static Thread _apAgentThread;
	
	/** The ap watcher thread. */
	public static Thread _apWatcherThread;
	
	/** The is restarted. */
	public static boolean _isRestarted = false;
	
	/** The ap prop. */
	public static Properties _apProp;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// Running AP Main
		_apMainThread = (new Thread(new ApMain()));
		_apMainThread.start();
	}

	/**
	 * Load ap schedule list.
	 */
	private static void loadApScheduleList() {
		_logger.info("Loading Job Manager");
		// Job manager
		_apJobManager = ApUtil.getJobManager(_logger, _apProp);
	}

	/**
	 * Inits the logging.
	 */
	public void initLogging() {
		// Logger
		_logger = LogManager.getLogger();
	}
	
	/**
	 * Restart ap agent.
	 */
	public synchronized static void restartApAgent() {
		// Interrupt agent thread
		_apAgentThread.interrupt();
		
		// Loading AP Schedule List
		loadApScheduleList();
		
		// Restart by create new agent thread
		_apAgentThread = (new Thread(new ApAgent(_logger, _apJobManager)));
		_apAgentThread.start();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// AP properties
		_apProp = ApUtil.getApProp();

		// Initialize logging service
		initLogging();
		
		// Loading AP Schedule List
		loadApScheduleList();

		// Running thread AP Agent
		_apAgentThread = (new Thread(new ApAgent(_logger, _apJobManager)));
		_apAgentThread.start();

		// Running thread AP Watcher
		_apWatcherThread = (new Thread(new ApWatcher(_logger)));
		_apWatcherThread.start();

		// Running thread for HTTP Client
		final String ipAddr = _apProp.getProperty("ap.httpclient.ip", "8080");
		final ApHttpClientAgent apHttpAgent = new ApHttpClientAgent(ipAddr, _logger);
		final Thread apHttpClientAgent = (new Thread(apHttpAgent));
		apHttpClientAgent.start();

		// Runtime in case application stop
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				_logger.info("Exit HTTP Client Agent");
				// Stop HTTP Agent
				apHttpAgent.stopHttp();
			}
		}));
	}
}
