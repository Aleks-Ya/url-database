package ru.yaal.project.urldatabase.storage;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yaal.project.urldatabase.loadable.ILoadable;
import ru.yaal.project.urldatabase.loadable.MemoryLoadable;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.*;
import static uk.co.it.modular.hamcrest.date.DateMatchers.within;

public class UrlStorageTest {
    private IStorage<URL> storage;

    @BeforeClass
    public void beforeClass() throws IOException {
        File root = Files.createTempDirectory("UrlDatabase").toFile();
        root.deleteOnExit();
        storage = new UrlStorage(root);
    }

    @Test(dataProvider = "loadableProvider")
    public void putAndGet(URL url, Date loadDate, byte[] content) throws MalformedURLException {
        ILoadable loadable = new MemoryLoadable(url, loadDate, content);
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
        ILoadable loadable = new MemoryLoadable(
                new URL("http://for_delete.ru"), new Date(), "I will be deleted :(".getBytes());
        storage.put(loadable);
        assertTrue(storage.isExists(loadable.getUrl()));
        storage.delete(loadable);
        assertFalse(storage.isExists(loadable.getUrl()));
    }

    @Test
    public void isExists() throws Exception {
        assertFalse(storage.isExists(new URL("http://i_do_not_exists.ru")));
        ILoadable loadable = new MemoryLoadable(new URL("http://ya.ru"), new Date(), "content".getBytes());
        storage.put(loadable);
        assertTrue(storage.isExists(loadable.getUrl()));
    }

    @Test
    public void replace() throws MalformedURLException {
        final URL url = new URL("http://for_replace.ru");

        final byte[] firstContent = "I will be REPLACED :(".getBytes();
        ILoadable first = new MemoryLoadable(url, new Date(), firstContent);
        storage.put(first);
        ILoadable firstGot = storage.get(url);
        assertEquals(firstGot.getContent(), firstContent);

        final byte[] secondContent = "I will REPLACE YOU!".getBytes();
        ILoadable second = new MemoryLoadable(url, new Date(), secondContent);
        storage.put(second);
        ILoadable secondGot = storage.get(url);
        assertEquals(secondGot.getContent(), secondContent);
    }
}
