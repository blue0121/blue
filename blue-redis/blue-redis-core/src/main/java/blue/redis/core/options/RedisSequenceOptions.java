package blue.redis.core.options;

import blue.base.core.id.IdGenerator;
import blue.base.core.util.AssertUtil;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-17
 */
public class RedisSequenceOptions {
    private String id;
    private String key;
    private RedisSequenceMode mode = RedisSequenceMode.ATOMIC;
    private int length;
    private String prefix;
    private String suffix;

	public RedisSequenceOptions() {
	}

	public void check() {
        AssertUtil.notEmpty(key, "Redis Key");
        AssertUtil.notNull(mode, "Redis Sequence Mode");
        AssertUtil.positive(length, "Length");
        if (prefix == null) {
            prefix = "";
        }
        if (suffix == null) {
            suffix = "";
        }
        if (id == null || id.isEmpty()) {
            id = mode.name() + IdGenerator.uuid12bit();
        }
    }

    public int valueLength() {
        int len = length - prefix.length() - suffix.length();
        return Math.max(0, len);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public RedisSequenceOptions setKey(String key) {
        this.key = key;
        return this;
    }

    public RedisSequenceMode getMode() {
        return mode;
    }

    public RedisSequenceOptions setMode(RedisSequenceMode mode) {
        this.mode = mode;
        return this;
    }

    public int getLength() {
        return length;
    }

    public RedisSequenceOptions setLength(int length) {
        this.length = length;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public RedisSequenceOptions setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public RedisSequenceOptions setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }
}
