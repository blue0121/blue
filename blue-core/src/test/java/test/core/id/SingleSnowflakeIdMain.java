package test.core.id;

import blue.core.id.SingleSnowflakeId;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhengjin
 * @since 1.0 2018年08月23日
 */
public class SingleSnowflakeIdMain
{

	public SingleSnowflakeIdMain()
	{
	}

	public static void main(String[] args)
	{
        SingleSnowflakeId snowflakeId = SingleSnowflakeId.getInstance();
		//System.out.println(snowflakeId.nextId());
		long maxCount = 1000000;
		Set<Long> set = new HashSet<>();
		System.out.println("开始");
		long start = System.currentTimeMillis();
		for (int i = 0; i < maxCount; i++)
		{
			long id = snowflakeId.nextId();
			//set.add(id);
		}
		long used = System.currentTimeMillis() - start;
		System.out.printf("用时 %d ms。每秒产生：%f 个。\n", used, (double)maxCount/used * 1000);
		System.out.printf("个数：%d，不重复个数：%d\n", maxCount, set.size());
	}

}
