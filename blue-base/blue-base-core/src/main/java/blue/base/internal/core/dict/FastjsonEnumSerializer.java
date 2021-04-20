package blue.base.internal.core.dict;

import blue.base.core.dict.DictParser;
import blue.base.core.dict.Dictionary;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author zhengjin
 * @since 1.0 2017年12月08日
 */
public class FastjsonEnumSerializer implements ObjectSerializer {
	public static final String VALUE = "value";
	public static final String FIELD = "field";
	public static final String LABEL = "label";
	public static final String COLOR = "color";

	private DictParser dictParser = DictParser.getInstance();

	public FastjsonEnumSerializer() {
	}

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
		SerializeWriter out = serializer.out;
		if (object instanceof Dictionary) {
			Dictionary dict = (Dictionary) object;
			String color = dict.getColor() == null ? "" : dict.getColor().name().toLowerCase();
			String field = dictParser.getFieldFromObject(dict);
			out.writeFieldValue('{', VALUE, dict.getIndex());
			out.writeFieldValue(',', FIELD, field);
			out.writeFieldValue(',', LABEL, dict.getName());
			out.writeFieldValue(',', COLOR, color);
			out.write('}');
		}

	}
}
