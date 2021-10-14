package com.yhkim.developutility.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Api(tags = "Hash API")
@RequestMapping("/api/hash")
@RestController
@AllArgsConstructor
public class HashController {

    @PostMapping("/sha-1")
    public ResponseEntity<String> sha1(@RequestBody String plainText) throws Exception {
        return ResponseEntity.ok(hash(plainText, MessageDigest.getInstance("SHA-1")));
    }

    @PostMapping("/sha-256")
    public ResponseEntity<String> sha256(@RequestBody String plainText) throws Exception {
        return ResponseEntity.ok(hash(plainText, MessageDigest.getInstance("SHA-256")));
    }

    private String hash(String plainText, MessageDigest messageDigest) {
        byte[] target = plainText.getBytes(StandardCharsets.UTF_8);
        messageDigest.update(target);
        return Hex.toHexString(messageDigest.digest());
    }
}
