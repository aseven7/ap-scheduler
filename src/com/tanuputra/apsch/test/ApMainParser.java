package com.tanuputra.apsch.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tanuputra.apsch.jabx.ApJobXml;
import com.tanuputra.apsch.jabx.ApJobs;

public class ApMainParser {
	public final static Logger _logger = LogManager.getLogger();
	public final static String _apFilePath = "E:/ap.xml";

	public static void checkXmlLoad() {
		JAXBContext jaxbContext;
		ApJobs apJobs = null;

		try {
			_logger.info("Loading file : " + _apFilePath);

			jaxbContext = JAXBContext.newInstance(ApJobs.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			apJobs = (ApJobs) jaxbUnmarshaller.unmarshal(new File(_apFilePath));

			_logger.info("Loading done job list " + _apFilePath);
		} catch (JAXBException e) {
			_logger.info(e.getMessage());
		}

		_logger.info(apJobs.getJob().size());

		for (ApJobXml job : apJobs.getJob()) {
			_logger.info("------------------------");
			_logger.info("ID              : " + job.getId());
			_logger.info("TITLE           : " + job.getTitle());
			_logger.info("DESCRIPTION     : " + job.getDescription());
			_logger.info("CATEGORY        : " + job.getCategory());
			_logger.info("ACTIVE          : " + job.getActive());
			_logger.info("TIME            : " + job.getTime());
			_logger.info("DAY             : " + job.getDay());
			_logger.info("DATE            : " + job.getDate());
			_logger.info("DUEDATE         : " + job.getDuedate());
			_logger.info("COMMAND         : " + job.getCommand());
			_logger.info("PRESENTCOMMAND  : " + job.getPresentCommand());
		}
	}

	public static void main(String[] args) {
		new ApMainParser();
	}

	public ApMainParser() {
		InputStream apResource = ApMainParser.class.getResourceAsStream("/ap.properties");
		Properties apProp = new Properties();
		try {
			apProp.load(apResource);
			apResource.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		// File file = new
		// File(classLoader.getResource("/config/ap.properties").getFile());
		// try {
		// BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		// String line;
		//
		// while((line = bufferedReader.readLine()) != null) {
		// _logger.info(line);
		// }
		//
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
