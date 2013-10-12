package ru.yaal.project.urldatabase.storage;

import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Доступ к хранилищу по датам.
 * User: Aleks
 * Date: 10.10.13
 */
public class DateStorage implements IDateStorage {
    private IUrlStorage urlStorage;

    public DateStorage(IUrlStorage urlStorage) {
        this.urlStorage = urlStorage;
    }

    private Date getMinDate(Date first, Date second) {
        return (first.before(second)) ? first : second;
    }

    private Date getMaxDate(Date first, Date second) {
        return (first.after(second)) ? first : second;
    }

    @Override
    public List<ILoadable> get(Date first, Date second) {
        List<ILoadable> result = new ArrayList<>();
        Date min = getMinDate(first, second);
        Date max = getMaxDate(first, second);
        assert (max.getTime() >= min.getTime());
        List<ILoadable> all = getAll();
        for (ILoadable loadable : all) {
            Date current = loadable.getLoadDate();
            if (min.before(current) && max.after(current)) {
                result.add(loadable);
            }
        }
        return result;
    }

    @Override
    public void delete(Date first, Date second) {
        Date min = getMinDate(first, second);
        Date max = getMaxDate(first, second);
        List<ILoadable> all = getAll();
        for (ILoadable loadable : all) {
            Date current = loadable.getLoadDate();
            if (min.before(current) && max.after(current)) {
                delete(loadable);
            }
        }
    }

    @Override
    public void put(ILoadable loadable) {
        urlStorage.put(loadable);
    }

    @Override
    public void delete(ILoadable loadable) {
        urlStorage.delete(loadable);
    }

    @Override
    public boolean isExists(ILoadable loadable) {
        return urlStorage.isExists(loadable);
    }

    @Override
    public List<ILoadable> getAll() {
        return urlStorage.getAll();
    }

    @Override
    public long size() {
        return urlStorage.size();
    }

    @Override
    public void clean() {
        urlStorage.clean();
    }
}
