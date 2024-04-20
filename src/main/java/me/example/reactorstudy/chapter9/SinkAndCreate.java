package me.example.reactorstudy.chapter9;

import java.util.stream.IntStream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.ManySpec;
import reactor.core.publisher.Sinks.UnicastSpec;

public class SinkAndCreate {

    public static void main(String[] args) throws InterruptedException {
        test();
        test2();


    }


    static void test() {
        Flux.create(emitter -> {
                IntStream.range(0, 10).parallel().forEach(i -> {
                    if (!emitter.isCancelled()) {
                        emitter.next(i);
                    }
                });
                emitter.complete();
            })
            .subscribe(
                    data -> System.out.println("Received: " + data),
                    error -> error.printStackTrace(),
                    () -> System.out.println("Completed")
            );
    }

    static void test2() throws InterruptedException {
        Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
        Flux<Integer> integerFlux = unicastSink.asFlux();


        integerFlux
                .subscribe(
                        data -> System.out.println("Received: " + data),
                        error -> error.printStackTrace(),
                        () -> System.out.println("Completed"));


        IntStream.range(0, 10)
                .parallel()
                .forEach(i -> {
                    try {
                        new Thread(() -> unicastSink.emitNext(i, EmitFailureHandler.FAIL_FAST)).run();
                    } catch (Exception e) {
                        System.out.println("1");
                    }
                });

        unicastSink.tryEmitComplete();
        Thread.sleep(10000L);
    }

}
