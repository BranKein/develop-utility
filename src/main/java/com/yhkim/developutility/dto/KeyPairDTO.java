package com.yhkim.developutility.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class KeyPairDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateResponse {
        private String publicKey;
        private String privateKey;
    }
}
