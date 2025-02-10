package app.rafo.bs_personal_finance_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/uploads")
public class UploadController {

    private static final String UPLOADS_DIR_IMAGES = "uploads/images/";
    private static final String UPLOADS_DIR_PDFS = "uploads/pdfs/";
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("png", "jpg", "jpeg", "gif");
    private static final List<String> ALLOWED_PDF_EXTENSIONS = List.of("pdf");

    /**
     * Endpoint para subir imágenes.
     *
     * @param file Archivo enviado desde el frontend.
     * @return URL relativa donde se almacenó la imagen.
     */
    @PostMapping("/images")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return handleFileUpload(file, UPLOADS_DIR_IMAGES, ALLOWED_IMAGE_EXTENSIONS, "imagen");
    }

    /**
     * Endpoint para subir PDFs.
     *
     * @param file Archivo enviado desde el frontend.
     * @return URL relativa donde se almacenó el PDF.
     */
    @PostMapping("/pdfs")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        return handleFileUpload(file, UPLOADS_DIR_PDFS, ALLOWED_PDF_EXTENSIONS, "PDF");
    }

    /**
     * Lógica genérica para manejar la subida de archivos.
     *
     * @param file           Archivo enviado desde el frontend.
     * @param uploadDir      Directorio donde se guardará el archivo.
     * @param allowedExtensions Lista de extensiones permitidas.
     * @param fileType       Tipo de archivo (para mensajes).
     * @return URL relativa donde se almacenó el archivo.
     */
    private ResponseEntity<String> handleFileUpload(MultipartFile file, String uploadDir, List<String> allowedExtensions, String fileType) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se recibió ningún archivo.");
        }

        // Validar extensión del archivo
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!allowedExtensions.contains(fileExtension)) {
            return ResponseEntity.badRequest().body("El archivo no es un " + fileType + " válido.");
        }

        try {
            // Crear la carpeta si no existe
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Guardar el archivo con un nombre único
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());

            // Generar la URL relativa
            String fileUrl = "/uploads/" + uploadDir.replace("uploads/", "") + fileName;
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar el archivo.");
        }
    }

    /**
     * Obtiene la extensión del archivo.
     *
     * @param fileName Nombre del archivo.
     * @return Extensión del archivo (en minúsculas).
     */
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }
}