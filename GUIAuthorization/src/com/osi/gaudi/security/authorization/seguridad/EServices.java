package com.osi.gaudi.security.authorization.seguridad;

public enum EServices {
	Authentication("Authentication/local", "Authentication/remote"),
	Authorization("Authorization/local", "Authorization/remote");
	
	private String localName;
	private String remoteName;
	
	private EServices(String localName, String remoteName){
		this.localName = localName;
		this.remoteName = remoteName;		
	}
	
	public String getLocalName() {
		return localName;
	}

	public String getRemoteName() {
		return remoteName;
	}
}
