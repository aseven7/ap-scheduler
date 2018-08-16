package com.tanuputra.apsch.jabx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "apjobroot")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApXml {

	@XmlElement(name = "apjobs")
	public ApJobs jobs;

	@XmlElement(name = "apjobgroups")
	public ApJobGroups jobGroups;

}
