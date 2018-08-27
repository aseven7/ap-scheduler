package com.tanuputra.apsch.core;
import java.io.IOException;

import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.entity.ApJob;

// TODO: Auto-generated Javadoc
/**
 * The Class ApJobInstance.
 */
public class ApJobInstance implements Runnable {
	
	/** The job. */
	private ApJob _job;
	
	/** The thread. */
	private Thread _thread;
	
	/** The logger. */
	private Logger _logger;
	
	/**
	 * Instantiates a new ap job instance.
	 *
	 * @param logger the logger
	 * @param job the job
	 */
	public ApJobInstance(Logger logger, ApJob job) {
		// Logger
		_logger = logger;
		// Job
		_job = job;
	}

	/**
	 * Execute.
	 *
	 * @param jobId the job id
	 */
	public void execute(String jobId) {
		// Gather job
		ApJob job = ApAgent._apJobManager.get(jobId);
		
		// Check job not empty
		if (job != null) {
			// Execute job agent target job
			ApAgent.execute(job);
		} else {
			// Else warn job
			_logger.warn("Execution target job Id : " + jobId + " failed, Job not found !");
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public synchronized void run() {
		// Command
		final String command = _job.getCommand();
		// Present Command
		final String presentCommand = _job.getPresentCommand();
		
		// Execute command : Command
		executeCommand(command);
		// Execute command : Present command
		executeCommand(presentCommand);
		
		// execute command
		_logger.info(this + " execution has been finish ...");
	}
	
	/**
	 * Execute command.
	 *
	 * @param command the command
	 */
	public void executeCommand(String command) {
		// Logger information
		_logger.info("Execution " + _job + " command : '" + command + "'");
		
		// Check command not empty
		if (command != null) {
			// Check whenever this job command call another job
			if (command.startsWith("job:")) {
				final String jobId = command.replace("job:", "");
				execute(jobId);
			} else if (!command.trim().equals("")){
				// Otherwise call from command line terminal
				try {
					// Declare process
					Process process = Runtime.getRuntime().exec(command);
					process.waitFor();
					
					// Return code
					int returnCode = process.exitValue();
					
					// Check return code
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
					// Free memory
					Runtime.getRuntime().freeMemory();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(id:" + _job.getId() + ")" + _job.getTitle();
	}
	
	/**
	 * Activate.
	 */
	public void activate() {
		// Activate thread
		_thread = new Thread(this);
		// Start thread
		_thread.start();
	}
	
	/**
	 * Gets the thread.
	 *
	 * @return the thread
	 */
	public Thread getThread() {
		return _thread;
	}
}
