package ru.yaal.project.urldatabase.storage;

import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.io.File;

/**
 * Вычисляет имя файла в хранилище.
 * User: Aleks
 * Date: 06.10.13
 */
public interface ICoder<T> {
    File code(ILoadable loadable);

    File code(T key);

    File getRootDir();
}
