package com.product.api.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {
    private static final Logger LOG = LogManager.getLogger();
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile file) throws Exception {
        try {
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String uniqueFileName = generateUniqueFileName(fileExtension);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            // Set the appropriate Content-Type
            String contentType = file.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/octet-stream";
            }
            metadata.setContentType(contentType);
            metadata.setHeader("Content-Disposition", "attachment; filename=\"" + uniqueFileName + "\"");
            // view in the browser
            // Explicitly set Content-Disposition to inline
//            metadata.setHeader("Content-Disposition", "inline");

            PutObjectRequest request = new PutObjectRequest(bucketName, uniqueFileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(request);

            String fileUrl = amazonS3.getUrl(bucketName, uniqueFileName).toString();
            LOG.info("File successfully uploaded: fileName={}, url={}", uniqueFileName, fileUrl);
            return fileUrl;
        } catch (IOException e) {
            // Log the error and/or wrap it in a custom exception
            throw new Exception("Failed to upload file to S3", e);
        }
    }

    public String generatePresignedUrl(String fileName, String fileType) {
        // Generate a unique file name
        String uniqueFileName = generateUniqueFileName(FilenameUtils.getExtension(fileName));

        // Generate the pre-signed URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, uniqueFileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(new Date(System.currentTimeMillis() + 3600 * 1000 * 7)); // 7 hours expiration
        
        generatePresignedUrlRequest.setContentType(fileType);

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        LOG.info("Pre-signed URL generated: fileName={}, url={}", uniqueFileName, url.toString());
        return url.toString();
    }

    private String generateUniqueFileName(String fileExtension) {
        ZoneId aucklandZone = ZoneId.of("Pacific/Auckland");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = ZonedDateTime.now(aucklandZone).format(formatter);
        String uniqueId = UUID.randomUUID().toString().substring(0, 4);
        return timestamp + "_" + uniqueId + "." + fileExtension;
    }
}
