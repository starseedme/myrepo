package com.example.demo;

public class Attendee {
	
	private String NAME;
	private String CLASS;
	private String BATCH;
	
	public Attendee(String NAME, String CLASS, String BATCH) {
		this.NAME = NAME;
		this.CLASS = CLASS;
		this.BATCH = BATCH;
	}

	public String getName() {
		return NAME;
	}

	public void setName(String NAME) {
		this.NAME = NAME;
	}

	public String getCLASS() {
		return CLASS;
	}

	public void setClassOrdinal(String CLASS) {
		this.CLASS = CLASS;
	}

	public String getBatch() {
		return BATCH;
	}

	public void setBatch(String BATCH) {
		this.BATCH = BATCH;
	}

	@Override
	public String toString() {
		return "Attendee: [NAME=" + NAME + ", CLASS=" + CLASS + ", BATCH=" + BATCH + "]";
	}
	
	
}
