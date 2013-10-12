package ru.yaal.project.urldatabase.loadable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import static org.testng.Assert.assertEquals;

public class AbstractLoadableTest {
    private GenericXmlApplicationContext testContext;

    @BeforeClass
    public void beforeClass() throws MalformedURLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        testContext = new GenericXmlApplicationContext();
        testContext.load("classpath:test-spring-config.xml");
        testContext.setParent(context);
        testContext.refresh();
    }

    @Test
    public void testEquals() throws Exception {
        URL url = new URL("http://ya.ru");
        Date date = new Date();
        byte[] content = "content".getBytes();
        ILoadable first = (ILoadable) testContext.getBean("memoryLoadable", url, date, content);
        ILoadable second = (ILoadable) testContext.getBean("memoryLoadable", url, date, content);
        assertEquals(first, second);
    }

    @Test
    public void setDateMillisecTruncation() throws MalformedURLException {
        ILoadable loadable = (ILoadable) testContext.getBean("testLoadable");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(loadable.getLoadDate());
        assertEquals(calendar.get(Calendar.MILLISECOND), 0);
    }
}
