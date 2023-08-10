package se.drutt.iacdemo;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketAccessControl;
import software.amazon.awscdk.services.s3.IBucket;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.Properties;


public class FrontEnd extends Stack
{
    public IBucket webBucket;
    public FrontEnd(final Construct scope, final String id) {
        this(scope, id, null, null);
    }

    public FrontEnd(final Construct scope, final String id, final StackProps props, String domainName)
    {
        super(scope, id, props);

        webBucket =
                Bucket.Builder.create(this, "SiteBucket")
                        .bucketName(domainName)
                        .websiteIndexDocument("index.html")
                        .websiteErrorDocument("error.html")
                        .publicReadAccess(false)
                        .removalPolicy(RemovalPolicy.DESTROY)
                        .build();
    }
}
