package com.yhkim.developutility.controller;

import com.yhkim.developutility.dto.HmacDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@Api(tags = "HMAC API")
@RequestMapping("/api/hmac")
@RestController
@AllArgsConstructor
public class HmacController {

    @PostMapping("/with-sha1")
    public ResponseEntity<String> hmacWithSHA1(@RequestBody HmacDTO hmacDTO) {
        return ResponseEntity.ok(doHmac(hmacDTO, new HMac(new SHA1Digest())));
    }

    @PostMapping("/with-sha256")
    public ResponseEntity<String> hmacWithSHA256(@RequestBody HmacDTO hmacDTO) {
        return ResponseEntity.ok(doHmac(hmacDTO, new HMac(new SHA256Digest())));
    }

    @PostMapping("/with-sha512")
    public ResponseEntity<String> hmacWithSHA512(@RequestBody HmacDTO hmacDTO) {
        return ResponseEntity.ok(doHmac(hmacDTO, new HMac(new SHA512Digest())));
    }

    private String doHmac(HmacDTO hmacDTO, HMac hmac) {
        final byte[] hmacResult = new byte[hmac.getMacSize()];

        byte[] target = hmacDTO.getPlainText().getBytes(StandardCharsets.UTF_8);
        hmac.init(new KeyParameter(hmacDTO.getKey().getBytes(StandardCharsets.UTF_8)));
        hmac.update(target, 0, target.length);
        hmac.doFinal(hmacResult, 0);

        return Hex.toHexString(hmacResult);
    }
}
