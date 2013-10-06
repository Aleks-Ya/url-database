package ru.yaal.project.urldatabase;

import org.testng.annotations.Test;
import ru.yaal.project.urldatabase.loadable.MemoryLoadable;

import java.net.URL;
import java.util.Date;

public class MemoryLoadableTest {
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void urlIsNull() throws Exception {
        new MemoryLoadable(null, new Date(), "a".getBytes());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void dateIsNull() throws Exception {
        new MemoryLoadable(new URL("http://ya.ru"), null, "a".getBytes());
    }

    @Test
    public void contentIsEmpty() throws Exception {
        new MemoryLoadable(new URL("http://ya.ru"), new Date(), new byte[0]);
    }

}
