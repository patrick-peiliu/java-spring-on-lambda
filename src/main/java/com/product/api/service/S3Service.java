package com.product.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile file) throws Exception {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String shortTimestamp = timestamp.substring(Math.max(timestamp.length() - 6, 0));
            String originalFilename = "file";
            if (StringUtils.isNotEmpty(file.getOriginalFilename())) {
                originalFilename = file.getOriginalFilename();
            }
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));

            String filePath = fileName + "-" + shortTimestamp + fileExtension;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());

            // Set the appropriate Content-Type
            String contentType = file.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/octet-stream";
            }
            metadata.setContentType(contentType);
            metadata.setHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\"");
            // view in the browser
            // Explicitly set Content-Disposition to inline
//            metadata.setHeader("Content-Disposition", "inline");

            PutObjectRequest request = new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(request);

            return amazonS3.getUrl(bucketName, filePath).toString();
        } catch (IOException e) {
            // Log the error and/or wrap it in a custom exception
            throw new Exception("Failed to upload file to S3", e);
        }
    }
}
