package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class CursoAwsCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        var vpcStack = new VpcStack(app, "Vpc");

        var clusterStack = new ClusterStack(app, "Cluster", vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        app.synth();
    }
}

