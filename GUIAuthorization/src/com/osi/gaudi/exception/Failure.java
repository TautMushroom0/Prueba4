package com.osi.gaudi.exception;

import java.util.List;

public class Failure extends RuntimeException {
	private static final long serialVersionUID = 1024837989318535795L;
	private String code;
	private List<String> fixes;

	public Failure(String code, String message, Exception cause) {
		super(message, cause);
		this.code = code;
	}

	public Failure(String code, String message, List<String> fixes,
			Exception cause) {
		super(message, cause);
		this.code = code;
		this.fixes = fixes;
	}

	public Failure(String code, String message, List<String> fixes) {
		super(message);
		this.code = code;
		this.fixes = fixes;
	}

	public String getCode() {
		return this.code;
	}

	public List<String> getFixes() {
		return this.fixes;
	}
}