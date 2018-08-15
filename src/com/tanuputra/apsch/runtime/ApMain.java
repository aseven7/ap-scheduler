package com.tanuputra.apsch.runtime;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

public class ApMain implements Runnable {
	private static final Logger _logger = LogManager.getLogger();
	private static ApJobManager _apJobManager;
	private static String _apFilePath = "E:/ap.xml";
	private static String _apEnv = "Development";
	public static Thread apMainThread;
	public static Thread apAgentThread;
	public static Thread apWatcherThread;
	public static boolean isRestarted = false;

	public static void main(String[] args) {
		// Running AP Main
		apMainThread = (new Thread(new ApMain()));
		apMainThread.start();
	}

	private void loadApScheduleList() {
		_logger.info("Loading Job Manager");
		_apJobManager = new ApJobManager(_logger);
		
		// open AP file located
		JAXBContext jaxbContext;
		ApXml apXml = null;

		try {
			_logger.info("Loading file : " + _apFilePath);

			jaxbContext = JAXBContext.newInstance(ApXml.class);
			File apFile = new File(_apFilePath);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			apXml = (ApXml) jaxbUnmarshaller.unmarshal(apFile);
			ApWatcher.setCurrentApTimestamp(apFile.lastModified());

			_logger.info("Loading done job list " + _apFilePath);
		} catch (JAXBException e) {
			_logger.info(e.getMessage());
		}

		for (ApJobXml job : apXml.jobs.getJob()) {
			// create AP job
			ApJob jobtest = new ApJob(job.getId(), job.getTitle(), job.getDescription(), job.getCategory());
			jobtest.event(job.getDay(), job.getTime(), job.getDate(), job.getDuedate());
			jobtest.setCommand(job.getCommand());
			jobtest.setPresentCommand(job.getPresentCommand());
			if (job.getActive() != null && job.getActive() == true) {
				jobtest.enable();
			} else {
				jobtest.disable();
			}
			_apJobManager.add(jobtest);
		}
	}
	
	public void loadApProperties() {
		InputStream apResource = getClass().getResourceAsStream("/ap.properties");
		Properties apProp = new Properties();
		
		System.out.println("load properties : " + _apFilePath);
		try {
			apProp.load(apResource);
			
			ApMain._apFilePath = apProp.getProperty("jobpath");
			ApMain._apEnv = apProp.getProperty("env");
			
			apResource.close();
		} catch (IOException e) {
			_logger.error(e.getMessage());
		}
	}

	@Override
	public void run() {
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
