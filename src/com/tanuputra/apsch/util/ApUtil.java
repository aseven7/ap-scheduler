package com.tanuputra.apsch.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.core.ApJobManager;
import com.tanuputra.apsch.core.ApWatcher;
import com.tanuputra.apsch.jabx.ApJob;
import com.tanuputra.apsch.jabx.ApJobXml;
import com.tanuputra.apsch.jabx.ApXml;
import com.tanuputra.apsch.runtime.ApMain;

public class ApUtil {
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		return calendar;
	}

	public static Properties getApProp() {
		InputStream apResource = ApMain.class.getResourceAsStream("/ap.properties");
		Properties apProp = new Properties();

		try {
			apProp.load(apResource);
			apResource.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return apProp;
	}

	public static ApXml getJobXML(Logger logger, Properties apProp) {
		final String jobFilePath = apProp.getProperty("ap.jobpath");
		ApXml apXml = null;

		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(ApXml.class);
			File apFile = new File(jobFilePath);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			apXml = (ApXml) jaxbUnmarshaller.unmarshal(apFile);
			ApWatcher.setCurrentApTimestamp(apFile.lastModified());

		} catch (JAXBException e) {
			logger.info(e.getMessage());
		}
		
		return apXml;
	}

	public static ApJobManager getJobManager(Logger logger, Properties apProp) {
		ApJobManager jobManager = new ApJobManager(logger);
		final String jobFilePath = apProp.getProperty("ap.jobpath");

		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(ApXml.class);
			File apFile = new File(jobFilePath);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final ApXml apXml = (ApXml) jaxbUnmarshaller.unmarshal(apFile);
			ApWatcher.setCurrentApTimestamp(apFile.lastModified());

			for (ApJobXml job : apXml.jobs.getJob()) {
				// create AP job
				ApJob newJob = new ApJob(job.getId(), job.getTitle(), job.getDescription(), job.getCategory());
				newJob.event(job.getDay(), job.getTime(), job.getDate(), job.getDuedate());
				newJob.setCommand(job.getCommand());
				newJob.setPresentCommand(job.getPresentCommand());
				if (job.getActive() != null && job.getActive() == true) {
					newJob.enable();
				} else {
					newJob.disable();
				}
				jobManager.add(newJob);
			}
		} catch (JAXBException e) {
			logger.info(e.getMessage());
		}

		return jobManager;
	}

	public static String getMethodGet(String name) {
		return "get" + name.trim().substring(0, 1).toUpperCase() + name.trim().substring(1);
	}
}
