package service.discovery.example.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ServiceBinder;
import service.discovery.example.Main;

import java.util.ArrayList;
import java.util.List;

public class SimpleServiceVerticle extends AbstractVerticle {
    private ServiceDiscovery discovery;
    private List<Record> records = new ArrayList<>();

    @Override
    public void start() throws Exception {
        discovery = ServiceDiscovery.create(vertx);
        addRecords(10);
    }

    @Override
    public void stop() throws Exception {
        for (Record record : records) {
            discovery.unpublish(record.getRegistration(), result -> {
            });
        }
    }

    private void addRecords(int count) {
        for (int i = 0; i < count; i++) {
            addRecord(i + 1);
        }
    }

    private void addRecord(int number) {
        new ServiceBinder(vertx).setAddress(SimpleService.SERVICE_ADDRESS).register(SimpleService.class, new SimpleServiceImpl(number));
        Record record = EventBusService.createRecord(
                SimpleService.SERVICE_NAME,
                SimpleService.SERVICE_ADDRESS,
                SimpleService.class);
        discovery.publish(record, result -> {
            if (result.succeeded()) {
                log(SimpleService.SERVICE_NAME + " published - " + this);
                records.add(record);
            } else {
                log(SimpleService.SERVICE_NAME + " doesn't published. Cause - " + result.cause());
            }
        });
    }

    private void log(String message) {
        Main.log(SimpleServiceImpl.class.getSimpleName(), message);
    }
}
