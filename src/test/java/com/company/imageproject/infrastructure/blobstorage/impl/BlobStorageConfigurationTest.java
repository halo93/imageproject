package com.company.imageproject.infrastructure.blobstorage.impl;

import akka.http.scaladsl.Http;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.company.imageproject.config.AwsProperties;
import com.company.imageproject.infrastructure.blobstorage.api.BlobStorageProperties;
import io.findify.s3mock.S3Mock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;

@Configuration
@Profile("test")
public class BlobStorageConfigurationTest {

    @Bean
    public TransferManager buildTransferManager(BlobStorageProperties blobStorageProperties, AwsProperties awsProperties, AWSCredentialsProvider awsCredentialsProvider) {
        return TransferManagerBuilder.standard()
            .withS3Client(buildAwsS3Client(blobStorageProperties, awsProperties, awsCredentialsProvider))
            .build();
    }

    private AmazonS3 buildAwsS3Client(BlobStorageProperties blobStorageProperties, AwsProperties awsProperties, AWSCredentialsProvider awsCredentialsProvider) {
        Http.ServerBinding binding = startS3MockApi();
        EndpointConfiguration endpointConfiguration = new EndpointConfiguration(
            String.format("http://localhost:%s", binding.localAddress().getPort()),
            awsProperties.getRegion());
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withPathStyleAccessEnabled(Boolean.TRUE)
            .withEndpointConfiguration(endpointConfiguration)
            .withCredentials(awsCredentialsProvider)
            .build();
        s3Client.createBucket(blobStorageProperties.getFileStorage().getBucketName());
        return s3Client;
    }

    private Http.ServerBinding startS3MockApi() {
        S3Mock api = new S3Mock.Builder().withPort(0).withInMemoryBackend().build();
        return api.start();
    }

    @Bean
    public AWSS3Service awsS3Service(TransferManager transferManager) {
        return new AWSS3Service(transferManager);
    }
}
