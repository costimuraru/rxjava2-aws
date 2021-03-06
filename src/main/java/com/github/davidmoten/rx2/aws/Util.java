package com.github.davidmoten.rx2.aws;

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
