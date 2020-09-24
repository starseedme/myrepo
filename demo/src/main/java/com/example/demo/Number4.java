package com.example.demo;

import java.util.List;

public class Number4 {

	private List<Attendee> members;
	private long count;
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Number4(List<Attendee> members, long count) {
		this.members = members;
		this.count = count;
	}

	public List<Attendee> getMembers() {
		return members;
	}

	public void setMembers(List<Attendee> members) {
		this.members = members;
	}
	
}
