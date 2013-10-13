package ru.yaal.project.urldatabase.storage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.lang.String.format;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertTrue;

public class DateStorageTest {
    private final Random random = new Random();
    private List<ILoadable> all;
    private GenericXmlApplicationContext testContext;
    private DateStorage storage;
    private ILoadable min;
    private List<ILoadable> middle;
    private ILoadable max;

    @BeforeClass
    public void loadSpringContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        testContext = new GenericXmlApplicationContext();
        testContext.load("classpath:test-spring-config.xml");
        testContext.setParent(context);
        testContext.refresh();
    }

    @BeforeMethod
    public void beforeMethod() throws MalformedURLException {
        List<ILoadable> least = new ArrayList<>();
        least.add(makeLoadable(1, 9, 1980));
        least.add(makeLoadable(15, 1, 1981));
        least.add(makeLoadable(16, 5, 1985));
        min = makeLoadable(18, 12, 1990);
        middle = new ArrayList<>();
        middle.add(makeLoadable(8, 10, 1994));
        middle.add(makeLoadable(7, 11, 1996));
        middle.add(makeLoadable(28, 6, 1999));
        max = makeLoadable(18, 8, 2000);
        List<ILoadable> most = new ArrayList<>();
        most.add(makeLoadable(2, 3, 2003));
        most.add(makeLoadable(21, 9, 2009));
        most.add(makeLoadable(10, 1, 2013));

        storage = (DateStorage) testContext.getBean("dateStorage");

        all = new ArrayList<>();
        all.addAll(most);
        all.addAll(least);
        all.addAll(middle);
        all.add(min);
        all.add(max);
        storage.put(all);
    }

    @Test
    public void get() {
        List<ILoadable> actMiddle = storage.get(max.getLoadDate(), min.getLoadDate());
        assertEqualsNoOrder(actMiddle.toArray(), middle.toArray());
        List<ILoadable> actAll = storage.getAll();
        assertEqualsNoOrder(actAll.toArray(), all.toArray());
    }

    private ILoadable makeLoadable(int day, int month, int year) throws MalformedURLException {
        URL url = new URL(format("http://www.mail%s.ru", random.nextLong()));
        Date date = (Date) testContext.getBean("dateDMY", day, month, year);
        byte[] content = ("Content is " + random.nextDouble()).getBytes();
        return (ILoadable) testContext.getBean("memoryLoadable", url, date, content);
    }

    @Test
    public void testDelete() throws Exception {
        List<ILoadable> actAll = storage.getAll();
        assertEqualsNoOrder(actAll.toArray(), all.toArray());
        storage.delete(max.getLoadDate(), min.getLoadDate());
        List<ILoadable> actAllDeleted = storage.getAll();
        all.removeAll(middle);
        assertEqualsNoOrder(actAllDeleted.toArray(), all.toArray());
    }

    @AfterMethod
    public void cleanStorage() throws NoSuchFieldException, IllegalAccessException {
        Field urlStorageField = DateStorage.class.getDeclaredField("urlStorage");
        urlStorageField.setAccessible(true);
        UrlStorage urlStorage = (UrlStorage) urlStorageField.get(storage);
        urlStorage.clean();
        Field rootField = UrlStorage.class.getDeclaredField("root");
        rootField.setAccessible(true);
        File root = (File) rootField.get(urlStorage);
        assertTrue(root.delete());
    }
}
