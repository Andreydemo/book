package com.epam.cdp.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

public class FileUtils {
    public static void storeFile(MultipartFile file, String uploadDirectory) throws IOException {
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(new File(uploadDirectory + "/" + file.getOriginalFilename())));
        FileCopyUtils.copy(file.getInputStream(), stream);
        stream.close();
    }

    public static void writeInPdfToStream(Object object, OutputStream stream) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 8);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(100, 700);

        if (object instanceof List) {
            List list = (List) object;
            for (Object obj : list) {
                contentStream.showText(obj.toString());
                contentStream.newLine();
            }
        } else {
            contentStream.showText(object.toString());
        }

        contentStream.endText();
        contentStream.close();
        document.save(stream);
        document.close();
    }
}
