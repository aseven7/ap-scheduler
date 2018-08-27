package com.tanuputra.apsch.core;

import java.io.File;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.runtime.ApMain;
import com.tanuputra.apsch.util.ApUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class ApWatcher.
 */
public class ApWatcher implements Runnable {
	
	/** The logger. */
	// Logger
	private static Logger _logger;
	
	/** The is canceled flag. */
	// Cancel flag
	boolean _isCanceledFlag = true;
	
	/** The timestamp ap. */
	// Time stamp
	private static Long timestampAp = 0L;
	
	/** The ap prop. */
	// Time stamp
	private static Properties _apProp;

	/**
	 * Instantiates a new ap watcher.
	 *
	 * @param logger the logger
	 */
	public ApWatcher(Logger logger) {
		// Logger
		_logger = logger;
		
		// Properties
		_apProp = ApUtil.getApProp();

		// Ap File Path
		String apFilePath = _apProp.getProperty("ap.jobpath");

		// File path
		File apFile = new File(apFilePath);

		// Check modified file
		if(apFile.exists() && apFile != null) {
			setCurrentApTimestamp(apFile.lastModified());
		}
	}

	/**
	 * Check ap file.
	 */
	public static void checkApFile() {
		// Ap File Path
		String apFilePath = _apProp.getProperty("ap.jobpath");

		// File path
		File apFile = new File(apFilePath);
		// Check modified file
		if(apFile.exists() && apFile != null) {
			if (apFile.lastModified() != timestampAp) {
				// Reload time stamp
				timestampAp = apFile.lastModified();
				// Restart agent
				ApMain.restartApAgent();
			}
		}
	}

	/**
	 * Sets the current ap timestamp.
	 *
	 * @param timestampAp the new current ap timestamp
	 */
	public static void setCurrentApTimestamp(Long timestampAp) {
		// Time stamp
		ApWatcher.timestampAp = timestampAp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (_isCanceledFlag) {
				// watch file changed
				ApWatcher.checkApFile();

				// Let the thread sleep for a while.
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			_logger.error("(Ap Watcher) has been interrupted.");
		}

	}

}
