package blue.base.core.util;

/**
 * 异常工具类
 *
 * @author zhengj
 * @since 1.0 2011-5-20
 */
public class ExceptionUtil {
	private ExceptionUtil() {
	}

	public static Throwable getRootCause(Exception e) {
		Throwable ee = e;
		while (ee != null) {
			ee = ee.getCause();
		}
		return ee;
	}

	/**
	 * 把异常栈变成字符串
	 *
	 * @param e 异常
	 * @return 字符串
	 */
	public static String exceptionToString(Throwable e) {
		return exceptionToString(e, Integer.MAX_VALUE);
	}

	/**
	 * 把异常栈变成字符串
	 *
	 * @param e     异常
	 * @param lines 异常栈最大行数，至少1行，小于1都会变成1行
	 * @return 字符串
	 */
	public static String exceptionToString(Throwable e, int lines) {
		while (e.getCause() != null) {
			e = e.getCause();
		}

		lines = (lines <= 1 ? 1 : lines);
		StringBuilder sb = new StringBuilder(4096);

		sb.append(e.getClass().getName())
				.append(" -- ")
				.append(e.getLocalizedMessage())
				.append("\n");

		StackTraceElement[] elements = e.getStackTrace();
		int i = 1;
		for (StackTraceElement element : elements) {
			sb.append("  at ")
					.append(element.getClassName())
					.append(".")
					.append(element.getMethodName())
					.append("(")
					.append(element.getFileName())
					.append(": ")
					.append(element.getLineNumber())
					.append(")\n");

			i++;
			if (i > lines) {
				break;
			}
		}

		return sb.toString();
	}
}
