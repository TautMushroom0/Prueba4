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

import com.osi.gaudi.security.authorization.entity.Permission;

public class DescriptionPermissionComparator implements Comparator<Permission>{

	@Override
	public int compare(Permission str1, Permission str2) {
		Collator collator = Collator.getInstance();
		return  collator.compare(str1.getDescription().trim(),str2.getDescription().trim());
	}
}
