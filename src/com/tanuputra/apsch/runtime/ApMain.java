package com.tanuputra.apsch.runtime;
import java.io.File;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.core.ApAgent;
import com.tanuputra.apsch.core.ApJobManager;
import com.tanuputra.apsch.core.ApWatcher;
import com.tanuputra.apsch.jabx.ApJob;
import com.tanuputra.apsch.jabx.ApJobXml;
import com.tanuputra.apsch.jabx.ApXml;
import com.tanuputra.apsch.util.ApUtil;

public class ApMain implements Runnable {
	private static final Logger _logger = LogManager.getLogger();
	private static ApJobManager _apJobManager;
	private static String _apFilePath = "E:/ap.xml";
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
		ApMain._apFilePath = _apProp.getProperty("jobpath");
		ApMain._apEnv = _apProp.getProperty("env");
	}

	@Override
	public void run() {
		_apProp = ApUtil.getApProp();
		
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
	}
}
