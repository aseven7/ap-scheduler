package com.tanuputra.apsch.core;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.jabx.ApJob;

public class ApJobManager extends HashMap<String, ApJob> {
	private static final long serialVersionUID = 1L;
	private Logger _logger;

	public ApJobManager(Logger logger) {
		_logger = logger;
	}

	public void add(ApJob job) {
		this.put(job.getId(), job);
		_logger.debug("Registering job instance : " + job);
	}
}
