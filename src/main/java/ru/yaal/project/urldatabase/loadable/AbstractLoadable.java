package ru.yaal.project.urldatabase.loadable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import static java.lang.String.format;

public abstract class AbstractLoadable implements ILoadable {
    private static final String BAD_URL_MESSAGE = "Illegal URL: %s";
    private URL url;
    private byte[] content;
    private Date loadDate;

    public URL getUrl() {
        return url;
    }

    protected final void setUrl(URL url) {
        if (url == null) {
            throw new IllegalArgumentException(format(BAD_URL_MESSAGE, url));
        }
        this.url = url;
    }

    protected void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException | NullPointerException e) {
            throw new IllegalArgumentException(format(BAD_URL_MESSAGE, url));
        }
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

    protected final void setLoadDate(Date loadDate) {
        if (loadDate == null) {
            throw new IllegalArgumentException(format("Load date can't be null."));
        }
        this.loadDate = loadDate;
    }

    @Override
    public final boolean equals(Object otherObj) {
        if (otherObj instanceof ILoadable) {
            ILoadable other = (ILoadable) otherObj;
            return getUrl().equals(other.getUrl())
                    && getLoadDate().equals(other.getLoadDate())
                    && Arrays.equals(getContent(), other.getContent());
        } else {
            return false;
        }
    }
}