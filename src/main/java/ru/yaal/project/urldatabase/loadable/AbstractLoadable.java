package ru.yaal.project.urldatabase.loadable;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static java.lang.String.format;

abstract class AbstractLoadable implements ILoadable {
    private static final String BAD_URL_MESSAGE = "Illegal URL: %s";
    private static final String BAD_LOAD_DATE_MESSAGE = "Illegal load date: %s";
    private DateFormat dateFormat;
    private URL url;
    private byte[] content;
    private Date loadDate;

    public URL getUrl() {
        return url;
    }

    protected void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException | NullPointerException e) {
            throw new IllegalArgumentException(format(BAD_URL_MESSAGE, url));
        }
    }

    protected final void setUrl(URL url) {
        if (url == null) {
            throw new IllegalArgumentException(format(BAD_URL_MESSAGE, url));
        }
        this.url = url;
    }

    public byte[] getContent() {
        return content;
    }

    protected final void setContent(byte[] content) {
        this.content = content;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    /**
     * Устанавливает дату загрузки.
     * Миллисекунды округляются до 0.
     */
    protected final void setLoadDate(String loadDate) {
        try {
            setLoadDate(dateFormat.parse(loadDate));
        } catch (ParseException | NullPointerException e) {
            throw new IllegalArgumentException(format(BAD_LOAD_DATE_MESSAGE, loadDate));
        }
    }

    protected final void setLoadDate(Date loadDate) {
        if (loadDate == null) {
            throw new IllegalArgumentException(format(BAD_LOAD_DATE_MESSAGE, loadDate));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(loadDate);
        calendar.set(Calendar.MILLISECOND, 0);
        this.loadDate = calendar.getTime();
    }

    @Override
    public final boolean equals(Object otherObj) {
        if (otherObj instanceof ILoadable) {
            ILoadable other = (ILoadable) otherObj;
            return getUrl().equals(other.getUrl())
                    && isDatesEquals(getLoadDate(), other.getLoadDate())
                    && Arrays.equals(getContent(), other.getContent());
        } else {
            return false;
        }
    }

    /**
     * Даты равны, если отличаются не больше чем на 1 сек.
     * Введено, п.ч. при сохранении в файл теряются миллисекунды.
     */
    private boolean isDatesEquals(Date first, Date second) {
        long long1 = first.getTime();
        long long2 = second.getTime();
        return Math.abs(long1 - long2) < 1000;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public final String toString() {
        return format("Loadable[%s]", getUrl());
    }
}
