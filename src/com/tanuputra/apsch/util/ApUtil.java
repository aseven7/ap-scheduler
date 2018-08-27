package com.tanuputra.apsch.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.tanuputra.apsch.core.ApJobManager;
import com.tanuputra.apsch.entity.ApJob;
import com.tanuputra.apsch.entity.ApEntityJobJSON;
import com.tanuputra.apsch.entity.ApEntityJob;
import com.tanuputra.apsch.runtime.ApMain;

// TODO: Auto-generated Javadoc
/**
 * The Class ApUtil.
 */
public class ApUtil {
	
	/**
	 * Gets the calendar.
	 *
	 * @return the calendar
	 */
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		return calendar;
	}

	/**
	 * Gets the ap prop.
	 *
	 * @return the ap prop
	 */
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

	/**
	 * Gets the job JSON.
	 *
	 * @param logger the logger
	 * @param apProp the ap prop
	 * @return the job JSON
	 */
	public static ApEntityJobJSON getJobJSON(Logger logger, Properties apProp) {
		final Gson gson = new Gson();

		final String jobFilePath = apProp.getProperty("ap.jobpath");
		ApEntityJobJSON apJobJSON = null;
		try {
			apJobJSON = gson.fromJson(new FileReader(jobFilePath), ApEntityJobJSON.class);
		} catch (JsonSyntaxException e) {
			logger.error(e.getMessage());
		} catch (JsonIOException e) {
			logger.error(e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}

		return apJobJSON;
	}

	/**
	 * Gets the job manager.
	 *
	 * @param logger the logger
	 * @param apProp the ap prop
	 * @return the job manager
	 */
	public static ApJobManager getJobManager(Logger logger, Properties apProp) {
		ApJobManager jobManager = new ApJobManager(logger);

		ApEntityJobJSON apJobJSON = getJobJSON(logger, apProp);

		for (ApEntityJob job : apJobJSON.jobs) {
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

		return jobManager;
	}

	/**
	 * Gets the method get.
	 *
	 * @param name the name
	 * @return the method get
	 */
	public static String getMethodGet(String name) {
		return "get" + name.trim().substring(0, 1).toUpperCase() + name.trim().substring(1);
	}
}
