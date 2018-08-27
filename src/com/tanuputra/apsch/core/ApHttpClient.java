package com.tanuputra.apsch.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.tanuputra.apsch.entity.ApEntityJobJSON;
import com.tanuputra.apsch.logger.ApListAppender;
import com.tanuputra.apsch.runtime.ApMain;
import com.tanuputra.apsch.util.ApConstants;
import com.tanuputra.apsch.util.ApUtil;

import fi.iki.elonen.NanoHTTPD;

// TODO: Auto-generated Javadoc
/**
 * The Class ApHttpClient.
 */
public class ApHttpClient extends NanoHTTPD {
	
	/** The logger. */
	private Logger _logger;
	
	/** The ap prop. */
	private Properties _apProp;

	/**
	 * Instantiates a new ap http client.
	 *
	 * @param ipAddr the ip addr
	 * @param logger the logger
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ApHttpClient(int ipAddr, Logger logger) throws IOException {
		super(ipAddr);
		// Logger
		_logger = logger;
		// AP Properties
		_apProp = ApUtil.getApProp();
		// Start HTTP Server
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		
		_logger.info("Ap Http Client starting :" + ipAddr);
	}

	/* (non-Javadoc)
	 * @see fi.iki.elonen.NanoHTTPD#serve(fi.iki.elonen.NanoHTTPD.IHTTPSession)
	 */
	@Override
	public Response serve(IHTTPSession session) {
		// Google GSON
		final Gson gson = new Gson();
		// Print log every routing
		if (_apProp.getProperty("ap.httpclient.log", "false").equals("true")) {
			_logger.info("ApHttpClient access : " + session.getUri());
		}

		try {
			// Request URI
			final String sessionUri = session.getUri();
			
			// Request URI: /
			if (sessionUri.equals("/")) {
				return newFixedLengthResponse(viewIndex());
				// Request URI: /api/logging.json 
			} else if (sessionUri.equals("/api/logging.json")) {
				final String result = gson.toJson(ApListAppender.events);
				return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", result);
				// Request URI: /api/job.json
			} else if (sessionUri.equals("/api/job.json")) {
				ApEntityJobJSON apJobJSON = ApUtil.getJobJSON(_logger, _apProp);
				final String result = gson.toJson(apJobJSON.jobs);
				return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/json", result);
				// Request URI: /api/jobgroup.json
			} else if (sessionUri.equals("/api/jobgroup.json")) {
				ApEntityJobJSON apJobJSON = ApUtil.getJobJSON(_logger, _apProp);
				final String result = gson.toJson(apJobJSON.jobGroups);
				return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/json", result);
				// Request URI: /api/config.json
			} else if (sessionUri.equals("/api/config.json")) {
				_apProp = ApUtil.getApProp();
				final String result  = gson.toJson(_apProp.entrySet());
				return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/json", result);
			}
		} catch (IOException e) {
			final String errorMsg = "Error opening, Please see console log !";
			return newFixedLengthResponse(errorMsg);
		}

		return newFixedLengthResponse("Not found...");
	}

	/**
	 * View index.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String viewIndex() throws IOException {
		// Content
		StringBuilder content = new StringBuilder();
		// htmStream
		InputStream htmStream = ApMain.class.getResourceAsStream("/template/client.htm");
		// bufferedReader
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(htmStream));
		// line
		String line = null;
		
		// Template content
		String env = _apProp.getProperty("ap.env", ApConstants.DEFAULT_ENV);
		
		// Fetching line HTM files
		while ((line = bufferedReader.readLine()) != null) {
			line = line.replace("{env}", env);
			content.append(line + "\n");
		}
		// Close input stream for HTML file
		htmStream.close();
		// Close buffered reader
		bufferedReader.close();

		// Return
		return content.toString();
	}
}
