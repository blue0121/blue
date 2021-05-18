package blue.redis.core.options;

import blue.base.core.id.IdGenerator;
import blue.base.core.util.AssertUtil;
import blue.redis.core.RedisCacheLoader;
import blue.redis.core.RedisCacheWriter;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-17
 */
public class RedisCacheOptions {
    private static final long THOUSAND = 1000;

    private String id;
    private String prefix;
    private RedisCacheMode mode = RedisCacheMode.REDIS;
    private long ttl = 10 * 60 * THOUSAND;
    private long localTtl = 50 * THOUSAND;
    private long localMaxSize = THOUSAND;
    private long timeout = 2 * THOUSAND;
    private int retry = 2;

    private RedisCacheWriteMode writeMode = RedisCacheWriteMode.SYNC;
    private RedisCacheLoader loader;
    private RedisCacheWriter writer;

	public RedisCacheOptions() {
	}

    public void check() {
        AssertUtil.notEmpty(prefix, "Key Prefix");
        AssertUtil.notNull(mode, "Redis Cache Mode");
        AssertUtil.notNull(writeMode, "Redis Cache Write Mode");
        AssertUtil.positive(ttl, "TTL");
        AssertUtil.positive(localTtl, "Local TTL");
        AssertUtil.positive(localMaxSize, "Local Max Size");
        AssertUtil.positive(timeout, "Timeout");
        AssertUtil.positive(retry, "Retry");
        if (id == null || id.isEmpty()) {
            id = mode.name() + IdGenerator.uuid12bit();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public RedisCacheOptions setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public RedisCacheMode getMode() {
        return mode;
    }

    public RedisCacheOptions setMode(RedisCacheMode mode) {
        this.mode = mode;
        return this;
    }

    public long getTtl() {
        return ttl;
    }

    public RedisCacheOptions setTtl(long ttl) {
        this.ttl = ttl;
        return this;
    }

    public long getLocalTtl() {
        return localTtl;
    }

    public RedisCacheOptions setLocalTtl(long localTtl) {
        this.localTtl = localTtl;
        return this;
    }

    public long getLocalMaxSize() {
        return localMaxSize;
    }

    public RedisCacheOptions setLocalMaxSize(long localMaxSize) {
        this.localMaxSize = localMaxSize;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public RedisCacheOptions setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public int getRetry() {
        return retry;
    }

    public RedisCacheOptions setRetry(int retry) {
        this.retry = retry;
        return this;
    }

    public RedisCacheWriteMode getWriteMode() {
        return writeMode;
    }

    public RedisCacheOptions setWriteMode(RedisCacheWriteMode writeMode) {
        this.writeMode = writeMode;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public RedisCacheLoader getLoader() {
        return loader;
    }

    @SuppressWarnings("rawtypes")
    public RedisCacheOptions setLoader(RedisCacheLoader loader) {
        this.loader = loader;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public RedisCacheWriter getWriter() {
        return writer;
    }

    @SuppressWarnings("rawtypes")
    public RedisCacheOptions setWriter(RedisCacheWriter writer) {
        this.writer = writer;
        return this;
    }
}
