package ru.yaal.project.urldatabase.storage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yaal.project.urldatabase.loadable.ILoadable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.*;
import static uk.co.it.modular.hamcrest.date.DateMatchers.within;

public class UrlStorageTest {
    private static final ApplicationContext TEST_CONTEXT = new ClassPathXmlApplicationContext("test-spring-config.xml");
    private static final IStorage<URL> STORAGE = TEST_CONTEXT.getBean("urlStorage", UrlStorage.class);

    @Test(dataProvider = "loadableProvider")
    public void putAndGet(URL url, Date loadDate, byte[] content) throws MalformedURLException {
        ILoadable loadable = (ILoadable) TEST_CONTEXT.getBean("memoryLoadable", url, loadDate, content);
        STORAGE.put(loadable);
        ILoadable actual = STORAGE.get(url);
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
        ILoadable loadable = TEST_CONTEXT.getBean("testLoadable", ILoadable.class);
        STORAGE.put(loadable);
        assertTrue(STORAGE.isExists(loadable.getUrl()));
        STORAGE.delete(loadable);
        assertFalse(STORAGE.isExists(loadable.getUrl()));
    }

    @Test
    public void isExists() throws Exception {
        assertFalse(STORAGE.isExists(new URL("http://i_do_not_exists.ru")));
        ILoadable loadable = TEST_CONTEXT.getBean("testLoadable", ILoadable.class);
        STORAGE.put(loadable);
        assertTrue(STORAGE.isExists(loadable.getUrl()));
    }

    @Test
    public void replace() throws MalformedURLException {
        final URL url = new URL("http://for_replace.ru");

        final byte[] firstContent = "I will be REPLACED :(".getBytes();
        ILoadable first = (ILoadable) TEST_CONTEXT.getBean("memoryLoadable", url, new Date(), firstContent);
        STORAGE.put(first);
        ILoadable firstGot = STORAGE.get(url);
        assertEquals(firstGot.getContent(), firstContent);

        final byte[] secondContent = "I will REPLACE YOU!".getBytes();
        ILoadable second = (ILoadable) TEST_CONTEXT.getBean("memoryLoadable", url, new Date(), secondContent);
        STORAGE.put(second);
        ILoadable secondGot = STORAGE.get(url);
        assertEquals(secondGot.getContent(), secondContent);
    }

    @Test
    public void size() {
        final IStorage<URL> storage = TEST_CONTEXT.getBean("urlStorage", IStorage.class);
        int expSize = 0;
        assertEquals(storage.size(), expSize);
        final int filesCount = 10;
        for (int i = 0; i < filesCount; i++) {
            ILoadable loadable = TEST_CONTEXT.getBean("testLoadable", ILoadable.class);
            storage.put(loadable);
            expSize++;
            assertEquals(storage.size(), expSize);
        }
    }

    @Test
    public void clean() {
        final IStorage<URL> storage = TEST_CONTEXT.getBean("urlStorage", IStorage.class);
        final int filesCount = 5;
        for (int i = 0; i < filesCount; i++) {
            ILoadable loadable = TEST_CONTEXT.getBean("testLoadable", ILoadable.class);
            storage.put(loadable);
        }
        assertEquals(storage.size(), filesCount);
        storage.clean();
        assertEquals(storage.size(), 0);

    }

}
