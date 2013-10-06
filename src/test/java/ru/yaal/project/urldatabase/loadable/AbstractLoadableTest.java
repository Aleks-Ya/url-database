package ru.yaal.project.urldatabase.loadable;

import org.testng.annotations.Test;

import java.net.URL;
import java.util.Date;

import static org.testng.Assert.assertEquals;

public class AbstractLoadableTest {
    @Test
    public void testEquals() throws Exception {
        URL url = new URL("http://ya.ru");
        Date date = new Date();
        byte[] content = "contetn".getBytes();
        ILoadable first = new MemoryLoadable(url, date, content);
        ILoadable second = new MemoryLoadable(url, date, content);
        assertEquals(first, second);
    }
}
