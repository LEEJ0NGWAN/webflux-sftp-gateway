package com.example.gateway.sftp.config;

import com.example.gateway.sftp.properties.SftpProperties;
import com.example.gateway.sftp.repository.SshKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JschConfig {

    private static JSch jSch;

    @Bean
    public Session session(SftpProperties sftpProperties, SshKeyRepository sshKeyRepository)
    throws Exception {

        final byte[] privateKeyBytes = sshKeyRepository
        .findById(sftpProperties.getPrivateKeyId())
        .get()
        .getPrivateKey()
        .getBytes();

        jSch = new JSch();

        System.out.println("Add ssh private key of "+sftpProperties.getPrivateKeyId());
        System.out.println("SFTP Connection Info: "+sftpProperties.getServerUsername()+"@"+sftpProperties.getServerAddress());

        jSch.addIdentity("id_rsa", privateKeyBytes, null, null);

        final Session session = jSch.getSession(
            sftpProperties.getServerUsername(),
            sftpProperties.getServerAddress());

        sftpProperties
        .getConfig()
        .forEach((key, value) -> JSch.setConfig(key, value));

        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        return session;
    }
}
