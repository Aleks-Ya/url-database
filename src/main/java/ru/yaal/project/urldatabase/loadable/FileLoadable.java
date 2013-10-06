package ru.yaal.project.urldatabase.loadable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * Объект БД, представленный файлом на диске.
 * User: Aleks
 * Date: 06.10.13
 */
public class FileLoadable extends AbstractLoadable {
    private ILoadable inputStreamLoadable;

    public FileLoadable(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    public FileLoadable(InputStream stream) {
        inputStreamLoadable = new InputStreamLoadable(stream);
    }

    @Override
    public URL getUrl() {
        return inputStreamLoadable.getUrl();
    }

    @Override
    public byte[] getContent() {
        return inputStreamLoadable.getContent();
    }

    @Override
    public Date getLoadDate() {
        return inputStreamLoadable.getLoadDate();
    }
}
