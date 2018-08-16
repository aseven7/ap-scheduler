package com.tanuputra.apsch.core;
import java.io.File;

import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.runtime.ApMain;

public class ApWatcher implements Runnable {
	// Logger
	private static Logger _logger;
	// Cancel flag
	boolean _isCanceledFlag = true;
	// Timestamps
	private static Long timestampAp;

	public ApWatcher(Logger logger) {
		_logger = logger;
	}

	public static void checkApFile() {
		String apFilePath = "E:\\ap.xml";
		// conclude AP file path
		String userFileDir = System.getProperty("user.dir");
		userFileDir += "\\ap.xml";
		userFileDir = userFileDir.replace("\\", "/");
		File file = new File(userFileDir);
		if (file.exists() && file.isFile()) {
			apFilePath = userFileDir;
		}
		
		
		File apFile = new File(apFilePath);
		if (apFile.lastModified() != ApWatcher.timestampAp) {
			ApMain.isRestarted = true;
			
			ApMain.apMainThread.interrupt();
			ApMain.apAgentThread.interrupt();
			ApMain.apWatcherThread.interrupt();
			
			ApMain.apMainThread = (new Thread(new ApMain()));
			ApMain.apMainThread.start();
		}
	}
	
	public static void setCurrentApTimestamp(Long timestampAp) {
		ApWatcher.timestampAp = timestampAp;
	}

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
