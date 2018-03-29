package service.discovery.example.service;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
public interface SimpleService {
    String SERVICE_NAME = "SimpleService";
    String SERVICE_ADDRESS = "simple.service";

    @Fluent
    SimpleService ping(Handler<AsyncResult<String>> handler);
}
