package test.core.util;

import blue.core.util.ReflectionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReflectionUtilTest
{
	public ReflectionUtilTest()
	{
	}
	
	@Test
	public void testTableToClazz()
	{
		Assertions.assertEquals("SysAdmin", ReflectionUtil.tableToClazz("sys_admin", false));
		Assertions.assertEquals("Admin", ReflectionUtil.tableToClazz("sys_admin", true));
		Assertions.assertEquals("SysAdmin", ReflectionUtil.tableToClazz("_sys_admin", false));
		Assertions.assertEquals("SysAdmin", ReflectionUtil.tableToClazz("sys_admin_", false));
		Assertions.assertEquals("SysAdminAuth", ReflectionUtil.tableToClazz("sys_admin_auth", false));
		Assertions.assertEquals("AdminAuth", ReflectionUtil.tableToClazz("sys_admin_auth", true));
	}
	
	@Test
	public void testClazzToField()
	{
		Assertions.assertEquals("sysAdmin", ReflectionUtil.clazzToField("SysAdmin"));
		Assertions.assertEquals("sys", ReflectionUtil.clazzToField("Sys"));
		Assertions.assertEquals("sysAdminAuth", ReflectionUtil.clazzToField("SysAdminAuth"));
	}
	
	@Test
	public void testFieldToClazz()
	{
		Assertions.assertEquals("SysAdmin", ReflectionUtil.fieldToClazz("sysAdmin"));
		Assertions.assertEquals("Sys", ReflectionUtil.fieldToClazz("sys"));
		Assertions.assertEquals("SysAdminAuth", ReflectionUtil.fieldToClazz("sysAdminAuth"));
	}

}


