package service.discovery.example.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import service.discovery.example.Main;
import service.discovery.example.service.SimpleService;

public class SimpleClientVerticle extends AbstractVerticle {
    private ServiceDiscovery discovery;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx
                .setTimer(1000, timer -> {
                    init();
                    startFuture.complete();
                });
    }

    private void init() {
        discovery = ServiceDiscovery.create(vertx);
        discovery.getRecords(new JsonObject().put("name", SimpleService.SERVICE_NAME), result -> {
            if (result.succeeded() && !result.result().isEmpty()) {
                this.log("Founded " + SimpleService.SERVICE_NAME + " - " + result.result().size());
                SimpleService service = discovery.getReference(result.result().get(0))
                        .getAs(SimpleService.class);
                vertx.setPeriodic(500, time -> {
                    this.log("Current time is - " + time);
                    service.ping(serviceResult -> {
                        if (serviceResult.succeeded()) {
                            this.log("" + SimpleService.SERVICE_NAME + " response - " + serviceResult.result());
                        } else {
                            this.log("" + SimpleService.SERVICE_NAME + " doesn't response.");
                        }
                    });
                });
            } else {
                this.log("Can't find " + SimpleService.SERVICE_NAME + "");
            }
        });
    }

    private void log(String message) {
        Main.log(SimpleClientVerticle.class.getSimpleName(), message);
    }
}