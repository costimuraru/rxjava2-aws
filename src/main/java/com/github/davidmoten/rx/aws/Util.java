package com.github.davidmoten.rx.aws;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;

final class Util {

    private Util() {
        // prevent instantiation
    }

    // visible for testing
    static void shutdown(AmazonS3 client) {
        try {
            client.shutdown();
        } catch (final RuntimeException e) {
            // ignore
        }
    }

    static void shutdown(AmazonSQS client) {
        try {
            client.shutdown();
        } catch (final RuntimeException e) {
            // ignore
        }
    }

}