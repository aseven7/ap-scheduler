package com.tanuputra.apsch.core;
import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.jabx.ApJob;

public class ApJobInstance implements Runnable {
	private ApJob _job;
	private Thread _thread;
	private Logger _logger;
	
	public ApJobInstance(Logger logger, ApJob job) {
		_logger = logger;
		_job = job;
	}

	public void execute(String jobId) {
		ApJob job = ApAgent._apJobManager.get(jobId);
		
		if (job != null) {
			ApAgent.execute(job);
		} else {
			_logger.warn("Execution target job Id : " + jobId + " failed, Job not found !");
		}
	}
	
	@Override
	public synchronized void run() {
		final String command = _job.getCommand();
		final String presentCommand = _job.getPresentCommand();
		
		executeCommand(command);
		executeCommand(presentCommand);
		
		// execute command
		_logger.info(this + " execution has been finish ...");
	}
	
	public void executeCommand(String command) {
		_logger.info("Execution " + _job + " command : '" + command + "'");
		
		if (command != null) {
			// Check whenever this job command call another job
			if (command.startsWith("job:")) {
				final String jobId = command.replace("job:", "");
				execute(jobId);
			} else if (!command.trim().equals("")){
				// Otherwise call from command line terminal
				try {
					Process process = Runtime.getRuntime().exec(command);
					process.waitFor();
					
					// Return code
					int returnCode = process.exitValue();
					
					if (returnCode == 0) {
						_logger.info(_job + " return code = 0 (normal)");
					} else {
						_logger.warn(_job + " return code = " + returnCode + " (abnormal)");
					}
					
				} catch (IOException e) {
					_logger.error(e.getMessage());
				} catch (InterruptedException e) {
					_logger.error(e.getMessage());
				} finally {
					Runtime.getRuntime().freeMemory();
				}
			}
		}
	}

	@Override
	public String toString() {
		return "(id:" + _job.getId() + ")" + _job.getTitle();
	}
	
	public void activate() {
		_thread = new Thread(this);
		_thread.start();
	}
	
	public Thread getThread() {
		return _thread;
	}
}
