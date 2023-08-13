package se.drutt.iacdemo;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cloudfront.CloudFrontWebDistribution;
import software.amazon.awscdk.services.s3.IBucket;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.ISource;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

import java.util.ArrayList;
import java.util.List;

public class WebContent extends Stack
{
    public WebContent(final Construct scope, final String id, final StackProps props, IBucket webBucket, CloudFrontWebDistribution distribution)
    {
        super(scope, id, props);

        List<ISource> sources = new ArrayList<>(1);
        sources.add(Source.asset("./web"));

        BucketDeployment.Builder.create(this, "DeployWithInvalidation")
                .sources(sources)
                .destinationBucket(webBucket)
                .distribution(distribution)
                .build();
    }
}