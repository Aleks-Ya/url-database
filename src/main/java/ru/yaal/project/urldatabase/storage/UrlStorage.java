package ru.yaal.project.urldatabase.storage;

import ru.yaal.project.urldatabase.loadable.FileLoadable;
import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Обращение к хранилищу БД по URL.
 * User: Aleks
 * Date: 06.10.13
 */
public class UrlStorage extends AbstractStorage<URL> {
    private final ICoder<URL> coder;
    private final DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();//инъекция DI

    public UrlStorage(File root) {
        coder = new UrlCoder(root); //todo инъекция срингом
    }

    @Override
    public ILoadable get(URL key) {
        try {
            File file = coder.code(key);
            return new FileLoadable(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void put(ILoadable loadable) {
        File file = coder.code(loadable);
        try {
            file.getParentFile().mkdir();
            try (BufferedOutputStream bos =
                         new BufferedOutputStream(new FileOutputStream(file, false))) {
                bos.write(loadable.getUrl().toString().getBytes());
                bos.write('\n');
                bos.write(dateFormat.format(loadable.getLoadDate()).getBytes());
                bos.write('\n');
                bos.write(loadable.getContent());
                bos.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(URL key) {
        coder.code(key).delete();
    }

    @Override
    public void delete(ILoadable loadable) {
        delete(loadable.getUrl());
    }

    @Override
    public boolean isExists(URL key) {
        return coder.code(key).exists();
    }
}
