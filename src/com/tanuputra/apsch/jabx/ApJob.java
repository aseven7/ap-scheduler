package com.tanuputra.apsch.jabx;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tanuputra.apsch.util.ApConstants;
import com.tanuputra.apsch.util.ApUtil;

public class ApJob {
	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String _title) {
		this._title = _title;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String _description) {
		this._description = _description;
	}

	public String getCategory() {
		return _category;
	}

	public void setCategory(String _category) {
		this._category = _category;
	}

	public Boolean getActive() {
		return _active;
	}

	public String getTime() {
		return _time;
	}

	public String getDate() {
		if (_date.trim().equals("")) {
			// Default current date
			Calendar calendar = ApUtil.getCalendar();
			SimpleDateFormat dateFormatter = new SimpleDateFormat(ApConstants.FRMT_DT);
			_date = dateFormatter.format(calendar.getTime());
		}
		
		return _date;
	}

	public void setDate(String _date) {
		this._date = _date;
	}

	public String getDuedate() {
		return _duedate;
	}

	public void setDuedate(String _duedate) {
		this._duedate = _duedate;
	}

	public String getCommand() {
		return _command;
	}

	public void setCommand(String _command) {
		this._command = _command;
	}

	public String getPresentCommand() {
		return _presentCommand;
	}

	public void setPresentCommand(String _presentCommand) {
		this._presentCommand = _presentCommand;
	}

	public String getDay() {
		return _day;
	}

	public void setDay(String _day) {
		this._day = _day;
	}
	
	public String getGroupId() {
		return _day;
	}

	public void setGroupId(String _groupId) {
		this._groupId = _groupId;
	}

	public ApJob(String id, String title, String description, String category) {
		_id = id;
		_title = title;
		_description = description;
		_category = category;
		_active = false;
	}

	public String[] getDayArray() {
		final String[] defaultDays = { "mon", "tue", "wed", "thu", "fri", "sat", "sun" };
		final String[] days = _day.split(",");
		if (days.length == 0) {
			return defaultDays;
		}

		return days;
	}

	public String[] getTimeArray() {
		if(getTime() == null) {
			_time = "";
		}
		final String[] times = getTime().split(":");
		return times;
	}

	public String[] getDateArray() {
		final String[] date = getDate().split("/");
		return date;
	}

	public void enable() {
		_active = true;
	}

	public void disable() {
		_active = false;
	}

	public void event(String day, String time, String date, String dueDate) {
		_day = day;
		_time = time;
		_date = date;
		_duedate = dueDate;
	}

	public void setupNextTime() {
		// Increment one day
		Calendar calendar = ApUtil.getCalendar();
		calendar.add(Calendar.DATE, 1);

		// Formatter Date
		SimpleDateFormat dateFormatter = new SimpleDateFormat(ApConstants.FRMT_DT);
		_date = dateFormatter.format(calendar.getTime());
	}

	private String _id;
	private String _title;
	private String _description;
	private String _category;
	private Boolean _active;
	private String _time;
	private String _day;
	private String _date;
	private String _duedate;
	private String _command;
	private String _presentCommand;
	private String _groupId;

	@Override
	public String toString() {
		return "(id:" + _id + ")" + _title;
	}

}
