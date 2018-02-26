package com.osi.gaudi.security.authorization.comparator;

import java.util.Comparator;

import com.osi.gaudi.security.authorization.entity.GroupPermission;

public class GroupPermissionComparator implements Comparator<GroupPermission>{

	@Override
	public int compare(GroupPermission o1, GroupPermission o2) {
		return o1.getName().compareTo(o2.getName());
	}
}
