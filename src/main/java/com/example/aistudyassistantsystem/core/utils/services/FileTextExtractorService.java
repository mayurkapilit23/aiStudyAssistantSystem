package com.example.aistudyassistantsystem.core.utils.services;

import com.example.aistudyassistantsystem.core.exception.ResourceNotFoundException;
import com.example.aistudyassistantsystem.features.document.entity.Document;
import com.example.aistudyassistantsystem.features.document.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class FileTextExtractorService {

    private final DocumentRepository documentRepository;
    public String extractText(Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Document not found"
                        )
                );

        File file = new File(document.getFilePath());

        String extension = getFileExtension(
                document.getFileName()
        ).toLowerCase();

        try {

            return switch (extension) {

                case "pdf" -> extractPdfText(file);

                case "docx" -> extractDocxText(file);

                case "txt", "md" -> extractPlainText(file);

                case "pptx" -> extractPptxText(file);

                case "png",
                     "jpg",
                     "jpeg" -> extractImageText(file);

                default -> throw new RuntimeException(
                        "Unsupported file type"
                );
            };

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to extract text: "
                            + e.getMessage()
            );
        }
    }

    // PDF
    private String extractPdfText(File file)
            throws Exception {

        PDDocument document = PDDocument.load(file);

        PDFTextStripper stripper =
                new PDFTextStripper();

        String text = stripper.getText(document);

        document.close();

        return text;
    }

    // DOCX
    private String extractDocxText(File file)
            throws Exception {

        FileInputStream fis =
                new FileInputStream(file);

        XWPFDocument document =
                new XWPFDocument(fis);

        XWPFWordExtractor extractor =
                new XWPFWordExtractor(document);

        String text = extractor.getText();

        extractor.close();
        document.close();

        return text;
    }

    // TXT + MD
    private String extractPlainText(File file)
            throws Exception {

        return Files.readString(file.toPath());
    }

    // PPTX
    private String extractPptxText(File file)
            throws Exception {

        FileInputStream fis =
                new FileInputStream(file);

        XMLSlideShow slideShow =
                new XMLSlideShow(fis);

        StringBuilder builder =
                new StringBuilder();

        for (XSLFSlide slide : slideShow.getSlides()) {

            for (XSLFShape shape : slide.getShapes()) {

                if (shape instanceof XSLFTextShape textShape) {

                    builder.append(
                            textShape.getText()
                    ).append("\n");
                }
            }
        }

        slideShow.close();

        return builder.toString();
    }

    // IMAGE OCR
    private String extractImageText(File file)
            throws Exception {

        Tesseract tesseract =
                new Tesseract();

        // Set installed tesseract path
        tesseract.setDatapath(
                "/usr/local/share/tessdata"
        );

        return tesseract.doOCR(file);
    }

    private String getFileExtension(
            String fileName
    ) {

        int lastIndex =
                fileName.lastIndexOf(".");

        if (lastIndex == -1) {
            return "";
        }

        return fileName.substring(lastIndex + 1);
    }

}
