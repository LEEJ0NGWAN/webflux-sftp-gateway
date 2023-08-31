package com.example.gateway.sftp.dto.download;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class DownloadResponse {

    private boolean result;
    private String payload;
}
