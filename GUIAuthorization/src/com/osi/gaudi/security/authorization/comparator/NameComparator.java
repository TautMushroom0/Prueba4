package com.osi.gaudi.security.authorization.comparator;
/**
 * Clase especializada en comparar lista generica de nombres de usuarios 
 * @author    Franklin Pinto -- fapinto@colsanitas.com
 * @created   04/05/2009
 * @copyright 2009 Organización Sanitas Internacional
 * @version   1.0
 */
import java.text.Collator;
import java.util.Comparator;

import com.osi.gaudi.security.authorization.helper.UserHelper;

public class NameComparator implements Comparator<UserHelper>{

	@Override
	public int compare(UserHelper str1, UserHelper str2) {
		 Collator collator = Collator.getInstance();
		return  collator.compare(str1.getName().trim(), str2.getName().trim());
	}
}
