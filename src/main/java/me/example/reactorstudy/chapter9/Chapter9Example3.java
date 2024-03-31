package me.example.reactorstudy.chapter9;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.ManySpec;
import reactor.core.publisher.Sinks.One;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Chapter9Example3 {

    public static void main(String[] args) throws InterruptedException {

        final One<String> sinksOne = Sinks.one();
        final Mono<String> mono = sinksOne.asMono();

        sinksOne.emitValue("Hello Reactor", EmitFailureHandler.FAIL_FAST);

        // Mono이기 때문에 하나의 데이터만 전달됨.
        // Debug를 찍으면 reactor.core.publisher.Operator - onNextDropped : Hi Reactor라고 뜬다.
        sinksOne.emitValue("Hi Reactor", EmitFailureHandler.FAIL_FAST);

        mono.subscribe(data -> log.info("# Subscriber1 {}", data));
        mono.subscribe(data -> log.info("# Subscriber2 {}", data));
    }

}

// 별도의 쓰레드를 사용하지 않았기 때문에 Main 쓰레드에서 이 작업을 모두 처리한다.
/*
23:16:00.178 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example3 -- # Subscriber1 Hello Reactor
23:16:00.181 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example3 -- # Subscriber2 Hello Reactor
*/
