package me.example.reactorstudy.chapter7;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

// 1. 결론은 share()만 쓴다고 HotSequence가 되는 것은 아닌 것 같음.
// 2. delayElements() Operator의 쓰레드 스케쥴러가 Parallel임. (Reactor는 어떤 쓰레드 스케쥴러를 쓰느냐에 따라 다르게 동작하는 것일 수도 있음.)
@Slf4j
public class Chapter7Example1 {

    public static void coldSeqExample() throws InterruptedException {
        final Flux<String> coldFlux = Flux.fromIterable(List.of("Korea", "Japan", "China"))
                                          .map(s -> s.toLowerCase());

        coldFlux.subscribe(country -> log.info("Subscribe1 : {}", country));
        Thread.sleep(2000);

        coldFlux.subscribe(country -> log.info("Subscribe1 : {}", country));
        Thread.sleep(2000);

        // 모두 Main 쓰레드에서 시작함.
        // Cold Sequence임.
        // 09:52:15.300 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : korea
        // 09:52:15.302 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : japan
        // 09:52:15.302 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : china
        // 09:52:17.309 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : korea
        // 09:52:17.310 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : japan
        // 09:52:17.310 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : china
    }


    public static void hotSeqExample1() throws InterruptedException {
        Flux<String> hotFlux = Flux.fromIterable(List.of("Korea", "Japan", "China"))
                                   .map(s -> s.toLowerCase())
                                   .delayElements(Duration.ofSeconds(1))
                                   .share();

        hotFlux.subscribe(country -> log.info("Subscribe1 : {}", country));
        Thread.sleep(2000);

        hotFlux.subscribe(country -> log.info("Subscribe1 : {}", country));
        Thread.sleep(2000);

        // Delay가 있는 경우, 모두 Parallel 쓰레드에서 실행됨.
        // 마치 Hot Sequence처럼 동작함.
        // 09:50:22.197 [parallel-1] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : korea
        // 09:50:23.203 [parallel-2] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : japan
        // 09:50:23.204 [parallel-2] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : japan
        // 09:50:24.205 [parallel-3] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : china
        // 09:50:24.206 [parallel-3] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : china
    }

    public static void hotSeqExample2() throws InterruptedException {
        Flux<String> hotFlux = Flux.fromIterable(List.of("Korea", "Japan", "China"))
                                   .map(s -> s.toLowerCase())
                                   .share();

        hotFlux.subscribe(country -> log.info("Subscribe1 : {}", country));
        Thread.sleep(2000);

        hotFlux.subscribe(country -> log.info("Subscribe1 : {}", country));
        Thread.sleep(2000);

        // Delay가 없는 경우, 모두 main 쓰레드에서 실행됨.
        // 마치 Cold Sequence처럼 동작함.
        // 09:50:25.204 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : korea
        // 09:50:25.204 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : japan
        // 09:50:25.204 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : china
        // 09:50:27.205 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : korea
        // 09:50:27.205 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : japan
        // 09:50:27.206 [main] INFO me.example.reactorstudy.chapter7.Chapter7Example1 -- Subscribe1 : china
    }



    public static void main(String[] args) throws InterruptedException {

        log.info("**** ColdSeqExample1 ****");
        coldSeqExample();

        log.info("**** HotSeqExample1 ****");
        hotSeqExample1();

        log.info("**** HotSeqExample2 ****");
        hotSeqExample2();
    }

}
