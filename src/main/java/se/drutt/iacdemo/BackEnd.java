package se.drutt.iacdemo;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;
import software.constructs.Construct;

public class BackEnd extends Stack
{
    public BackEnd(final Construct scope, final String id, final StackProps props, Configuration conf)
    {
        super(scope, id, props);

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
    }
}
