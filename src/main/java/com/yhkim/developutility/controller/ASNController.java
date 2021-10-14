package com.yhkim.developutility.controller;

import com.yhkim.developutility.dto.ASNDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.DecoderException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "ASN API")
@RequestMapping("/api/asn")
@RestController
@AllArgsConstructor
public class ASNController {

    @PostMapping("/decode")
    public ResponseEntity<String> decodeASN(@RequestBody ASNDTO asndto) {
        String encodedString = asndto.getEncodedString();
        try {
            encodedString = encodedString.replaceAll("\n", "");
            ASN1InputStream asn1InputStream = new ASN1InputStream(Base64.decode(encodedString));
            Object obj = null;
            StringBuilder output = new StringBuilder();
            while ((obj = asn1InputStream.readObject()) != null) {
                output.append(ASN1Dump.dumpAsString(obj));
                output.append("\n");
            }
            return ResponseEntity.ok(output.toString());
        } catch (DecoderException | IOException e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }
}
