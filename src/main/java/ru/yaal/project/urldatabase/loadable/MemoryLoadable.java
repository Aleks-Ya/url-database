package ru.yaal.project.urldatabase.loadable;

import java.net.URL;
import java.util.Date;

/**
 * Стандартная реализация (хранение в памяти).
 */
class MemoryLoadable extends AbstractLoadable {

    public MemoryLoadable(URL url, Date loadDate, byte[] content) {
        setUrl(url);
        setLoadDate(loadDate);
        setContent(content);
    }
}
