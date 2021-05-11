package myorg;

import software.amazon.awscdk.core.App;

public class CursoAwsCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        var vpcStack = new CursoAwsVpcStack(app, "Vpc");

        var clusterStack = new CursoAwsClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        var rdsStack = new CursoAwsRdsStack(app, "Rds", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        var snsStack = new CursoAwsSnsStack(app, "Sns");

        var ddbStack = new CursoAwsDdbStack(app, "Ddb");

        var invoiceStack = new CursoAwsInvoiceAppStack(app, "InvoiceApp");

        var service01 = new CursoAwsService01Stack(app, "Service01", clusterStack.getCluster(),
                snsStack.getProductEventsTopic(), invoiceStack.getBucket(), invoiceStack.getS3InvoiceQueue());
        service01.addDependency(clusterStack);
        service01.addDependency(rdsStack);
        service01.addDependency(snsStack);
        service01.addDependency(invoiceStack);

        var service02 = new CursoAwsService02Stack(app, "Service02", clusterStack.getCluster(),
                snsStack.getProductEventsTopic(), ddbStack.getProductEventDdb());
        service02.addDependency(clusterStack);
        service02.addDependency(snsStack);
        service02.addDependency(ddbStack);
        app.synth();
    }
}
