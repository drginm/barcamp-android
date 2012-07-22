package com.orleonsoft.android.barcamp.network;

public class Place {
	private long identifier;
	private String description;
	private String image;
	private String name;
	private String nextUnconference;

	public Place() {
	}

	public Place(long identifier, String description, String image,
			String name, String nextUnconference) {
		this.identifier = identifier;
		this.description = description;
		this.image = image;
		this.name = name;
		this.nextUnconference = nextUnconference;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNextUnconference() {
		return nextUnconference;
	}

	public void setNextUnconference(String nextUnconference) {
		this.nextUnconference = nextUnconference;
	}

}
