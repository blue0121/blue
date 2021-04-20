package blue.base.core.dict;

import blue.base.internal.core.dict.DefaultDictParser;
import blue.base.internal.core.dict.DictConfig;

import java.util.List;
import java.util.Map;

/**
 * 字典解析器
 *
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public interface DictParser {
	/**
	 * 获取字典解析器的实例(单例)
	 *
	 * @return
	 */
	static DictParser getInstance() {
		return DefaultDictParser.getInstance();
	}

	/**
	 * 根据字典索引值获取对象
	 *
	 * @param clazz 字典类型
	 * @param index 索引值
	 * @return 对象
	 */
	<T extends Dictionary> T getFromIndex(Class<T> clazz, Integer index);

	/**
	 * 根据字典字段名称获取对象
	 *
	 * @param clazz 字典类型
	 * @param field 字段名称
	 * @return 对象
	 */
	<T extends Dictionary> T getFromField(Class<T> clazz, String field);

	/**
	 * 根据字典中文名称获取对象
	 *
	 * @param clazz 字典类型
	 * @param name  中文名称
	 * @return 对象
	 */
	<T extends Dictionary> T getFromName(Class<T> clazz, String name);

	/**
	 * 根据对象获取字典索引值
	 *
	 * @param object 对象
	 * @return 字典索引值
	 */
	Integer getFromObject(Dictionary object);

	/**
	 * 根据对象获取字典中文名称
	 *
	 * @param object 对象
	 * @return 字典中文名称
	 */
	String getNameFromObject(Dictionary object);

	/**
	 * 根据对象获取字典字段名称
	 *
	 * @param object 对象
	 * @return 字典字段名称
	 */
	String getFieldFromObject(Dictionary object);

	/**
	 * 获取所有字典键值对
	 *
	 * @return 所有字典键值对
	 */
	Map<String, Map<String, String>> getStringMap();

	/**
	 * 根据字典类型获取字典值
	 *
	 * @param clazz 字典类型: State.class, Type.class
	 * @return
	 */
	List<DictValue> getDictValue(Class<? extends Dictionary> clazz);

	/**
	 * 解析字典
	 *
	 * @param clazz enum类型
	 */
	DictConfig parse(Class<?> clazz);

}
