package ru.yaal.project.urldatabase;

import org.testng.annotations.Test;
import ru.yaal.project.urldatabase.loadable.FileLoadable;

import java.io.BufferedReader;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileLoadableTest {
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void urlIsNull() throws Exception {
        BufferedReader br = mock(BufferedReader.class);
        when(br.readLine()).thenReturn(null);//Нарушено ограничение URL = null
        new FileLoadable(br).getUrl();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void dateIsNull() throws Exception {
        BufferedReader br = mock(BufferedReader.class);
        when(br.readLine()).thenReturn("http://ya.ru", null);//Нарушено ограничение loadDate = null
        new FileLoadable(br).getUrl();
    }
}
