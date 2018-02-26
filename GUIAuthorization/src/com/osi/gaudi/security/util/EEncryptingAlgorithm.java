package com.osi.gaudi.security.util;

public enum EEncryptingAlgorithm {
	BLOWFISH("Blowfish"), RIJNDAEL("Rijndael"), ARCFOUR("ARCFOUR"), RC2("RC2"), RC4(
			"RC4"), AES("AES");

	private String name;

	private EEncryptingAlgorithm(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}