package ru.yaal.project.urldatabase.storage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.*;
import static uk.co.it.modular.hamcrest.date.DateMatchers.within;

public class UrlStorageTest {
    private static final List<IUrlStorage> STORAGES_FOR_CLEAN_UP = new ArrayList<>();
    private static IUrlStorage storage;
    private static GenericXmlApplicationContext testContext;

    @BeforeClass
    public void beforeClass() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        testContext = new GenericXmlApplicationContext();
        testContext.load("classpath:test-spring-config.xml");
        testContext.setParent(context);
        testContext.refresh();
        storage = testContext.getBean("urlStorage", UrlStorage.class);
        STORAGES_FOR_CLEAN_UP.add(storage);
    }

    @Test(dataProvider = "loadableProvider")
    public void putAndGet(URL url, Date loadDate, byte[] content) throws MalformedURLException {
        ILoadable loadable = (ILoadable) testContext.getBean("memoryLoadable", url, loadDate, content);
        storage.put(loadable);
        ILoadable actual = storage.get(url);
        assertEquals(actual.getUrl(), url);
        assertThat(actual.getLoadDate(), within(1, TimeUnit.SECONDS, loadDate));
        assertEquals(actual.getContent(), content);
    }

    @DataProvider
    private Object[][] loadableProvider() throws MalformedURLException {
        return new Object[][]{
                {new URL("https://www.yaal.ru"), new Date(), "my content".getBytes()},
                {new URL("https://www.mail.ru"), new Date(), "everybody common!".getBytes()},
                {new URL("http://www.yandex.ru"), new Date(), "\t   aga\\ no  \n bbb".getBytes()},
                {new URL("http://www.amazon.com"), new Date(), "fuck off".getBytes()},
                {new URL("http://www.wikipedia.org\\articles"), new Date(), "simple text".getBytes()}
        };
    }

    @Test
    public void delete() throws Exception {
        ILoadable loadable = testContext.getBean("testLoadable", ILoadable.class);
        storage.put(loadable);
        assertTrue(storage.isExists(loadable.getUrl()));
        storage.delete(loadable);
        assertFalse(storage.isExists(loadable.getUrl()));
    }

    @Test
    public void isExists() throws Exception {
        assertFalse(storage.isExists(new URL("http://i_do_not_exists.ru")));
        ILoadable loadable = testContext.getBean("testLoadable", ILoadable.class);
        storage.put(loadable);
        assertTrue(storage.isExists(loadable.getUrl()));
    }

    @Test
    public void replace() throws MalformedURLException {
        final URL url = new URL("http://for_replace.ru");

        final byte[] firstContent = "I will be REPLACED :(".getBytes();
        ILoadable first = (ILoadable) testContext.getBean("memoryLoadable", url, new Date(), firstContent);
        storage.put(first);
        ILoadable firstGot = storage.get(url);
        assertEquals(firstGot.getContent(), firstContent);

        final byte[] secondContent = "I will REPLACE YOU!".getBytes();
        ILoadable second = (ILoadable) testContext.getBean("memoryLoadable", url, new Date(), secondContent);
        storage.put(second);
        ILoadable secondGot = storage.get(url);
        assertEquals(secondGot.getContent(), secondContent);
    }

    @Test
    public void size() {
        final IUrlStorage storage = (IUrlStorage) testContext.getBean("urlStorage");
        STORAGES_FOR_CLEAN_UP.add(storage);
        int expSize = 0;
        assertEquals(storage.size(), expSize);
        final int filesCount = 10;
        for (int i = 0; i < filesCount; i++) {
            ILoadable loadable = testContext.getBean("testLoadable", ILoadable.class);
            storage.put(loadable);
            expSize++;
            assertEquals(storage.size(), expSize);
        }
    }

    @Test
    public void clean() {
        final IUrlStorage storage = (IUrlStorage) testContext.getBean("urlStorage");
        STORAGES_FOR_CLEAN_UP.add(storage);
        final int filesCount = 5;
        for (int i = 0; i < filesCount; i++) {
            ILoadable loadable = testContext.getBean("testLoadable", ILoadable.class);
            storage.put(loadable);
        }
        assertEquals(storage.size(), filesCount);
        storage.clean();
        assertEquals(storage.size(), 0);
    }

    @Test
    public void getAll() {
        final IUrlStorage storage = testContext.getBean("urlStorage", IUrlStorage.class);
        STORAGES_FOR_CLEAN_UP.add(storage);
        final int filesCount = 5;
        List<ILoadable> expected = new ArrayList<>(filesCount);
        for (int i = 0; i < filesCount; i++) {
            ILoadable loadable = testContext.getBean("testLoadable", ILoadable.class);
            storage.put(loadable);
            expected.add(loadable);
        }
        List<ILoadable> actual = storage.getAll();
        assertEqualsNoOrder(actual.toArray(), expected.toArray());
    }

    @AfterClass
    public void cleanStorages() throws NoSuchFieldException, IllegalAccessException {
        for (IUrlStorage storage : STORAGES_FOR_CLEAN_UP) {
            UrlStorage urlStorage = (UrlStorage) storage;
            urlStorage.clean();
            Field rootField = UrlStorage.class.getDeclaredField("root");
            rootField.setAccessible(true);
            File root = (File) rootField.get(urlStorage);
            assertTrue(root.delete());
        }
    }

}
