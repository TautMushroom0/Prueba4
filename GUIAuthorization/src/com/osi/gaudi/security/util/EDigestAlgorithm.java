package com.osi.gaudi.security.util;

public enum EDigestAlgorithm {
	SHA_256("SHA-256"), SHA_512("SHA-512"), SHA("SHA"), SHA_384("SHA-384"), SHA_1(
			"SHA1"), MD5("MD5"), MD2("MD2");

	private String name;

	private EDigestAlgorithm(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}