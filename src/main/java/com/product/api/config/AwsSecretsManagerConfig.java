package com.product.api.config;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.Subsegment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



@Configuration
public class AwsSecretsManagerConfig {
    private static final Logger LOG = LogManager.getLogger();
    private Region region = Region.US_EAST_2;

    @Bean
    public SecretsManagerClient secretsManagerClient() {
        SecretsManagerClient client = null;
        try {
            client = SecretsManagerClient.builder()
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .region(region)
                    .build();
        } catch (Exception e) {
            LOG.error("Error occurred: {}", e.getMessage());
        }
        return client;
    }

    @Bean
    public Map<String, String> secretsManagerConfig(SecretsManagerClient secretsManagerClient, @Value("${aws.secretName}") String secretName) {
        AWSXRayRecorder xrayRecorder = AWSXRay.getGlobalRecorder();
        Segment segment = xrayRecorder.beginSegment("GetSecretSegment");
        String secret = "";

        try {
            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse getSecretValueResponse;

            try {
                Subsegment subsegment = xrayRecorder.beginSubsegment("SecretsManager");
                getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
                subsegment.close();
            } catch (Exception e) {
                // For a list of exceptions thrown, see
                // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
                throw e;
            }

            secret = getSecretValueResponse.secretString();
            LOG.info("aws secret is: {}", secret);
        } finally {
            segment.close();
        }
        try {
            // Assuming the secret is stored as a JSON string
            return new ObjectMapper().readValue(secret, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}