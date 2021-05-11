package myorg;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.rds.*;

import java.util.Collections;

public class CursoAwsRdsStack extends Stack {
    public CursoAwsRdsStack(final Construct scope, final String id, Vpc vpc) {
        this(scope, id, vpc, null);
    }

    public CursoAwsRdsStack(final Construct scope, final String id, Vpc vpc, final StackProps props) {
        super(scope, id, props);
        CfnParameter databasePassword = CfnParameter.Builder.create(this, "databasePassword")
                .type("String")
                .description("The RDS instance password")
                .build();
        ISecurityGroup iSecurityGroup = SecurityGroup.fromSecurityGroupId(this, id, vpc.getVpcDefaultSecurityGroup());
        iSecurityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(3306));

        DatabaseInstance databaseInstance = DatabaseInstance.Builder
                .create(this, "Rds01")
                .instanceIdentifier("aws-project01-db")
                .engine(DatabaseInstanceEngine.mysql(MySqlInstanceEngineProps.builder().version(MysqlEngineVersion.VER_5_6).build()))
                .vpc(vpc)
                .credentials(Credentials.fromUsername("admin", CredentialsFromUsernameOptions.builder().password(SecretValue.plainText(databasePassword.getValueAsString())).build()))
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .multiAz(false)
                .allocatedStorage(10)
                .securityGroups(Collections.singletonList(iSecurityGroup))
                .vpcSubnets(SubnetSelection.builder().subnets(vpc.getPrivateSubnets()).build())
                .build();

        CfnOutput.Builder.create(this, "rds-endpoint")
                .exportName("rds-endpoint")
                .value(databaseInstance.getDbInstanceEndpointAddress())
                .build();

        CfnOutput.Builder.create(this, "rds-password")
                .exportName("rds-password")
                .value(databasePassword.getValueAsString())
                .build();


    }
}
