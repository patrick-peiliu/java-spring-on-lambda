package com.product.api.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;
import net.coobird.thumbnailator.Thumbnails;

public class ImageCompressor {
    public static String compressImageToBase64(MultipartFile imageFile, float quality) throws IOException {
        String formatName = getFormatName(imageFile.getOriginalFilename());

        BufferedImage image;
        if ("heic".equals(formatName)) {
            byte[] imageBytes = imageFile.getBytes();
            image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                throw new IllegalArgumentException("Failed to read the HEIC image file: image is null");
            }
        } else {
            image = ImageIO.read(imageFile.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("Failed to read the image file: image is null");
            }
        }

        ByteArrayOutputStream compressedImageStream = new ByteArrayOutputStream();
        Thumbnails.of(image)
                .scale(1.0)
                .outputQuality(quality)
                .outputFormat(formatName)
                .toOutputStream(compressedImageStream);

        byte[] compressedImageBytes = compressedImageStream.toByteArray();
        return Base64.getEncoder().encodeToString(compressedImageBytes);
    }


    public static String compressBase64String(String base64String) throws IOException {
        // Decode Base64 string to byte array
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        // Compress byte array using GZIP
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(decodedBytes);
        }

        // Encode compressed byte array back to Base64 string
        byte[] compressedBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(compressedBytes);
    }

    private static String getFormatName(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "jpeg";
            case "png":
                return "png";
            case "gif":
                return "gif";
            case "bmp":
                return "bmp";
            case "heic":
                return "heic";
            default:
                throw new IllegalArgumentException("Unsupported image format: " + extension);
        }
    }
}
