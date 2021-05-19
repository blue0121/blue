package blue.redis.internal.core.client;

import blue.base.core.util.StringUtil;
import blue.redis.core.options.RedisClientOptions;
import org.redisson.client.codec.Codec;
import org.redisson.config.BaseConfig;
import org.redisson.config.Config;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-12
 */
public class RedissonConfig {
    private static final int MIN_SUBSCRIPTION_CONNECTION_POOL_SIZE = 2;

    private final RedisClientOptions options;

    public RedissonConfig(RedisClientOptions options) {
        this.options = options;
    }

    public Config getConfig() {
        Config config = new Config();
        if (options.getCodec() != null) {
            config.setCodec((Codec) options.getCodec());
        }

        switch (options.getMode()) {
            case SINGLE:
                this.setSingleConfig(config);
                break;
            case SENTINEL:
                this.setSentinelConfig(config);
                break;
            case CLUSTER:
                this.setClusterConfig(config);
                break;
            default:
                throw new UnsupportedOperationException("RedisConnectionMode is null");
        }
        return config;
    }

    private void setSingleConfig(Config config) {
        var singleConfig = config.useSingleServer().setAddress(options.getBroker());
        if (options.getDatabase() > 0) {
            singleConfig.setDatabase(options.getDatabase());
        }
        if (options.getConnectionPoolSize() > 0) {
            singleConfig.setConnectionPoolSize(options.getConnectionPoolSize());
        }
        if (options.getSubscriptionConnectionPoolSize() > 0) {
            singleConfig.setSubscriptionConnectionPoolSize(options.getSubscriptionConnectionPoolSize());
        }
        singleConfig.setSubscriptionConnectionMinimumIdleSize(MIN_SUBSCRIPTION_CONNECTION_POOL_SIZE);
        this.setBaseConfig(singleConfig);
    }

    private void setSentinelConfig(Config config) {
        var sentinelConfig = config.useSentinelServers().setMasterName(options.getMasterName())
                .addSentinelAddress(StringUtil.split(options.getBroker()).toArray(new String[0]));
        if (options.getDatabase() > 0) {
            sentinelConfig.setDatabase(options.getDatabase());
        }
        if (options.getConnectionPoolSize() > 0) {
            sentinelConfig.setMasterConnectionPoolSize(options.getConnectionPoolSize())
                    .setSlaveConnectionPoolSize(options.getConnectionPoolSize());
        }
        if (options.getSubscriptionConnectionPoolSize() > 0) {
            sentinelConfig.setSubscriptionConnectionPoolSize(options.getSubscriptionConnectionPoolSize());
        }
        sentinelConfig.setSubscriptionConnectionMinimumIdleSize(MIN_SUBSCRIPTION_CONNECTION_POOL_SIZE);
        this.setBaseConfig(sentinelConfig);
    }

    private void setClusterConfig(Config config) {
        var clusterConfig = config.useClusterServers()
                .addNodeAddress(StringUtil.split(options.getBroker()).toArray(new String[0]));
        if (options.getConnectionPoolSize() > 0) {
            clusterConfig.setMasterConnectionPoolSize(options.getConnectionPoolSize())
                    .setSlaveConnectionPoolSize(options.getConnectionPoolSize());
        }
        if (options.getSubscriptionConnectionPoolSize() > 0) {
            clusterConfig.setSubscriptionConnectionPoolSize(options.getSubscriptionConnectionPoolSize());
        }
        clusterConfig.setSubscriptionConnectionMinimumIdleSize(MIN_SUBSCRIPTION_CONNECTION_POOL_SIZE);
        this.setBaseConfig(clusterConfig);
    }

    private void setBaseConfig(BaseConfig<?> baseConfig) {
        if (options.getPassword() != null && !options.getPassword().isEmpty()) {
            baseConfig.setPassword(options.getPassword());
        }
        if (options.getTimeoutMillis() > 0) {
            baseConfig.setTimeout(options.getTimeoutMillis())
                    .setConnectTimeout(options.getTimeoutMillis())
                    .setIdleConnectionTimeout(options.getTimeoutMillis());
        }
        if (options.getRetry() > 0) {
            baseConfig.setRetryAttempts(options.getRetry());
        }
    }

}
