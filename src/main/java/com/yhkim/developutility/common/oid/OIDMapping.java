package com.yhkim.developutility.common.oid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yhkim.developutility.common.ShutdownManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class OIDMapping {

    private final String OID_FILE_PATH = "asn1-oid.json";
    private final ShutdownManager shutdownManager;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static HashMap<String, OID> oidDictionary;

    @PostConstruct
    void init() {
        try {
            ClassPathResource classPathResource = new ClassPathResource(this.OID_FILE_PATH);
            if (!classPathResource.exists()) {
                throw new IllegalAccessException();
            }

            ObjectMapper mapper = new ObjectMapper();
            oidDictionary = mapper.readValue(classPathResource.getInputStream(), new TypeReference<HashMap<String, OID>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            shutdownManager.initiateShutdown(-1);
        }
    }

    public OID getOIDByIdentifier(ASN1ObjectIdentifier identifier) {
        String oid = identifier.getId();
        return oidDictionary.get(oid);
    }

    public String getDomainByOID(String oid) {
        return oidDictionary.get(oid).getDomain();
    }

}
