package com.tanuputra.apsch.core;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

public class ApHttpClientAgent implements Runnable {
	private Logger _logger;
	private String _ipAddr;
	public ApHttpClient _apHttpClient;
	
	public ApHttpClientAgent(String ipAddr, Logger logger) {
		_ipAddr = ipAddr;
		_logger = logger;
	}
	
	public void stopHttp() {
		_apHttpClient.stop();
	}

	@Override
	public synchronized void run() {
		try {
			_apHttpClient = new ApHttpClient(Integer.valueOf(_ipAddr), _logger);
			while (true) {
				// Let the thread sleep for a while.
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			_logger.error("(Ap Http Client) has been interrupted.");
			_apHttpClient.stop();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
