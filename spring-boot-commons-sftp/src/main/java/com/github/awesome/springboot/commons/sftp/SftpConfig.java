package com.github.awesome.springboot.commons.sftp;

public class SftpConfig {

    private static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 30000;

    private static final int DEFAULT_CORE_POOL_SIZE = 8;

    private static final int DEFAULT_MAX_POOL_SIZE = 64;

    private static final int DEFAULT_KEEP_ALIVE_MILLIS = 60000;

    private static final int DEFAULT_ACQUIRE_IDLE_CONNECTION_TIMEOUT_MILLIS = 30000;

    private final String host;

    private final int port;

    private final String username;

    private final String password;

    private final int connectTimeoutMillis;

    private final int corePoolSize;

    private final int maxPoolSize;

    private final int keepAliveMillis;

    private final int acquireIdleConnectionTimeoutMillis;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getKeepAliveMillis() {
        return keepAliveMillis;
    }

    public int getAcquireIdleConnectionTimeoutMillis() {
        return acquireIdleConnectionTimeoutMillis;
    }

    private SftpConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
        this.connectTimeoutMillis = builder.connectTimeoutMillis;
        this.corePoolSize = builder.corePoolSize;
        this.maxPoolSize = builder.maxPoolSize;
        this.keepAliveMillis = builder.keepAliveMillis;
        this.acquireIdleConnectionTimeoutMillis = builder.acquireIdleConnectionTimeoutMillis;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String host;

        private int port;

        private String username;

        private String password;

        private int connectTimeoutMillis = DEFAULT_CONNECT_TIMEOUT_MILLIS;

        private int corePoolSize = DEFAULT_CORE_POOL_SIZE;

        private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;

        private int keepAliveMillis = DEFAULT_KEEP_ALIVE_MILLIS;

        private int acquireIdleConnectionTimeoutMillis = DEFAULT_ACQUIRE_IDLE_CONNECTION_TIMEOUT_MILLIS;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder connectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        public Builder corePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public Builder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public Builder keepAliveMillis(int keepAliveMillis) {
            this.keepAliveMillis = keepAliveMillis;
            return this;
        }

        public Builder acquireIdleConnectionTimeoutMillis(int acquireIdleConnectionTimeoutMillis) {
            this.acquireIdleConnectionTimeoutMillis = acquireIdleConnectionTimeoutMillis;
            return this;
        }

        public SftpConfig build() {
            return new SftpConfig(this);
        }

    }

}
