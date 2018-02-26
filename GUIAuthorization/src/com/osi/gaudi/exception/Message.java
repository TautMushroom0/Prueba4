package com.osi.gaudi.exception;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = -1087814327845112073L;
	private String code;
	private String message;

	public Message(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}