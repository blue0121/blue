package blue.base.core.dict;

/**
 * @author Jin Zheng
 * @since 2020-09-06
 */
public interface DictValue extends Comparable<DictValue> {

	Integer getValue();

	String getField();

	String getLabel();

}
