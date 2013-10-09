package ru.yaal.project.urldatabase.storage;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.DateFormat;

import static java.lang.String.format;

/**
 * Обращение к хранилищу БД по URL.
 * User: Aleks
 * Date: 06.10.13
 */
class UrlStorage extends AbstractStorage<URL> implements ApplicationContextAware {
    private final ICoder<URL> coder;
    private final DateFormat dateFormat;
    private final File root;
    private ApplicationContext context;

    public UrlStorage(ICoder<URL> coder, DateFormat dateFormat) {
        assert (coder != null);
        this.coder = coder;
        assert (dateFormat != null);
        this.dateFormat = dateFormat;
        this.root = coder.getRootDir();
    }

    @Override
    public ILoadable get(URL key) {
        File file = coder.code(key);
        return (ILoadable) context.getBean("fileLoadable", file);
    }

    @Override
    public void put(ILoadable loadable) {
        File file = coder.code(loadable);
        try {
            file.getParentFile().mkdirs();
            try (BufferedOutputStream bos =
                         new BufferedOutputStream(new FileOutputStream(file, false))) {
                bos.write(loadable.getUrl().toString().getBytes());
                bos.write('\n');
                assert (dateFormat != null);
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

    @Override
    public long size() {
        long result = 0;
        File[] subDirs = root.listFiles();
        if (subDirs != null) {
            for (File subDir : subDirs) {
                result += subDir.list().length;
            }
        }
        return result;
    }

    @Override
    public void clean() {
        File[] subDirs = root.listFiles();
        if (subDirs != null) {
            for (File subDir : subDirs) {
                File[] files = subDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        deleteWithException(file);
                    }
                    deleteWithException(subDir);
                }
            }
        }
    }

    private void deleteWithException(File file) {
        boolean deleted = file.delete();
        if (!deleted) {
            throw new RuntimeException(format("Can't delete file: %s", file));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
