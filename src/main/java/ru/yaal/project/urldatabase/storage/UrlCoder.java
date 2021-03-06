package ru.yaal.project.urldatabase.storage;

import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.security.MessageDigest;

/**
 * Кодирует URL в SHA-1.
 * User: Aleks
 * Date: 06.10.13
 */
class UrlCoder implements ICoder<URL> {
    private final File root;
    private final MessageDigest digest;

    public UrlCoder(File root, MessageDigest digest) {
        this.root = root;
        this.digest = digest;
    }

    public UrlCoder(Path root, MessageDigest digest) {
        this(root.toFile(), digest);
    }

    private String hashToString(byte[] hash) {
        String result = "";
        for (byte b : hash) {
            int v = b & 0xFF;
            if (v < 16) result += "0";
            result += Integer.toString(v, 16).toUpperCase();
        }
        return result;
    }

    @Override
    public File code(ILoadable loadable) {
        return code(loadable.getUrl());
    }

    @Override
    public File code(URL key) {
        digest.update(key.toString().getBytes());
        byte[] digestBytes = digest.digest();
        String sha = hashToString(digestBytes);
        String sub = sha.substring(0, 2);
        File subFolder = new File(root, sub);
        return new File(subFolder, sha);
    }

    @Override
    public File getRootDir() {
        return root;
    }
}
