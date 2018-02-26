package com.osi.gaudi.security.authorization.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import com.osi.gaudi.security.authorization.comparator.SelectItemAlphabeticalComparator;

public class Listados {
	
	public static final int LONGITUDETIQUETAS = 100; 
	
	
	public static Map<Object, String> SelectItemListToMap(List<SelectItem> selectItems) {
		Map<Object, String> respuesta = new HashMap<Object, String>();
		for (SelectItem element : selectItems) {
			respuesta.put(element.getValue(), element.getLabel());
		}
		return respuesta;
	}
	
	@SuppressWarnings("unchecked")
	public static void ordenarSelectItemsAlfabeticamente(List<SelectItem> lista){
		
    	Collections.sort(lista, new SelectItemAlphabeticalComparator());

    	for (Iterator i = lista.iterator(); i.hasNext();) {
			SelectItem element = (SelectItem) i.next();
			element.setLabel(subStringVista(element.getLabel(), LONGITUDETIQUETAS));
		}
		
    }
	
	public static String subStringVista(String str, int max) {
		if (str == null || max < 1)
			return ".";
		String ff = (str.length() > max ? "..." : "");
		if (str.length() < max) {
			max = str.length();
		}
		return str.substring(0, max).trim() + ff;
	}

	public static List<SelectItem> MapToSelectItemList(Map<?,String> map) {
		List<SelectItem> respuesta = new ArrayList<SelectItem>();
		for (Object object : map.keySet()) {
			if (object instanceof String) {
				String key = (String) object;
				respuesta.add(new SelectItem(key,(String) map.get(key)));
			}else if (object instanceof Integer) {
				Integer key = (Integer) object;
				respuesta.add(new SelectItem(key,(String) map.get(key)));
			}else if (object instanceof Boolean) {
				Boolean key = (Boolean) object;
				respuesta.add(new SelectItem(key,(String) map.get(key)));
			}
		}
		ordenarSelectItemsAlfabeticamente(respuesta);
		return respuesta;
	}
	
	public static List<SelectItem> MapToSelectItemListNoOrden(Map<?,String> map) {
		List<SelectItem> respuesta = new ArrayList<SelectItem>();
		for (Object object : map.keySet()) {
			if (object instanceof String) {
				String key = (String) object;
				respuesta.add(new SelectItem(key,(String) map.get(key)));
			}else if (object instanceof Integer) {
				Integer key = (Integer) object;
				respuesta.add(new SelectItem(key,(String) map.get(key)));
			}else if (object instanceof Boolean) {
				Boolean key = (Boolean) object;
				respuesta.add(new SelectItem(key,(String) map.get(key)));
			}
		}
		return respuesta;
	}

}
