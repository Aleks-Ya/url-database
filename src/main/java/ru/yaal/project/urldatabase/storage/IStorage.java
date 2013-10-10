package ru.yaal.project.urldatabase.storage;

import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.util.List;

/**
 * Интерфейс для обращения к хранилищу по ILoadable.
 * (URL или интервал дат).
 * User: Aleks
 * Date: 06.10.13
 */
public interface IStorage {

    void put(ILoadable loadable);

    void delete(ILoadable loadable);

    boolean isExists(ILoadable loadable);

    List<ILoadable> getAll();

    long size();

    void clean();
}
