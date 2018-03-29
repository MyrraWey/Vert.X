package service.discovery.example;

import com.hazelcast.config.Config;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import service.discovery.example.client.SimpleClientVerticle;
import service.discovery.example.service.SimpleServiceVerticle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void log(String logName, String message) {
        Logger.getLogger(logName).log(Level.INFO, message);
    }

    public static void main(String[] args) {
        Vertx.clusteredVertx(
                new VertxOptions()
                        .setClusterManager(new HazelcastClusterManager(new Config())),
                result -> {
                    if (result.succeeded()) {
                        start(result.result());
                    }
                });
    }

    private static void start(Vertx vertx) {
        vertx.deployVerticle(
                SimpleServiceVerticle.class,
                new DeploymentOptions());
        vertx.deployVerticle(
                SimpleClientVerticle.class,
                new DeploymentOptions());
    }
}
