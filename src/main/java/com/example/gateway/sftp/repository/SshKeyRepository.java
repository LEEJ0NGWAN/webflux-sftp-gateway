package com.example.gateway.sftp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gateway.sftp.entity.SshKey;

public interface SshKeyRepository extends JpaRepository<SshKey, String> {}
