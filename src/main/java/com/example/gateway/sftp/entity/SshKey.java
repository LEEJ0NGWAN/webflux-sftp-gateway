package com.example.gateway.sftp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "ssh_key")
public class SshKey {

    @Id
    private String id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String privateKey;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String publicKey;
}
