package com.epam.cdp.util;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static void storeFile(MultipartFile file, String uploadDirectory) throws IOException {
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(new File(uploadDirectory + "/" + file.getOriginalFilename())));
        FileCopyUtils.copy(file.getInputStream(), stream);
        stream.close();
    }
}
