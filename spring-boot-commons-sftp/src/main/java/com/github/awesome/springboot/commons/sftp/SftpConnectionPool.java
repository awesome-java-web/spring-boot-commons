package com.github.awesome.springboot.commons.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SftpConnectionPool {

    private final SftpConfig config;

    private final BlockingQueue<ChannelSftp> pool;

    private final ScheduledThreadPoolExecutor idleChannelRecycler;

    public SftpConnectionPool(SftpConfig config) {
        this.config = config;
        this.pool = new LinkedBlockingQueue<>(config.getCorePoolSize());
        this.idleChannelRecycler = new ScheduledThreadPoolExecutor(1);
        this.initializeConnectionPool();
        this.startIdleChannelRecycler();
    }

    private void initializeConnectionPool() {
        for (int i = 0, corePoolSize = config.getCorePoolSize(); i < corePoolSize; i++) {
            ChannelSftp channel = createChannel();
            final boolean offered = pool.offer(channel);
            if (!offered) {
                channel.disconnect();
            }
        }
    }

    private ChannelSftp createChannel() {
        final String host = config.getHost();
        final int port = config.getPort();
        final String username = config.getUsername();
        final String password = config.getPassword();
        final int connectTimeoutMillis = config.getConnectTimeoutMillis();

        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            // 为了防止首次连接时进行主机检查，这里直接禁用主机检查
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(connectTimeoutMillis);
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(connectTimeoutMillis);
            return channel;
        } catch (JSchException e) {
            throw new SftpException("Failed to create SFTP session", e);
        }
    }

    private void startIdleChannelRecycler() {
        final int keepAliveMillis = config.getKeepAliveMillis();
        idleChannelRecycler.scheduleWithFixedDelay(() -> {
            if (pool.size() > config.getCorePoolSize()) {
                ChannelSftp channel = pool.poll();
                if (channel != null) {
                    channel.disconnect();
                }
            }
        }, keepAliveMillis, keepAliveMillis, TimeUnit.MILLISECONDS);
    }

    public ChannelSftp borrowChannel() {
        ChannelSftp channel = pool.poll();
        if (channel == null) {
            if (pool.size() < config.getMaxPoolSize()) {
                channel = createChannel();
            } else {
                try {
                    pool.poll(config.getAcquireIdleConnectionTimeoutMillis(), TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new SftpException("Failed to borrow SFTP channel", e);
                }
            }
        }
        return channel;
    }

    public void returnChannel(ChannelSftp channel) {
        if (channel == null) {
            return;
        }

        if (pool.size() < config.getCorePoolSize()) {
            final boolean offered = pool.offer(channel);
            if (!offered) {
                channel.disconnect();
            }
        } else {
            channel.disconnect();
        }
    }

}
