package test.base.core.dict;

import blue.base.core.dict.Color;
import blue.base.core.dict.Dictionary;

/**
 * @author Jin Zheng
 * @since 2019-04-21
 */
public final class Test extends Dictionary
{
	public static final Test ONE = new Test(1,  "一", Color.BLACK);
	public static final Test TWO = new Test(2,  "二", Color.BLUE);

	private Test(int index, String name, Color color)
	{
		super(index, name, color);
	}
}
