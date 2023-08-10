package se.drutt.iacdemo;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.s3.IBucket;
import software.constructs.Construct;

public class WebContent extends Stack
{
    public WebContent(final Construct scope, final String id) {
        this(scope, id, null, null);
    }

    public WebContent(final Construct scope, final String id, final StackProps props, IBucket webBucket)
    {
        super(scope, id, props);
    }
}