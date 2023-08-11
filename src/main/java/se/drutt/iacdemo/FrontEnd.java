package se.drutt.iacdemo;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;
import software.amazon.awscdk.services.certificatemanager.ICertificate;
import software.amazon.awscdk.services.cloudfront.*;
import software.amazon.awscdk.services.route53.*;
import software.amazon.awscdk.services.route53.targets.CloudFrontTarget;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;
import software.constructs.Construct;

import java.util.ArrayList;
import java.util.List;

/*
 * Kudos to: https://github.com/aws-samples/aws-cdk-examples/blob/master/java/static-site/src/main/java/software/amazon/awscdk/examples/StaticSiteStack.java
 */

public class FrontEnd extends Stack
{
    public IBucket webBucket;
    public CloudFrontWebDistribution distribution;

    public FrontEnd(final Construct scope, final String id, final StackProps props, Configuration conf)
    {
        super(scope, id, props);

        webBucket =
                Bucket.Builder.create(this, "SiteBucket")
                        .bucketName(conf.FRONTEND_DOMAIN_NAME)
                        .websiteIndexDocument("index.html")
                        .websiteErrorDocument("error.html")
                        .publicReadAccess(false)
                        .removalPolicy(RemovalPolicy.DESTROY)
                        .build();

        final IHostedZone zone =
                HostedZone.fromHostedZoneAttributes(this, "HostedZoneLookup",
                        HostedZoneAttributes.builder()
                                .hostedZoneId(conf.HOSTED_ZONE_ID)
                                .zoneName(conf.DNS_DOMAIN)
                                .build());

        List<String> siteDomainList = new ArrayList<>(1);
        siteDomainList.add(conf.FRONTEND_DOMAIN_NAME);

        // Site URL CfnOutput variable
        CfnOutput.Builder.create(this, "SiteOutput")
                .description("Site Domain Url")
                .value("https://" + conf.FRONTEND_DOMAIN_NAME)
                .build();

        CfnOutput.Builder.create(this, "BucketOutput")
                .description("Bucket Name")
                .value(webBucket.getBucketName())
                .build();

        // TLS certificate
        final ICertificate certificate = Certificate.Builder.create(this, "SiteCertificate")
                .domainName(conf.FRONTEND_DOMAIN_NAME)
                .validation(CertificateValidation.fromDns(zone))
                .build();

        CfnOutput.Builder.create(this, "Certificate")
                .description("Certificate ARN")
                .value(certificate.getCertificateArn())
                .build();

        // CloudFront distribution that provides HTTPS
        List<Behavior> behavioursList = new ArrayList<>(1);
        behavioursList.add(Behavior.builder().isDefaultBehavior(true).build());

        List<SourceConfiguration> sourceConfigurationsList = new ArrayList<>(1);
        sourceConfigurationsList.add(
                SourceConfiguration.builder()
                        .s3OriginSource(S3OriginConfig.builder().s3BucketSource(webBucket).build())
                        .behaviors(behavioursList)
                        .build());

        distribution = CloudFrontWebDistribution.Builder.create(this, "SiteDistribution")
                .viewerCertificate(ViewerCertificate.fromAcmCertificate(certificate, ViewerCertificateOptions.builder()
                                .aliases(siteDomainList)
                                .sslMethod(SSLMethod.SNI)
                                .securityPolicy(SecurityPolicyProtocol.TLS_V1_2_2021)
                                .build()
                ))
                .originConfigs(sourceConfigurationsList)
                .build();

        CfnOutput.Builder.create(this, "DistributionId")
                .description("CloudFront Distribution Id")
                .value(distribution.getDistributionId())
                .build();

        // Route53 alias record for the CloudFront distribution
        ARecord.Builder.create(this, "SiteAliasRecord")
                .recordName(conf.FRONTEND_DOMAIN_NAME)
                .target(RecordTarget.fromAlias(new CloudFrontTarget(distribution)))
                .zone(zone)
                .build();
    }
}
