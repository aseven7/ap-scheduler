package com.tanuputra.apsch.jabx;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "apjobgroups")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApJobGroups {

	@XmlElement(name = "jobgroup")
	private ArrayList<ApJobGroupXml> jobGroups;

	public ArrayList<ApJobGroupXml> getJobGroup() {
		return this.jobGroups;
	}

	public void setJobGroup(ArrayList<ApJobGroupXml> jobGroup) {
		this.jobGroups = jobGroup;
	}

}
