package com.example.gateway.sftp.dto.download;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DownloadRequest {

    private String name;
    private String path; // absolute path
}
