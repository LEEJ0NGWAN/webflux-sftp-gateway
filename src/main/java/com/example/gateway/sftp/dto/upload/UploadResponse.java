package com.example.gateway.sftp.dto.upload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class UploadResponse {

    private boolean result;
    private String location;
}
