/**
 * 
 */
package com.osi.gaudi.security.authorization.comparator;

import java.util.Comparator;

import javax.faces.model.SelectItem;

/**
 * @author root
 *
 */
public class SelectItemAlphabeticalComparator implements Comparator<Object> {

    /**
     * 
     */
    public SelectItemAlphabeticalComparator() {
        super();
       
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
    	SelectItem s1 = (SelectItem) o1;
    	SelectItem s2 = (SelectItem) o2;
    	if(s1 != null && s1.getLabel() != null){
    		if(s2 != null && s2.getLabel() != null){
    			String label1 = s1.getLabel().toUpperCase();
    			String label2 = s2.getLabel().toUpperCase();
    			return label1.compareTo(label2);
    		}
    	}
        return 0;
        
    }

}