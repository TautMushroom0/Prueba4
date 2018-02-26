package com.osi.gaudi.cfg;

import java.io.Serializable;
import java.util.List;

public class CfgItem implements Serializable {
	
	private static final long serialVersionUID = -1364996897806996545L;
	public static final int SINGLE = 0;
	public static final int ARRAY = 1;
	private String name;
	private String value;
	private String description;
	private int type;
	private List<String> values;

	public CfgItem(String name, String value, String description, int type,
			List<String> values) {
		this.name = name;
		this.value = value;
		this.description = description;
		this.type = type;
		this.values = values;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	public String getDescription() {
		return this.description;
	}

	public int getType() {
		return this.type;
	}

	public List<String> getValues() {
		return this.values;
	}
}