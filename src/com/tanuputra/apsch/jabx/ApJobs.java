package com.tanuputra.apsch.jabx;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "apjobs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApJobs {
	@XmlElement(name = "job")
	private ArrayList<ApJobXml> jobs;
	
	public ArrayList<ApJobXml> getJob() {
		return this.jobs;
	}

	public void setJob(ArrayList<ApJobXml> job) {
		this.jobs = job;
	}
}
