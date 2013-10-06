package ru.yaal.project.urldatabase.loadable;

import java.net.URL;
import java.util.Date;

/**
 * Интерфейс объектов, которые можно хранить в БД.
 * User: Aleks
 * Date: 06.10.13
 */
public interface ILoadable {
    URL getUrl();
    byte[] getContent();
    Date getLoadDate();
}
