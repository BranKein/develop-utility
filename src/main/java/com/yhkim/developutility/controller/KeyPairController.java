package com.yhkim.developutility.controller;

import com.yhkim.developutility.dto.KeyPairDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

@Api(tags = "KeyPair API")
@RequestMapping("/api/key-pair")
@RestController
@AllArgsConstructor
public class KeyPairController {

    @PostMapping("/generate/rsa/2048")
    public ResponseEntity<KeyPairDTO.GenerateResponse> generateWithRSA2048() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        KeyPairDTO.GenerateResponse result = KeyPairDTO.GenerateResponse.builder()
                .publicKey(new String(Base64.encode(keyPair.getPublic().getEncoded())))
                .privateKey(new String(Base64.encode(keyPair.getPrivate().getEncoded())))
                .build();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/generate/rsa/4096")
    public ResponseEntity<KeyPairDTO.GenerateResponse> generateWithRSA4096() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
        generator.initialize(4096);
        KeyPair keyPair = generator.generateKeyPair();
        KeyPairDTO.GenerateResponse result = KeyPairDTO.GenerateResponse.builder()
                .publicKey(new String(Base64.encode(keyPair.getPublic().getEncoded())))
                .privateKey(new String(Base64.encode(keyPair.getPrivate().getEncoded())))
                .build();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/generate/ecdsa/secp256r1")
    public ResponseEntity<KeyPairDTO.GenerateResponse> generateWithECDSAWithSecp256r1() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        generator.initialize(ecSpec, new SecureRandom());
        KeyPair keyPair = generator.generateKeyPair();
        KeyPairDTO.GenerateResponse result = KeyPairDTO.GenerateResponse.builder()
                .publicKey(new String(Base64.encode(keyPair.getPublic().getEncoded())))
                .privateKey(new String(Base64.encode(keyPair.getPrivate().getEncoded())))
                .build();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/generate/ecdsa/secp256k1")
    public ResponseEntity<KeyPairDTO.GenerateResponse> generateWithECDSAWithSecp256k1() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        generator.initialize(ecSpec, new SecureRandom());
        KeyPair keyPair = generator.generateKeyPair();
        KeyPairDTO.GenerateResponse result = KeyPairDTO.GenerateResponse.builder()
                .publicKey(new String(Base64.encode(keyPair.getPublic().getEncoded())))
                .privateKey(new String(Base64.encode(keyPair.getPrivate().getEncoded())))
                .build();
        return ResponseEntity.ok(result);
    }
}
