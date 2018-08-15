package com.tanuputra.apsch.core;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.jabx.ApJob;
import com.tanuputra.apsch.util.ApConstants;
import com.tanuputra.apsch.util.ApUtil;

public class ApAgent implements Runnable {
	// Logger
	private static Logger _logger;
	// Cancel flag
	boolean _isCanceledFlag = true;
	// Calendar
	Calendar _calendar;
	// SimpleDateFormat
	SimpleDateFormat _simpleDateFormat;
	// CurrentDateTime
	String _currentDateTime = "";
	// ParsedDateTime array
	String[] _parsedDateTime = {};
	// JobManager
	public static ApJobManager _apJobManager;
	
	public static void execute(ApJob job) {
		// Create job instance
		ApJobInstance instance = new ApJobInstance(_logger, job);
		_logger.info("Job instances " + instance + " is being started ");
		instance.activate();
	}

	public ApAgent(Logger logger, ApJobManager apJobManager) {
		// Logger
		_logger = logger;
		
		// Job Manager
		_apJobManager = apJobManager;
		
		// mon 2018 08 13 16 45 48
		_simpleDateFormat = new SimpleDateFormat(ApConstants.FRMT_DTTM_AP_JOB);
		_logger.info("Ap Agent just started !");
	}
	
	private void gatherDateTime() {
		_calendar = ApUtil.getCalendar();
		_currentDateTime = _simpleDateFormat.format(_calendar.getTime());
		_parsedDateTime = _currentDateTime.split(" ");
	}
	
	private void checkAvailJob() {
		// Watch job running
		final String[] current = _currentDateTime.split(" ");
		boolean activate = false;
		ApJob job = null;
		
		for(String id : _apJobManager.keySet()) {
			job = _apJobManager.get(id);
			
			// Check job running
			if (job.getActive() && job.getTimeArray().length == 2) {
				Boolean dayFlg = false;
				
				String[] dayArray = job.getDayArray();
				String[] timeArray = job.getTimeArray();
				String[] dateArray = job.getDateArray();
				
				String currentDay = current[0];
				
				// day judge
				for(String day : dayArray) {
					if (currentDay.contains(day)) {
						dayFlg = true;
					}
				}
				
				// judge: day, time, and date
				if (dayFlg && timeArray[0].equals(current[4]) && timeArray[1].equals(current[5])) {
					// date
					if(dateArray[0].equals(current[1]) && dateArray[1].equals(current[2]) && dateArray[2].equals(current[3])) {
						activate = true;
					}
				}

				if (activate && job != null) {
					// Create job instance
					ApAgent.execute(job);
					
					// Setup for next schedule
					job.setupNextTime();
					
					// Update job manager
					_apJobManager.put(job.getId(), job);
					_logger.info(job + " setup for next time running at " + job.getDate() + " " + job.getTime());
					
					// Execute job
					activate  = false;
					job = null;
				}
			}
			
		}
		
	}

	@Override
	public void run() {
		try {
			while (isCanceled()) {
				gatherDateTime();
				
				// Watch job instance 
				checkAvailJob();

				// Let the thread sleep for a while.
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			_logger.error("(Ap Agent) has been interrupted.");
		}
		
	}
	
	private boolean isCanceled() {
		return _isCanceledFlag;
	}
}
