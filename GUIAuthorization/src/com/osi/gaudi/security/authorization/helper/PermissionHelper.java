package com.osi.gaudi.security.authorization.helper;


public class PermissionHelper {
	
	private long id;
	
	private boolean selected;
	
	private String name;
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	private Domain domain;
//
//	private GroupPermission groupPermission;
	
	

}
