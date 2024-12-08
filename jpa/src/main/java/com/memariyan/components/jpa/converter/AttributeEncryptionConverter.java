package com.memariyan.components.jpa.converter;

import com.memariyan.components.jpa.config.JpaProperties;
import com.memariyan.components.security.util.CredentialPropertyEncryptorUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class AttributeEncryptionConverter implements AttributeConverter<String, String> {

    private final JpaProperties properties;

    public AttributeEncryptionConverter(JpaProperties properties) {
        this.properties = properties;
    }

    @Override
    public String convertToDatabaseColumn(String value) {
        try {
            return CredentialPropertyEncryptorUtil.encrypt(value, properties.getAttributeEncryptionKey());

        } catch (Exception e) {
            log.error("unable to encrypt attribute data for error ", e);
            return value;
        }
    }

    @Override
    public String convertToEntityAttribute(String value) {
        try {
            return CredentialPropertyEncryptorUtil.decrypt(value, properties.getAttributeEncryptionKey());

        } catch (Exception e) {
            log.error("unable to decrypt attribute data for error ", e);
            return value;
        }
    }
}
