package ru.yaal.project.urldatabase.index;

import java.io.*;

/**
 * Класс позволяет узнать был ли файл в прошлый раз сохранен корректно.
 * Предназначен для хранения индексов базы данных.
 * Если будет обнаружено, что файл был сохранен некорректно, индекс будет перестроен.
 * User: Aleks
 * Date: 27.09.13
 */
public class ActualFile {
    static final byte[] ACTUAL_CODE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private File file;

    public ActualFile(File file) {
        this.file = file;
    }

    /**
     * Читает в память содержимое файла, сохраненного методом #saveContent().
     * После прочтения удаляет файл, т.к. его содержимое считается устаревшим.
     *
     * @return byte[] Содержимое файла очищенное от контрольной информации.
     * @throws IOException               Ошибки при чтении файла.
     * @throws NotActualContentException Файл был сохранен некорректно при предыдущем обращении.
     */
    byte[] getContent() throws IOException, NotActualContentException {
        if (!file.exists()) {
            throw new NotActualContentException();
        }
        int size = (int) file.length();
        int codeLength = ACTUAL_CODE.length;
        if (size < codeLength) {
            throw new NotActualContentException();
        }
        byte[] data = new byte[size - codeLength];
        byte[] code = new byte[codeLength];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            bis.read(data);
            bis.read(code);
            bis.close();
            if (!arrayEquals(ACTUAL_CODE, code)) {
                throw new NotActualContentException();
            }
            file.delete();
            return data;
        }
    }

    private boolean arrayEquals(byte[] first, byte[] second) {
        if (first.length != second.length) {
            return false;
        }
        for (int i = 0; i < first.length; i++) {
            if (first[i] != second[i]) return false;
        }
        return true;
    }

    /**
     * Сохраняет массив байтов в файл. Добавляет контрольную последовательность в конце файла, чтобы
     * проверить корректность сохранения при чтении методом #getContent().
     */
    void saveContent(byte[] data) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            bos.write(data);
            bos.write(ACTUAL_CODE);
        }
    }

}
