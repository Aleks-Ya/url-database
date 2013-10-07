package ru.yaal.project.urldatabase.spring;

import org.springframework.beans.factory.FactoryBean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1FactoryBean implements FactoryBean<MessageDigest> {
    @Override
    public MessageDigest getObject() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return MessageDigest.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
