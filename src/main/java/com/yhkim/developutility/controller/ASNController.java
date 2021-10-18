package com.yhkim.developutility.controller;

import com.yhkim.developutility.common.CustomASN1Dump;
import com.yhkim.developutility.common.oid.OIDMapping;
import com.yhkim.developutility.dto.ASNDTO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.DecoderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "ASN.1 API")
@RequestMapping("/api/asn-1")
@RestController
@RequiredArgsConstructor
public class ASNController {

    private final OIDMapping oidMapping;
    private final CustomASN1Dump customASN1Dump;

    @PostMapping("/oid-repository")
    public ResponseEntity<String> getNameOfOID(@RequestBody ASNDTO.OID oid) {
        String domain = oidMapping.getDomainByOID(oid.getOid());
        if (domain == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such oid");
        } else {
            return ResponseEntity.ok(oid.getOid() + ": " + domain);
        }
    }

    @PostMapping("/decode")
    public ResponseEntity<String> decodeASN(@RequestBody ASNDTO.EncodedString asndto) {
        String encodedString = asndto.getStr();
        encodedString = encodedString.replaceAll("\n", "");
        byte[] decodedString = Base64.decode(encodedString);

        StringBuilder output = new StringBuilder();
        output.append("ASN.1 ...\n\n");
        try {
            ASN1InputStream asn1InputStream = new ASN1InputStream(decodedString);
            Object obj = null;
            while ((obj = asn1InputStream.readObject()) != null) {
                output.append(customASN1Dump.dumpAsString(obj, true));
                output.append("\n");
            }
        } catch (DecoderException | IOException e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
        output.append("\n");
        output.append("Binary ...\n\n");

        for (int i = 0; i < decodedString.length; i++) {
            output.append(String.format("%02x ", decodedString[i]&0xff));
            if ((i + 1) % 8 == 0) {
                output.append(" ");
            }
            if ((i + 1) % 16 == 0) {
                output.append("\n");
            }
        }

        ASN1ObjectIdentifier identifier = new ASN1ObjectIdentifier("1.2.840.10045.4.3.2");


        return ResponseEntity.ok(output.toString());
    }
}
