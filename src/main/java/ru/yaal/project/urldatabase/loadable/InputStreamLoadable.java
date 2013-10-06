package ru.yaal.project.urldatabase.loadable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import static java.lang.String.format;

/**
 * Загружает объект БД из потока.
 * User: Aleks
 * Date: 06.10.13
 */
public class InputStreamLoadable extends AbstractLoadable {
    private final InputStream stream;
    private boolean loaded = false;

    public InputStreamLoadable(InputStream stream) {
        this.stream = stream;
    }

    private void load() {
        loaded = true;
        try (InputStream bis = new BufferedInputStream(stream)) {
            setUrl(readLine(bis));
            setLoadDate(readLine(bis));
            setContent(readContent(bis));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private String readLine(InputStream is) throws IOException {
        StringBuilder result = new StringBuilder();
        while (true) {
            int c = is.read();
            if (c == -1) {
                throw new IOException(format("Unexpected end of file: %s", result.toString()));
            } else if (c == 10) {
                break;
            } else {
                result.append((char) c);
            }
        }
        return result.toString();
    }

    private byte[] readContent(InputStream is) throws IOException {
        int contentChar;
        StringBuilder content = new StringBuilder();
        while ((contentChar = is.read()) != -1) {
            content.append((char) contentChar);
        }
        return content.toString().getBytes();
    }

    @Override
    public URL getUrl() {
        if (!loaded) {
            load();
        }
        return super.getUrl();
    }

    @Override
    public byte[] getContent() {
        if (!loaded) {
            load();
        }
        return super.getContent();
    }

    @Override
    public Date getLoadDate() {
        if (!loaded) {
            load();
        }
        return super.getLoadDate();
    }
}
