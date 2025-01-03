package com.github.awesome.springboot.commons.sftp

import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory
import org.apache.sshd.server.SshServer
import org.apache.sshd.server.auth.password.PasswordAuthenticator
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider
import org.apache.sshd.sftp.server.SftpSubsystemFactory
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledIfSystemProperty
import java.nio.file.Files
import java.nio.file.Paths

@DisabledIfSystemProperty(named = "os.name", matches = "Windows.*")
class SftpClientTest {

    companion object {

        private lateinit var sshServer: SshServer

        private lateinit var sftpConfig: SftpConfig

        @BeforeAll
        @JvmStatic
        fun setUp() {
            sshServer = SshServer.setUpDefaultServer()
            sshServer.port = 2222
            sshServer.passwordAuthenticator = PasswordAuthenticator { username, password, _ ->
                username == "root" && password == "root"
            }
            sshServer.keyPairProvider = SimpleGeneratorHostKeyProvider()
            sshServer.subsystemFactories = listOf(SftpSubsystemFactory())
            sshServer.fileSystemFactory = VirtualFileSystemFactory(Paths.get("/tmp"))
            sshServer.start()

            sftpConfig = SftpConfig.builder()
                .host("localhost")
                .port(2222)
                .username("root")
                .password("root")
                .connectTimeoutMillis(10000)
                .corePoolSize(1)
                .maxPoolSize(1)
                .keepAliveMillis(10000)
                .acquireIdleConnectionTimeoutMillis(10000)
                .build()

            Files.createFile(Paths.get("test.txt"))
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            Files.deleteIfExists(Paths.get("test.txt"))
            if (sshServer.isStarted) {
                sshServer.stop(true)
            }
        }

    }

    @Test
    fun testUpload() {
        val sftpClient = SftpClient(sftpConfig)
        sftpClient.upload("test.txt", "test.txt")
    }

    @Test
    fun testDownload() {
        val sftpClient = SftpClient(sftpConfig)
        sftpClient.download("test.txt", "test.txt")
    }

}
