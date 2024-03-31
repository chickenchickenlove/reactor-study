package me.example.reactorstudy.chapter9;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Chapter9Example4 {

    public static void main(String[] args) throws InterruptedException {
        // Hot Sequence의 WarmUp 특성으로 동작함.
        // Cold Sequence를 원한다면, multicast() ->replay()
        final Many<String> many = Sinks.many()
                                       .multicast()
                                       .onBackpressureBuffer();


        final Flux<String> multicastFlux = many.asFlux();

        many.emitNext("1", EmitFailureHandler.FAIL_FAST);
        many.emitNext("2", EmitFailureHandler.FAIL_FAST);

        multicastFlux.subscribe(data -> log.info("# Subscribe1 : {}", data));
        multicastFlux.subscribe(data -> log.info("# Subscribe2 : {}", data));

        many.emitNext("3", EmitFailureHandler.FAIL_FAST);

        Thread.sleep(2000);
    }
}

// Warm-Up seq때문에 Subscribe는 1 ~ 3을 다 받음.
// 반면 Subiscribe2는 구독 직후 발행된 '3'만 받음.
/*
23:31:54.246 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example4 -- # Subscribe1 : 1
23:31:54.251 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example4 -- # Subscribe1 : 2
23:31:54.251 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example4 -- # Subscribe1 : 3
23:31:54.251 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example4 -- # Subscribe2 : 3
 */
