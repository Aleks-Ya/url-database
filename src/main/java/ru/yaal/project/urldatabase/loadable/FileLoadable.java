package ru.yaal.project.urldatabase.loadable;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;

/**
 * Объект БД, представленный файлом на диске.
 * User: Aleks
 * Date: 06.10.13
 */
public class FileLoadable extends AbstractLoadable {
    private final BufferedReader bufferedReader;
    private final DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();//инъекция DI
    private boolean loaded = false;

    public FileLoadable(File file) throws FileNotFoundException {
        this(new BufferedReader(new FileReader(file)));
    }

    public FileLoadable(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    private void load() {
        loaded = true;
        try (BufferedReader bir = bufferedReader) {
            setUrl(bir.readLine());
            String dateStr = null;
            try {
                dateStr = bir.readLine();
                setLoadDate(dateFormat.parse(dateStr));
            } catch (ParseException | NullPointerException e) {
                throw new IllegalArgumentException(format("Incorrect Date: %s", dateStr));
            }
            //todo заменить алгоримт чтения по символам на чтение по байтам
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bir.readLine()) != null) {
                builder.append(line);
            }
            setContent(builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();  //todo добавить логгер
        }
    }

    @Override
    public URL getUrl() {
        if (!loaded) {
            load();
        }
        return super.getUrl();
    }

    @Override
    public byte[] getContent() {
        if (!loaded) {
            load();
        }
        return super.getContent();
    }

    @Override
    public Date getLoadDate() {
        if (!loaded) {
            load();
        }
        return super.getLoadDate();
    }
}
