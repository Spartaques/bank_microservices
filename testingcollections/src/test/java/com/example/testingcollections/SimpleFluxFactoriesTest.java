package com.example.testingcollections;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Log4j2
public class SimpleFluxFactoriesTest {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    @Test
    public void simple() {
        Flux<Integer> integers = Flux.create(emitter -> this.launch(emitter, 5));
        StepVerifier
                .create(integers.doFinally(signalType -> this.executorService.shutdown()
                ))
                .expectNextCount(5)//
                .verifyComplete();
    }

    private void launch(FluxSink<Integer> integerFluxSink, int count) {
        this.executorService.submit(() -> {
            var integer = new AtomicInteger();
            Assertions.assertNotNull(integerFluxSink);
            while (integer.get() < count) {
                double random = Math.random();
                integerFluxSink.next(integer.incrementAndGet());
                this.sleep((long) (random * 1_000));
            }
            integerFluxSink.complete();
        });
    }
    private void sleep(long s) {
        try {
            Thread.sleep(s);
        }
        catch (Exception e) {
            log.error(e);
        }
    }
}
