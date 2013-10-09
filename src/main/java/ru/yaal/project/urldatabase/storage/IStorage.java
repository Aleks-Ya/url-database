package ru.yaal.project.urldatabase.storage;

import ru.yaal.project.urldatabase.loadable.ILoadable;

/**
 * Интерфейс для обращения к хранилищу по заданному типу ключа
 * (URL или интервал дат).
 * User: Aleks
 * Date: 06.10.13
 */
public interface IStorage<T> {
    ILoadable get(T key);

    void put(ILoadable loadable);

    void delete(T key);

    void delete(ILoadable loadable);

    boolean isExists(T key);

    long size();

    void clean();
}
