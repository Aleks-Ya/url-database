package ru.yaal.project.urldatabase;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static ru.yaal.project.urldatabase.ActualFile.ACTUAL_CODE;

public class ActualFileTest {
    private final Random random = new Random();
    private File file;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        file = File.createTempFile("actual_", ".tmp");
    }

    @Test
    public void getActual() throws Exception {
        byte[] expData = getRandomBytes(100);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            out.write(expData);
            out.write(ACTUAL_CODE);
        }
        ActualFile actualFile = new ActualFile(file);
        byte[] actData = actualFile.getContent();
        assertEquals(actData, expData);
        assertTrue(!file.exists());
    }

    private byte[] getRandomBytes(int length) {
        int dataSize = random.nextInt(length);
        byte[] expData = new byte[dataSize];
        random.nextBytes(expData);
        return expData;
    }

    @Test(expectedExceptions = NotActualContentException.class)
    public void getNotActual() throws Exception {
        byte[] expData = getRandomBytes(100);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            out.write(expData);
        }
        ActualFile actualFile = new ActualFile(file);
        actualFile.getContent();
    }

    @Test(expectedExceptions = NotActualContentException.class)
    public void fileNotExists() throws Exception {
        ActualFile actualFile = new ActualFile(new File("not_exists"));
        actualFile.getContent();
    }

    @Test(expectedExceptions = NotActualContentException.class)
    public void dataSizeLessCodeSize() throws Exception {
        final int dataSize = 5;
        assertTrue(dataSize < ACTUAL_CODE.length);
        byte[] expData = getRandomBytes(dataSize);
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            out.write(expData);
        }
        ActualFile actualFile = new ActualFile(file);
        actualFile.getContent();
    }

    @Test
    public void save() throws IOException, NotActualContentException {
        byte[] expData = getRandomBytes(100);
        ActualFile actualFile = new ActualFile(file);
        actualFile.saveContent(expData);
        byte[] actData = actualFile.getContent();
        assertEquals(actData, expData);
        assertTrue(!file.exists());
    }

    @AfterMethod
    public void afterMethod() {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

}
