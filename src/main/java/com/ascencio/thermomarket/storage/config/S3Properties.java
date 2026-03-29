package com.ascencio.thermomarket.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "s3")
public record S3Properties(
        String bucketName,
        String region,
        String publicBaseUrl
) {
}
