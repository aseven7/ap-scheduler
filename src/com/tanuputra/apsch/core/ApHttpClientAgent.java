package com.tanuputra.apsch.core;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ApHttpClientAgent.
 */
public class ApHttpClientAgent implements Runnable {
	
	/** The logger. */
	private Logger _logger;
	
	/** The ip addr. */
	private String _ipAddr;
	
	/** The ap http client. */
	public ApHttpClient _apHttpClient;
	
	/**
	 * Instantiates a new ap http client agent.
	 *
	 * @param ipAddr the ip addr
	 * @param logger the logger
	 */
	public ApHttpClientAgent(String ipAddr, Logger logger) {
		// IP Address
		_ipAddr = ipAddr;
		// Logger
		_logger = logger;
	}
	
	/**
	 * Stop http.
	 */
	public void stopHttp() {
		// Stop thread
		_apHttpClient.stop();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public synchronized void run() {
		try {
			// Declare HTTP Client
			_apHttpClient = new ApHttpClient(Integer.valueOf(_ipAddr), _logger);
			
			// Thread loop
			while (true) {
				// Let the thread sleep for a while.
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// Logger error
			_logger.error("(Ap Http Client) has been interrupted.");
			// Stop HTTP client
			_apHttpClient.stop();
		} catch (NumberFormatException e) {
			_logger.error(e.getMessage());
		} catch (IOException e) {
			_logger.error(e.getMessage());
		}
		
	}

}
