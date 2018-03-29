package service.discovery.example.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import service.discovery.example.Main;

public class SimpleServiceImpl implements SimpleService {

    private final int number;

    public SimpleServiceImpl(int number) {
        this.number = number;
    }

    @Override
    public SimpleService ping(Handler<AsyncResult<String>> handler) {
        log("pong - " + this);
        handler.handle(Future.succeededFuture("pong(" + number + "): " + System.currentTimeMillis()));
        return this;
    }

    private void log(String message) {
        Main.log(SimpleServiceImpl.class.getSimpleName(), message);
    }
}
