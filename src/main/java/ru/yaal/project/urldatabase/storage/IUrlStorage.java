package ru.yaal.project.urldatabase.storage;

import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.net.URL;

/**
 * Интерфейс для обращения к хранилищу по URL объектов.
 * User: Aleks
 * Date: 10.10.13
 */
public interface IUrlStorage extends IStorage {
    ILoadable get(URL key);

    void delete(URL key);

    boolean isExists(URL key);

}
