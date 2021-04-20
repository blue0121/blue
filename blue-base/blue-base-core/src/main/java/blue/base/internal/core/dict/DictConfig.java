package blue.base.internal.core.dict;

import java.util.Map;

/**
 * 字典配置
 *
 * @author zhengj
 * @since 1.0 2017年1月14日
 */
public class DictConfig {
	private Class<?> clazz;
	private String name;
	private Map<Integer, Object> indexMap;
	private Map<String, Object> fieldMap;
	private Map<String, Object> nameMap;
	private Map<Object, Integer> objectMap;
	private Map<Object, String> objectFieldMap;
	private Map<Object, String> objectNameMap;

	public DictConfig() {
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, Object> getIndexMap() {
		return indexMap;
	}

	public void setIndexMap(Map<Integer, Object> indexMap) {
		this.indexMap = indexMap;
	}

	public Map<String, Object> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, Object> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<String, Object> getNameMap() {
		return nameMap;
	}

	public void setNameMap(Map<String, Object> nameMap) {
		this.nameMap = nameMap;
	}

	public Map<Object, Integer> getObjectMap() {
		return objectMap;
	}

	public void setObjectMap(Map<Object, Integer> objectMap) {
		this.objectMap = objectMap;
	}

	public Map<Object, String> getObjectFieldMap() {
		return objectFieldMap;
	}

	public void setObjectFieldMap(Map<Object, String> objectFieldMap) {
		this.objectFieldMap = objectFieldMap;
	}

	public Map<Object, String> getObjectNameMap() {
		return objectNameMap;
	}

	public void setObjectNameMap(Map<Object, String> objectNameMap) {
		this.objectNameMap = objectNameMap;
	}
}
