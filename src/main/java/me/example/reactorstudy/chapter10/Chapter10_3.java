package me.example.reactorstudy.chapter10;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Chapter10_3 {

    private static Integer[] createInteger() {
        log.info("createInteger");
        return new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,21, 22 };
    }

    public static void main(String[] args) throws InterruptedException {

        Flux.fromArray(createInteger())
            .parallel()
                .runOn(Schedulers.parallel())
            .doOnNext(data -> log.info("# doOnNext: {}", data))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe. it means flux are started to be subscribed."))
            .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(1000);
    }
}

// 1. parallel()은 ParallelFlux를 반환함. 이 때, 데이터를 CPU에서 제공하는 물리 쓰레드 개수만큼 나누어 rail을 생성함.
// 2. 생성된 rail을 실행하기 위해서는 runOn() 메서드를 이용해 parallel 스케쥴러에서 실행되도록 해야함.
// 3. runOn()으로 실행하지 않는 경우 그냥 메인 쓰레드에서 실행됨.publishOn() 이후의 코드만 스케쥴러에서 실행됨. 따라서 아래와 같이 실행됨.
//
/*
22:49:11.634 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- createInteger
22:49:11.709 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.711 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.711 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.711 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.711 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.711 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.711 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.711 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.711 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.711 [main] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnSubscribe. it means flux are started to be subscribed.
22:49:11.712 [parallel-3] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 3
22:49:11.712 [parallel-6] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 6
22:49:11.712 [parallel-4] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 4
22:49:11.712 [parallel-5] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 5
22:49:11.712 [parallel-7] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 7
22:49:11.713 [parallel-6] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 6
22:49:11.713 [parallel-3] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 3
22:49:11.712 [parallel-1] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 1
22:49:11.713 [parallel-6] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 16
22:49:11.713 [parallel-3] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 13
22:49:11.712 [parallel-10] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 10
22:49:11.712 [parallel-8] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 8
22:49:11.713 [parallel-1] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 1
22:49:11.713 [parallel-10] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 10
22:49:11.712 [parallel-9] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 9
22:49:11.713 [parallel-4] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 4
22:49:11.713 [parallel-9] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 9
22:49:11.713 [parallel-5] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 5
22:49:11.713 [parallel-9] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 19
22:49:11.713 [parallel-7] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 7
22:49:11.713 [parallel-5] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 15
22:49:11.713 [parallel-9] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 19
22:49:11.712 [parallel-2] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 2
22:49:11.713 [parallel-5] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 15
22:49:11.713 [parallel-3] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 13
22:49:11.713 [parallel-2] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 2
22:49:11.713 [parallel-6] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 16
22:49:11.713 [parallel-8] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 8
22:49:11.713 [parallel-1] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 11
22:49:11.713 [parallel-8] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 18
22:49:11.713 [parallel-10] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 20
22:49:11.713 [parallel-8] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 18
22:49:11.713 [parallel-4] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 14
22:49:11.713 [parallel-10] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 20
22:49:11.713 [parallel-4] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 14
22:49:11.713 [parallel-7] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 17
22:49:11.713 [parallel-7] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 17
22:49:11.713 [parallel-2] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 12
22:49:11.713 [parallel-1] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 11
22:49:11.713 [parallel-2] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 12
22:49:11.713 [parallel-1] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 21
22:49:11.713 [parallel-1] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 21
22:49:11.713 [parallel-2] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # doOnNext: 22
22:49:11.713 [parallel-2] INFO me.example.reactorstudy.chapter10.Chapter10_3 -- # onNext: 22
 */
