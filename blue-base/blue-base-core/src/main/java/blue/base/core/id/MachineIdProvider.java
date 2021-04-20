package blue.base.core.id;

import blue.base.core.util.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 机器ID配置
 *
 * @author zhengjin
 * @since 1.0 2018年09月12日
 */
public class MachineIdProvider {
	private static Logger logger = LoggerFactory.getLogger(MachineIdProvider.class);

	private Integer defaultId = 0;
	private Map<String, Integer> machineIdMap = new HashMap<>();

	/**
	 * 创建机器ID配置
	 */
	public MachineIdProvider() {
	}

	/**
	 * 创建机器ID配置
	 *
	 * @param classpath 类路径下的配置
	 */
	public MachineIdProvider(String classpath) {
		InputStream is = MachineIdProvider.class.getResourceAsStream(classpath);
		if (is == null) {
			throw new IllegalArgumentException("文件不存在：" + classpath);
		}

		this.read(is);
	}

	/**
	 * 获取机器ID
	 *
	 * @return
	 */
	public Integer getMachineId() {
		Set<String> ipList = NetworkUtil.getAllAddress();
		for (String ip : ipList) {
			Integer id = machineIdMap.get(ip);
			if (id != null) {
				return id;
			}
		}
		return defaultId;
	}

	/**
	 * 获取 IP => 机器ID 配置
	 *
	 * @return
	 */
	public Map<String, Integer> getMachineIdMap() {
		return machineIdMap;
	}

	/**
	 * 添加 IP=>机器ID
	 *
	 * @param ip
	 * @param machineId
	 */
	public void put(String ip, Integer machineId) {
		machineIdMap.put(ip, machineId);
	}

	/**
	 * 读取配置文件
	 *
	 * @param file
	 */
	public void read(File file) {
		try {
			InputStream is = new FileInputStream(file);
			this.read(is);
		}
		catch (FileNotFoundException e) {
			throw new IllegalArgumentException("文件不存在：" + file.getPath());
		}
	}

	/**
	 * 读取配置文件
	 *
	 * @param is
	 */
	public void read(InputStream is) {
		try (InputStream iis = is) {
			Scanner scanner = new Scanner(iis, StandardCharsets.UTF_8);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line == null || line.isEmpty() || line.startsWith("#")) {
					continue;
				}

				String[] keyValue = line.split("=");
				if (keyValue.length != 2) {
					continue;
				}

				machineIdMap.put(keyValue[0], Integer.valueOf(keyValue[1]));
			}
		}
		catch (IOException e) {
			throw new IllegalArgumentException("读取文件流出错");
		}
	}

}
