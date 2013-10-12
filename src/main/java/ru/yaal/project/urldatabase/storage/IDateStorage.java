package ru.yaal.project.urldatabase.storage;

import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.util.Date;
import java.util.List;

/**
 * Интерфейс для обращения к хранилищу по датам добавления объектов.
 * User: Aleks
 * Date: 10.10.13
 */
public interface IDateStorage extends IStorage {
    List<ILoadable> get(Date first, Date second);

    void delete(Date first, Date second);
}
