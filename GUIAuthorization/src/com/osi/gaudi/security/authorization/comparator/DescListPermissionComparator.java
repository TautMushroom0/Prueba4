package com.osi.gaudi.security.authorization.comparator;
/**
 * Clase especializada en comparar lista generica de permisos de usuario según descripción 
 * @author    Franklin Pinto -- fapinto@colsanitas.com
 * @created   06/05/2009
 * @copyright 2009 Organización Sanitas Internacional
 * @version   1.0
 */
import java.text.Collator;
import java.util.Comparator;

import com.osi.gaudi.security.authorization.helper.PermissionHelper;

public class DescListPermissionComparator implements Comparator<PermissionHelper>{

	@Override
	public int compare(PermissionHelper str1, PermissionHelper str2) {
		Collator collator = Collator.getInstance();
		return  collator.compare(str1.getDescription().trim(),str2.getDescription().trim());
	}
}
