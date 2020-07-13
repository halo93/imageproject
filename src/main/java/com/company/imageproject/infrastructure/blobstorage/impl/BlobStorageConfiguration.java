package com.company.imageproject.infrastructure.blobstorage.impl;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.company.imageproject.config.AwsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
class BlobStorageConfiguration {

    @Bean
    TransferManager buildTransferManager(AwsProperties awsProperties, AWSCredentialsProvider awsCredentialsProvider) {
        return TransferManagerBuilder.standard().withS3Client(buildAwsS3Client(awsProperties, awsCredentialsProvider)).build();
    }

    private AmazonS3 buildAwsS3Client(AwsProperties awsProperties, AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonS3ClientBuilder.standard()
            .withRegion(awsProperties.getRegion())
            .withCredentials(awsCredentialsProvider)
            .build();

    }

    @Bean
    AWSS3Facade awsS3Facade(TransferManager transferManager) {
        return new AWSS3Facade(transferManager);
    }

}
