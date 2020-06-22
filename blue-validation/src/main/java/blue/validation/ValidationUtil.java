package blue.validation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;


public class ValidationUtil
{
	
	private static Validator validator;
	
	public ValidationUtil()
	{
	}
	
	/**
	 * 验证对象
	 * 
	 * @param object 对象
	 * @param groups 验证分组
	 */
	public static void valid(Object object, Class<?>...groups) throws ValidationException
	{
		Assert.notNull(object, "待验证对象不能为空");
		if (groups.length == 0)
			return;

		init();
		
		Set<ConstraintViolation<Object>> set = validator.validate(object, groups);
		if (set == null || set.isEmpty())
			return;
		
		StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<Object> cv : set)
		{
			sb.append(cv.getMessage()).append(",");
		}
		if (sb.length() > 1)
			sb.delete(sb.length() - 1, sb.length());
		
		throw new ValidationException(sb.toString());
	}
	
	/**
	 * 把json转换成对象，并验证对象
	 * 
	 * @param clazz 对象类型
	 * @param json JSON字符串
	 * @param groups 验证分组
	 * @return 对象
	 */
	public static <T> T valid(Class<T> clazz, String json, Class<?>...groups) throws ValidationException
	{
		if (json == null || json.isEmpty())
			return null;
		
		Assert.notNull(clazz, "类型不能为空");
		T obj = JSON.parseObject(json, clazz);
		valid(obj, groups);
		return obj;
	}
	
	/**
	 * 把json转换成对象列表，并验证对象列表
	 * 
	 * @param clazz 对象引用类型
	 * @param json JSON字符串
	 * @param groups 验证分组
	 * @return 对象
	 */
	public static <T> List<T> validList(Class<T> clazz, String json, Class<?>...groups) throws ValidationException
	{
		if (json == null || json.isEmpty())
			return null;

		Assert.notNull(clazz, "类型不能为空");
		List<T> list = JSON.parseObject(json, new TypeReference<List<T>>(clazz){});
		if (list != null)
		{
			for (T t : list)
			{
				valid(t, groups);
			}
		}
		return list;
	}
	
	private static void init()
	{
		if (validator != null)
			return;
		
		synchronized (ValidationUtil.class)
		{
			if (validator != null)
				return;
			
			validator = Validation.buildDefaultValidatorFactory().getValidator();
		}
	}
	
	/**
	 * SpringMVC 验证结果
	 * 
	 * @param result
	 */
	public static void valid(BindingResult result) throws ValidationException
	{
		if (result == null)
			return;
		
		if (!result.hasErrors())
			return;
		
		List<FieldError> errorList = result.getFieldErrors();
		StringBuilder sb = new StringBuilder();
		for (FieldError error : errorList)
		{
			sb.append(error.getDefaultMessage()).append(",");
		}
		if (sb.length() > 1)
			sb.delete(sb.length() - 1, sb.length());
		
		throw new ValidationException(sb.toString());
	}
	
}
