package com.osi.gaudi.security.authorization.helper;



public class RoleHelper {
	
	private long id;
	
	private String name;
    
	private String description;
	
	private boolean selected;
	
	private boolean adminDomain;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAdminDomain() {
		return adminDomain;
	}

	public void setAdminDomain(boolean adminDomain) {
		this.adminDomain = adminDomain;
	}
	
	

}
