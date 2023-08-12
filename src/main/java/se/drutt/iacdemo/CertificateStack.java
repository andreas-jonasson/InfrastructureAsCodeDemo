package se.drutt.iacdemo;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneAttributes;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.constructs.Construct;

public class CertificateStack extends Stack
{
    public String certificateArn;

    public CertificateStack(final Construct scope, final String id, final StackProps props, Configuration conf)
    {

        super(scope, id, props);

        final IHostedZone zone =
                HostedZone.fromHostedZoneAttributes(this, "HostedZoneLookup",
                        HostedZoneAttributes.builder()
                                .hostedZoneId(conf.HOSTED_ZONE_ID)
                                .zoneName(conf.DNS_DOMAIN)
                                .build());

        // TLS certificate
        Certificate certificate = Certificate.Builder.create(this, "SiteCertificate")
                .domainName(conf.FRONTEND_DOMAIN_NAME)
                .validation(CertificateValidation.fromDns(zone))
                .build();

        certificateArn = certificate.getCertificateArn();

        CfnOutput.Builder.create(this, "Certificate")
                .description("Certificate ARN")
                .value(certificateArn)
                .build();
    }

}
