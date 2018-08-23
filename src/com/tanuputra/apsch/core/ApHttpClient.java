package com.tanuputra.apsch.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;

import com.google.gson.Gson;
import com.tanuputra.apsch.jabx.ApXml;
import com.tanuputra.apsch.logger.ApListAppender;
import com.tanuputra.apsch.runtime.ApMain;
import com.tanuputra.apsch.util.ApUtil;

import fi.iki.elonen.NanoHTTPD;

public class ApHttpClient extends NanoHTTPD {
	private Logger _logger;
	private Properties _apProp;

	public ApHttpClient(int ipAddr, Logger logger) throws IOException {
		super(ipAddr);
		_apProp = ApUtil.getApProp();

		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		_logger = logger;
		_logger.info("Ap Http Client starting :" + ipAddr);
	}

	@Override
	public Response serve(IHTTPSession session) {
		final Gson gson = new Gson();
		// Print log every routing
		if (_apProp.getProperty("ap.httpclient.log", "false").equals("true")) {
			_logger.info("ApHttpClient access : " + session.getUri());
		}

		try {
			final String sessionUri = session.getUri();
			if (sessionUri.equals("/")) {
				return newFixedLengthResponse(viewIndex());
			} else if (sessionUri.equals("/api/logging.json")) {
				final String result = gson.toJson(ApListAppender.events);
				return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/plain", result);
			} else if (sessionUri.equals("/api/job.json")) {
				ApXml apXml = ApUtil.getJobXML(_logger, _apProp);
				final String result = gson.toJson(apXml.jobs);
				return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/json", result);
			}
		} catch (IOException e) {
			final String errorMsg = "Error opening, Please see console log !";
			return newFixedLengthResponse(errorMsg);
		}

		return newFixedLengthResponse("Not found...");
	}

	public String viewIndex() throws IOException {
		StringBuilder content = new StringBuilder();
		InputStream apHtmFile = ApMain.class.getResourceAsStream("/template/client.htm");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(apHtmFile));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			content.append(line + "\n");
		}
		apHtmFile.close();
		bufferedReader.close();

		return content.toString();
	}
}
