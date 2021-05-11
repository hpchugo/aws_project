package myorg;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.dynamodb.*;

public class CursoAwsDdbStack extends Stack {

    private final Table productEventDdb;

    public CursoAwsDdbStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CursoAwsDdbStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        productEventDdb = Table.Builder.create(this, "ProductEventDdb")
                .tableName("product-events")
                .billingMode(BillingMode.PROVISIONED)
                .readCapacity(1)
                .writeCapacity(1)
                .partitionKey(Attribute.builder()
                        .name("pk")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("sk")
                        .type(AttributeType.STRING)
                        .build())
                .timeToLiveAttribute("ttl")
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
        productEventDdb.autoScaleReadCapacity(EnableScalingProps.builder()
                .minCapacity(1)
                .maxCapacity(4)
                .build())
                .scaleOnUtilization(UtilizationScalingProps.builder()
                        .targetUtilizationPercent(50)
                        .scaleInCooldown(Duration.seconds(30))
                        .scaleOutCooldown(Duration.seconds(30))
                        .build());
        productEventDdb.autoScaleWriteCapacity(EnableScalingProps.builder()
                .minCapacity(1)
                .maxCapacity(4)
                .build())
                .scaleOnUtilization(UtilizationScalingProps.builder()
                        .targetUtilizationPercent(50)
                        .scaleInCooldown(Duration.seconds(30))
                        .scaleOutCooldown(Duration.seconds(30))
                        .build());
    }

    public Table getProductEventDdb() {
        return productEventDdb;
    }
}
