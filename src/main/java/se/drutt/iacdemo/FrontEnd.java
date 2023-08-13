package se.drutt.iacdemo;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.ICertificate;
import software.amazon.awscdk.services.cloudfront.*;
import software.amazon.awscdk.services.iam.CanonicalUserPrincipal;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.route53.*;
import software.amazon.awscdk.services.route53.targets.CloudFrontTarget;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;
import software.constructs.Construct;

import java.util.List;

/*
 * Kudos to: https://github.com/aws-samples/aws-cdk-examples/blob/master/java/static-site/src/main/java/software/amazon/awscdk/examples/StaticSiteStack.java
 */

public class FrontEnd extends Stack {
    public IBucket webBucket;
    public CloudFrontWebDistribution distribution;

    public FrontEnd(final Construct scope, final String id, final StackProps props, Configuration conf, String certificateArn) {
        super(scope, id, props);

        webBucket =
                Bucket.Builder.create(this, "SiteBucket")
                        .bucketName(conf.FRONTEND_DOMAIN_NAME)
                        .websiteIndexDocument("index.html")
                        .websiteErrorDocument("error.html")
                        .publicReadAccess(false)
                        .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
                        .removalPolicy(RemovalPolicy.DESTROY)
                        .autoDeleteObjects(true)
                        .build();

        IOriginAccessIdentity originAccessIdentity = new OriginAccessIdentity(this, "CloudfrontOAI",
                OriginAccessIdentityProps.builder()
                        .comment("OAI for " + conf.DNS_SUBDOMAIN)
                        .build());

        PolicyStatement allowGet = new PolicyStatement(PolicyStatementProps.builder()
                .actions(List.of("s3:GetObject"))
                .resources(List.of(webBucket.arnForObjects("*")))
                .principals(List.of(new CanonicalUserPrincipal(((OriginAccessIdentity) originAccessIdentity).getCloudFrontOriginAccessIdentityS3CanonicalUserId()))).build());

        webBucket.addToResourcePolicy(allowGet);

        final IHostedZone zone =
                HostedZone.fromHostedZoneAttributes(this, "HostedZoneLookup",
                        HostedZoneAttributes.builder()
                                .hostedZoneId(conf.HOSTED_ZONE_ID)
                                .zoneName(conf.DNS_DOMAIN)
                                .build());

        List<String> siteDomainList = List.of(conf.FRONTEND_DOMAIN_NAME);

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
        final ICertificate certificate = Certificate.fromCertificateArn(this, "WebCertificate", certificateArn);

        CfnOutput.Builder.create(this, "Certificate")
                .description("Certificate ARN")
                .value(certificate.getCertificateArn())
                .build();

        SourceConfiguration sourceConfiguration = SourceConfiguration.builder()
                .s3OriginSource(S3OriginConfig.builder()
                        .originAccessIdentity(originAccessIdentity)
                        .s3BucketSource(webBucket)
                        .build())
                .behaviors(List.of(Behavior.builder()
                        .compress(true)
                        .allowedMethods(CloudFrontAllowedMethods.GET_HEAD_OPTIONS)
                        .viewerProtocolPolicy(ViewerProtocolPolicy.REDIRECT_TO_HTTPS)
                        .isDefaultBehavior(true)
                        .build()))
                .build();

        distribution = CloudFrontWebDistribution.Builder.create(this, "SiteDistribution")
                .viewerCertificate(ViewerCertificate.fromAcmCertificate(certificate, ViewerCertificateOptions.builder()
                        .aliases(siteDomainList)
                        .sslMethod(SSLMethod.SNI)
                        .securityPolicy(SecurityPolicyProtocol.TLS_V1_2_2021)
                        .build()
                ))
                .defaultRootObject("index.html")
                .errorConfigurations(List.of(CfnDistribution.CustomErrorResponseProperty.builder()
                        .errorCode(403)
                        .responseCode(403)
                        .responsePagePath("/error.html")
                        .errorCachingMinTtl(30)
                        .build()))
                .originConfigs(List.of(sourceConfiguration))
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
