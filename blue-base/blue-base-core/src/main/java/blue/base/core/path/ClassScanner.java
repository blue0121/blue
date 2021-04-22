package blue.base.core.path;

import blue.base.core.util.AssertUtil;
import blue.base.internal.core.path.DefaultClassScanner;

import java.util.List;

/**
 * 类遍历
 *
 * @author Jin Zheng
 * @since 1.0 2021-01-15
 */
public interface ClassScanner {
	/**
	 * 创建类遍历器
	 *
	 * @param handler 类处理器不能为空
	 * @return
	 */
	static ClassScanner create(ClassHandler handler) {
		return new DefaultClassScanner(handler);
	}

	/**
	 * 扫描包及子包下的类文件
	 *
	 * @param pkg 包
	 */
	default void scan(String pkg) {
		this.scan(true, pkg);
	}

	/**
	 * 扫描包下的类文件
	 *
	 * @param isRecursive 是否扫描子包，true为扫描子包，false不扫描子包
	 * @param pkg         包
	 */
	void scan(boolean isRecursive, String pkg);

	/**
	 * 扫描包下的类文件
	 *
	 * @param packageList 包列表
	 */
	default void scan(List<String> packageList) {
		AssertUtil.notEmpty(packageList, "扫描包列表");
		packageList.forEach(e -> this.scan(true, e));
	}

	/**
	 * 扫描包下的类文件
	 *
	 * @param isRecursive 是否扫描子包，true为扫描子包，false不扫描子包
	 * @param packageList 包列表
	 */
	default void scan(boolean isRecursive, List<String> packageList) {
		AssertUtil.notEmpty(packageList, "扫描包列表");
		packageList.forEach(e -> this.scan(isRecursive, e));
	}
}
