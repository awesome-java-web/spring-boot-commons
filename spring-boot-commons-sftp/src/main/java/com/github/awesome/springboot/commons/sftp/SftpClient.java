package com.github.awesome.springboot.commons.sftp;

import com.jcraft.jsch.ChannelSftp;

public class SftpClient {

    private final SftpConnectionPool connectionPool;

    public SftpClient(SftpConfig config) {
        this.connectionPool = new SftpConnectionPool(config);
    }

    public void upload(final String localPath, final String remotePath) {
        ChannelSftp channel = null;
        try {
            channel = connectionPool.borrowChannel();
            channel.put(localPath, remotePath);
        } catch (Exception e) {
            final String message = String.format("Failed to upload file from %s to %s", localPath, remotePath);
            throw new SftpException(message, e);
        } finally {
            connectionPool.returnChannel(channel);
        }
    }

    public void download(final String remotePath, final String localPath) {
        ChannelSftp channel = null;
        try {
            channel = connectionPool.borrowChannel();
            channel.get(remotePath, localPath);
        } catch (Exception e) {
            final String message = String.format("Failed to download file from %s to %s", remotePath, localPath);
            throw new SftpException(message, e);
        } finally {
            connectionPool.returnChannel(channel);
        }
    }

}
