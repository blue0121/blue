package blue.http;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jin Zheng
 * @since 2020-01-11
 */
public interface HttpServer
{
	/**
	 * 启动服务器
	 *
	 * @throws Exception
	 */
	void start() throws Exception;

	/**
	 * 启动服务器
	 *
	 * @param start 开始时间戳
	 * @throws Exception
	 */
	void start(long start) throws Exception;

	/**
	 * 启动应用服务器
	 * @param springs Classpath下的spring配置文件
	 * @throws Exception
	 */
	static void run(String... springs) throws Exception
	{
		long start = System.currentTimeMillis();
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(springs);
		HttpServer httpServer = ctx.getBean(HttpServer.class);
		httpServer.start(start);
		ctx.close();
	}
}
