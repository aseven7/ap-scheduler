package com.tanuputra.apsch.core;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.entity.ApJob;

// TODO: Auto-generated Javadoc
/**
 * The Class ApJobManager.
 */
public class ApJobManager extends HashMap<String, ApJob> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The logger. */
	private Logger _logger;

	/**
	 * Instantiates a new ap job manager.
	 *
	 * @param logger the logger
	 */
	public ApJobManager(Logger logger) {
		// Logger
		_logger = logger;
	}

	/**
	 * Adds the.
	 *
	 * @param job the job
	 */
	public void add(ApJob job) {
		// Add job
		this.put(job.getId(), job);
		
		// Logger debug added job
		_logger.debug("Registering job instance : " + job);
	}
}
