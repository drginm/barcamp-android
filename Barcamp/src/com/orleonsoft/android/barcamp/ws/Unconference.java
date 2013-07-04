package com.orleonsoft.android.barcamp.ws;

public class Unconference {
	private long identifier;
	private String description;
	private String endTime;
	private String keywords;
	private String name;
	private long place;
	private String schedule;
	private Long scheduleId;
	private String speakers;
	private String startTime;
	private String namePlace;

	public Unconference() {
	}

	public Unconference(long identifier, String description, String endTime,
			String keywords, String name, long place, String schedule,
			Long scheduleId, String speakers, String startTime, String namePlace) {
		super();
		this.identifier = identifier;
		this.description = description;
		this.endTime = endTime;
		this.keywords = keywords;
		this.name = name;
		this.place = place;
		this.schedule = schedule;
		this.scheduleId = scheduleId;
		this.speakers = speakers;
		this.startTime = startTime;
		this.namePlace = namePlace;
	}

	public long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(long identifier) {
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPlace() {
		return place;
	}

	public void setPlace(long place) {
		this.place = place;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getSpeakers() {
		return speakers;
	}

	public void setSpeakers(String speakers) {
		this.speakers = speakers;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getNamePlace() {
		return namePlace;
	}

	public void setNamePlace(String namePlace) {
		this.namePlace = namePlace;
	}

}
