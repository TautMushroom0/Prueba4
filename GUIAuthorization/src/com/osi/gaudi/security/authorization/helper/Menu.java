package com.osi.gaudi.security.authorization.helper;

public class Menu {
	
	private String label;
	private String parametro;
	private boolean selected;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Menu(String label,String parametro){
		this.label = label;
		this.parametro = parametro;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getParametro() {
		return parametro;
	}
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
}
