package se.drutt.iacdemo;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.route53.*;
import software.amazon.awscdk.services.route53.targets.ApiGateway;
import software.constructs.Construct;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BackEnd extends Stack
{
    public BackEnd(final Construct scope, final String id, final StackProps props, Configuration conf, String certificateArn)
    {
        super(scope, id, props);

        //=======================================================================
        // DynamoDB table for storing question cards
        //=======================================================================
        final Table table = new Table(this, "CardTable", TableProps.builder()
                .tableName(conf.CARD_TABLE_NAME)
                .removalPolicy(RemovalPolicy.DESTROY)
                .deletionProtection(false)
                .partitionKey(Attribute.builder()
                        .name(conf.CARD_PARTITION_KEY)
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name(conf.CARD_SORT_KEY)
                        .type(AttributeType.NUMBER)
                        .build())
                .build());

        //=======================================================================
        // Role for AWS Lambda
        //=======================================================================
        IPrincipal lambdaPrincipal = new ServicePrincipal("lambda.amazonaws.com");

        Role lambdaApiRole = new Role(this, "LambdaRole", RoleProps.builder()
                .assumedBy(lambdaPrincipal)
                .build());

        //=======================================================================
        // Attaching a role that will allow AWS Lambda to access logs
        //=======================================================================
        lambdaApiRole.addToPolicy(new PolicyStatement(PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .actions(Arrays.asList("logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents"))
                .resources(List.of("*"))
                .build()
        ));

        lambdaApiRole.addToPolicy(new PolicyStatement(PolicyStatementProps.builder()
                .effect(Effect.ALLOW)
                .actions(Arrays.asList("route53:ChangeResourceRecordSets", "route53:ListResourceRecordSets"))
                .resources(List.of(conf.HOSTED_ZONE_ARN))
                .build()
        ));

        // Lambda function for get
        Function cardLambda = new Function(this, "CardLambda", FunctionProps.builder()
                .code(Code.fromAsset("./lambda/target/lambda-0.1.jar"))
                .handler("se.drutt.iacdemo.lambda.CardHandler::handleRequest")
                .functionName("iac-demo-card-lambda")
                .runtime(Runtime.JAVA_11)
                .role(lambdaApiRole)
                .timeout(Duration.seconds(300))
                .memorySize(1024)
                .environment(Map.of("DRUTT", "Drutt"))
                .build());

        CfnOutput.Builder.create(this, "seven-days-lambda-output")
                .description("ARN ServerLambda")
                .value(cardLambda.getFunctionArn())
                .build();

        //=======================================================================
        // Create and API Gateway with an post-interface to the
        // reminder-lambdas.
        //=======================================================================

        final IHostedZone zone =
                HostedZone.fromHostedZoneAttributes(this, "HostedZoneLookup",
                        HostedZoneAttributes.builder()
                                .hostedZoneId(conf.HOSTED_ZONE_ID)
                                .zoneName(conf.DNS_DOMAIN)
                                .build());

        final Certificate certificate = Certificate.Builder.create(this, "ApiCertificate")
                .domainName(conf.API_DOMAIN_NAME)
                .validation(CertificateValidation.fromDns(zone))
                .build();

        final RestApi api =
                RestApi.Builder.create(this, "ApiGateway")
                        .restApiName("Infrastructure as Code Demo API")
                        .description("API for the infrastructure as code demo. It has one endpoint: 'card' - Sending a CardRequest returns question cards.")
                        .domainName(DomainNameOptions.builder()
                                .domainName(conf.API_DOMAIN_NAME)
                                .certificate(certificate)
                                .build())
                        .defaultCorsPreflightOptions(CorsOptions.builder()
                                .allowHeaders(Arrays.asList("Content-Type", "X-Amz-Date", "Authorization", "X-Api-Key", "Access-Control-Allow-Origin"))
                                .allowMethods(Arrays.asList("OPTIONS", "GET", "POST", "PUT", "DELETE"))
                                .allowCredentials(true)
                                .allowOrigins(Collections.singletonList("*"))
                                .build())
                        .build();

        Integration getReminderIntegration = new LambdaIntegration(cardLambda);
        software.amazon.awscdk.services.apigateway.Resource startS = api.getRoot().addResource(conf.API_CARD_ENDPOINT);
        startS.addMethod("POST", getReminderIntegration, MethodOptions.builder()
                .apiKeyRequired(false)
                .build());

        CfnOutput.Builder.create(this, "CardEndpointOutput")
                .description("Endpoint for CardRequests")
                .value(api.urlForPath("/" + conf.API_CARD_ENDPOINT))
                .build();


        ARecord.Builder.create(this, "APiAliasRecord")
                .recordName(conf.API_DOMAIN_NAME)
                .target(RecordTarget.fromAlias(new ApiGateway(api)))
                .zone(zone)
                .build();
    }
}
