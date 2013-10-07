package ru.yaal.project.urldatabase.storage;

import org.springframework.context.ApplicationContext;

/**
 * Общие элементы реализации.
 * User: Aleks
 * Date: 06.10.13
 */
public abstract class AbstractStorage<T> implements IStorage<T> {
    protected final ApplicationContext context;

    public AbstractStorage(ApplicationContext context) {
        this.context = context;
    }
}
