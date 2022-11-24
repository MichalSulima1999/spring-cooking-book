package com.example.recipebook.utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUploadUtilTest {
    private final String testFileSaveDirectory = "test-directory/";
    private final String testFileName = "testFile.png";

    @Test
    void saveFile() throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                testFileName,
                testFileName,
                MediaType.IMAGE_PNG_VALUE,
                new byte[1]
        );

        FileUploadUtil.saveFile(testFileSaveDirectory, testFileName, file);

        File f = new File(testFileSaveDirectory + testFileName);
        assertTrue(f.isFile());
    }

    @Test
    void deleteFile() throws IOException {
        FileUploadUtil.deleteFile(testFileSaveDirectory, testFileName);

        File f = new File(testFileSaveDirectory + testFileName);
        assertFalse(f.isFile());
    }
}