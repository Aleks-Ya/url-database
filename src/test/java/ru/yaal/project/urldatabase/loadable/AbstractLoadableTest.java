package ru.yaal.project.urldatabase.loadable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Date;

import static org.testng.Assert.assertEquals;

public class AbstractLoadableTest {
    private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("spring-config.xml");

    @Test
    public void testEquals() throws Exception {
        URL url = new URL("http://ya.ru");
        Date date = new Date();
        byte[] content = "content".getBytes();
        ILoadable first = (ILoadable) CONTEXT.getBean("memoryLoadable", url, date, content);
        ILoadable second = (ILoadable) CONTEXT.getBean("memoryLoadable", url, date, content);
        assertEquals(first, second);
    }
}
