package com.osi.gaudi.exception;

import java.util.List;

public class FailureMessage extends Message {
	private static final long serialVersionUID = -7361261478813689597L;
	private List<String> fixes;

	public FailureMessage(String code, String msg, List<String> fixes) {
		super(code, msg);
		this.fixes = fixes;
	}

	public List<String> getFixes() {
		return this.fixes;
	}
}
