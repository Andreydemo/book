package com.epam.cdp.web.controller;

import com.epam.cdp.service.FileEntitySaverService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import static com.epam.cdp.util.FileUtils.storeFile;

@Controller
@RequestMapping("upload")
public class UploadFileController {
    private static final Logger logger = Logger.getLogger(UploadFileController.class);
    private FileEntitySaverService entitySaver;
    private String uploadDirectory;

    @Autowired
    public UploadFileController(FileEntitySaverService entitySaver, @Value("${file.uploadDirectory}") String uploadDirectory) {
        this.entitySaver = entitySaver;
        this.uploadDirectory = uploadDirectory;
    }

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty())
            return "file is empty.";
        try {
            storeFile(file, uploadDirectory);
            entitySaver.save(uploadDirectory + "/" + file.getOriginalFilename());
        } catch (Exception e) {
            logger.error("File cannot be stored, message: " + e.getMessage());
            return "file was not uploaded";
        }
        return "file was successfully uploaded";
    }
}