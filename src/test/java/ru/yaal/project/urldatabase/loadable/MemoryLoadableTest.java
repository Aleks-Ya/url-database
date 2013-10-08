package ru.yaal.project.urldatabase.loadable;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Date;

public class MemoryLoadableTest {
    private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("spring-config.xml");

    @Test(expectedExceptions = BeanCreationException.class)
    public void urlIsNull() throws Exception {
        CONTEXT.getBean("memoryLoadable", null, new Date(), "a".getBytes());
    }

    @Test(expectedExceptions = BeanCreationException.class)
    public void dateIsNull() throws Exception {
        CONTEXT.getBean("memoryLoadable", new URL("http://ya.ru"), null, "a".getBytes());
    }

    @Test
    public void contentIsEmpty() throws Exception {
        CONTEXT.getBean("memoryLoadable", new URL("http://ya.ru"), new Date(), new byte[0]);
    }

}
