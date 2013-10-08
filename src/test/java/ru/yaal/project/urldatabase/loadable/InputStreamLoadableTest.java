package ru.yaal.project.urldatabase.loadable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static java.lang.String.format;
import static org.testng.Assert.assertEquals;

public class InputStreamLoadableTest {
    private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("spring-config.xml");

    @Test
    public void readFromInputStream() throws Exception {
        final String url = "http://ya.ru";
        final String date = "06.10.2013 17:57:55";
        final String content = "fuck off";
        String data = format("%s\n%s\n%s", url, date, content);
        InputStream is = new ByteArrayInputStream(data.getBytes());
        ILoadable loadable = (ILoadable) CONTEXT.getBean("inputStreamLoadable", is);
        assertEquals(loadable.getUrl(), new URL(url));
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();//инъекция DI
        assertEquals(loadable.getLoadDate(), dateFormat.parse(date));
        assertEquals(loadable.getContent(), content.getBytes());
    }
}
