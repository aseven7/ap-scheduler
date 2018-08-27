package com.tanuputra.apsch.core;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.entity.ApJob;
import com.tanuputra.apsch.util.ApConstants;
import com.tanuputra.apsch.util.ApUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class ApAgent.
 */
public class ApAgent implements Runnable {
	
	/** The logger. */
	// Logger
	private static Logger _logger;
	
	/** The is canceled flag. */
	// Cancel flag
	boolean _isCanceledFlag = true;
	
	/** The calendar. */
	// Calendar
	Calendar _calendar;
	
	/** The simple date format. */
	// SimpleDateFormat
	SimpleDateFormat _simpleDateFormat;
	
	/** The current date time. */
	// CurrentDateTime
	String _currentDateTime = "";
	
	/** The parsed date time. */
	// ParsedDateTime array
	String[] _parsedDateTime = {};
	
	/** The ap job manager. */
	// JobManager
	public static ApJobManager _apJobManager;
	
	/**
	 * Execute.
	 *
	 * @param job the job
	 */
	public static void execute(ApJob job) {
		// Create job instance
		ApJobInstance instance = new ApJobInstance(_logger, job);
		_logger.info("Job instances " + instance + " is being started ");
		instance.activate();
	}

	/**
	 * Instantiates a new ap agent.
	 *
	 * @param logger the logger
	 * @param apJobManager the ap job manager
	 */
	public ApAgent(Logger logger, ApJobManager apJobManager) {
		// Logger
		_logger = logger;
		
		// Job Manager
		_apJobManager = apJobManager;
		
		// Date format
		_simpleDateFormat = new SimpleDateFormat(ApConstants.FRMT_DTTM_AP_JOB);
		
		_logger.info("Ap Agent just started !");
	}
	
	/**
	 * Gather date time.
	 */
	private void gatherDateTime() {
		// Calendar
		_calendar = ApUtil.getCalendar();
		// Current DateTime
		_currentDateTime = _simpleDateFormat.format(_calendar.getTime());
		// Parse to Array
		_parsedDateTime = _currentDateTime.split(" ");
	}
	
	/**
	 * Check avail job.
	 */
	private void checkAvailJob() {
		// Watch job running
		final String[] current = _currentDateTime.split(" ");
		boolean activate = false;
		ApJob job = null;
		
		// Loop
		for(String id : _apJobManager.keySet()) {
			// ApJob
			job = _apJobManager.get(id);
			
			// Check job running
			if (job.getActive() && job.getTimeArray().length == 2) {
				// Day flag
				Boolean dayFlg = false;
				
				String[] dayArray = job.getDayArray();
				String[] timeArray = job.getTimeArray();
				String[] dateArray = job.getDateArray();
				
				String currentDay = current[0];
				
				// Day judge
				for(String day : dayArray) {
					if (currentDay.contains(day)) {
						dayFlg = true;
					}
				}
				
				// Judge: day, time, and date
				if (dayFlg && timeArray[0].equals(current[4]) && timeArray[1].equals(current[5])) {
					// date
					if(dateArray[0].equals(current[1]) && dateArray[1].equals(current[2]) && dateArray[2].equals(current[3])) {
						activate = true;
					}
				}

				// Check job judge result
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

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			// While not canceled job agent
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
	
	/**
	 * Checks if is canceled.
	 *
	 * @return true, if is canceled
	 */
	private boolean isCanceled() {
		return _isCanceledFlag;
	}
}
