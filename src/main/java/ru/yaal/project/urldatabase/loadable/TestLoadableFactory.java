package ru.yaal.project.urldatabase.loadable;

import org.springframework.beans.factory.FactoryBean;

import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * Фабрика тестовых объектов для БД ILoadable.
 * User: Aleks
 * Date: 09.10.13
 */
public class TestLoadableFactory implements FactoryBean<ILoadable> {
    private final Random random = new Random();

    @Override
    public ILoadable getObject() throws Exception {
        URL url = new URL("http://yandex" + random.nextInt(Integer.MAX_VALUE) + ".ru");
        Date date = new Date();
        byte[] content = ("Content from TestLoadableFactory " + random.nextDouble()).getBytes();
        return new MemoryLoadable(url, date, content);
    }

    @Override
    public Class<?> getObjectType() {
        return MemoryLoadable.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
