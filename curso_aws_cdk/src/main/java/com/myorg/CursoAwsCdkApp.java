package com.myorg;

import software.amazon.awscdk.App;


public class CursoAwsCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        var vpcStack = new VpcStack(app, "Vpc");

        var clusterStack = new ClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        var rdsStack = new RdsStack(app, "Rds", vpcStack.getVpc());
        rdsStack.addDependency(vpcStack);

        var snsStack = new SnsStack(app, "Sns");

        var service01Stack = new Service01Stack(app, "Service01",
                clusterStack.getCluster(),snsStack.getProductEventsTopic());
        service01Stack.addDependency(clusterStack);
        service01Stack.addDependency(rdsStack);
        service01Stack.addDependency(snsStack);

        var service02Stack = new Service02Stack(app, "Service02", clusterStack.getCluster(), snsStack.getProductEventsTopic());

        service02Stack.addDependency(clusterStack);
        service02Stack.addDependency(snsStack);

        app.synth();
    }
}

