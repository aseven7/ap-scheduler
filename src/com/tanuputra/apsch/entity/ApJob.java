package com.tanuputra.apsch.entity;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tanuputra.apsch.util.ApConstants;
import com.tanuputra.apsch.util.ApUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class ApJob.
 */
public class ApJob {
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return _id;
	}

	/**
	 * Sets the id.
	 *
	 * @param _id the new id
	 */
	public void setId(String _id) {
		this._id = _id;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return _title;
	}

	/**
	 * Sets the title.
	 *
	 * @param _title the new title
	 */
	public void setTitle(String _title) {
		this._title = _title;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Sets the description.
	 *
	 * @param _description the new description
	 */
	public void setDescription(String _description) {
		this._description = _description;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return _category;
	}

	/**
	 * Sets the category.
	 *
	 * @param _category the new category
	 */
	public void setCategory(String _category) {
		this._category = _category;
	}

	/**
	 * Gets the active.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
		return _active;
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public String getTime() {
		return _time;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		if (_date.trim().equals("")) {
			// Default current date
			Calendar calendar = ApUtil.getCalendar();
			SimpleDateFormat dateFormatter = new SimpleDateFormat(ApConstants.FRMT_DT);
			_date = dateFormatter.format(calendar.getTime());
		}
		
		return _date;
	}

	/**
	 * Sets the date.
	 *
	 * @param _date the new date
	 */
	public void setDate(String _date) {
		this._date = _date;
	}

	/**
	 * Gets the duedate.
	 *
	 * @return the duedate
	 */
	public String getDuedate() {
		return _duedate;
	}

	/**
	 * Sets the duedate.
	 *
	 * @param _duedate the new duedate
	 */
	public void setDuedate(String _duedate) {
		this._duedate = _duedate;
	}

	/**
	 * Gets the command.
	 *
	 * @return the command
	 */
	public String getCommand() {
		return _command;
	}

	/**
	 * Sets the command.
	 *
	 * @param _command the new command
	 */
	public void setCommand(String _command) {
		this._command = _command;
	}

	/**
	 * Gets the present command.
	 *
	 * @return the present command
	 */
	public String getPresentCommand() {
		return _presentCommand;
	}

	/**
	 * Sets the present command.
	 *
	 * @param _presentCommand the new present command
	 */
	public void setPresentCommand(String _presentCommand) {
		this._presentCommand = _presentCommand;
	}

	/**
	 * Gets the day.
	 *
	 * @return the day
	 */
	public String getDay() {
		return _day;
	}

	/**
	 * Sets the day.
	 *
	 * @param _day the new day
	 */
	public void setDay(String _day) {
		this._day = _day;
	}
	
	/**
	 * Gets the group id.
	 *
	 * @return the group id
	 */
	public String getGroupId() {
		return _day;
	}

	/**
	 * Sets the group id.
	 *
	 * @param _groupId the new group id
	 */
	public void setGroupId(String _groupId) {
		this._groupId = _groupId;
	}

	/**
	 * Instantiates a new ap job.
	 *
	 * @param id the id
	 * @param title the title
	 * @param description the description
	 * @param category the category
	 */
	public ApJob(String id, String title, String description, String category) {
		_id = id;
		_title = title;
		_description = description;
		_category = category;
		_active = false;
	}

	/**
	 * Gets the day array.
	 *
	 * @return the day array
	 */
	public String[] getDayArray() {
		final String[] defaultDays = { "mon", "tue", "wed", "thu", "fri", "sat", "sun" };
		final String[] days = _day.trim().split(",");
		if (days.length == 0) {
			return defaultDays;
		}

		return days;
	}

	/**
	 * Gets the time array.
	 *
	 * @return the time array
	 */
	public String[] getTimeArray() {
		if(getTime() == null) {
			_time = "";
		}
		final String[] times = getTime().split(":");
		return times;
	}

	/**
	 * Gets the date array.
	 *
	 * @return the date array
	 */
	public String[] getDateArray() {
		final String[] date = getDate().split("/");
		return date;
	}

	/**
	 * Enable.
	 */
	public void enable() {
		_active = true;
	}

	/**
	 * Disable.
	 */
	public void disable() {
		_active = false;
	}

	/**
	 * Event.
	 *
	 * @param day the day
	 * @param time the time
	 * @param date the date
	 * @param dueDate the due date
	 */
	public void event(String day, String time, String date, String dueDate) {
		_day = day;
		_time = time;
		_date = date;
		_duedate = dueDate;
	}

	/**
	 * Setup next time.
	 */
	public void setupNextTime() {
		// Increment one day
		Calendar calendar = ApUtil.getCalendar();
		calendar.add(Calendar.DATE, 1);

		// Formatter Date
		SimpleDateFormat dateFormatter = new SimpleDateFormat(ApConstants.FRMT_DT);
		_date = dateFormatter.format(calendar.getTime());
	}

	/** The id. */
	private String _id;
	
	/** The title. */
	private String _title;
	
	/** The description. */
	private String _description;
	
	/** The category. */
	private String _category;
	
	/** The active. */
	private Boolean _active;
	
	/** The time. */
	private String _time;
	
	/** The day. */
	private String _day;
	
	/** The date. */
	private String _date;
	
	/** The duedate. */
	private String _duedate;
	
	/** The command. */
	private String _command;
	
	/** The present command. */
	private String _presentCommand;
	
	/** The group id. */
	private String _groupId;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(id:" + _id + ")" + _title;
	}

}
