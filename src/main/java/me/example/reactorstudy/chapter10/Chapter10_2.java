package me.example.reactorstudy.chapter10;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Chapter10_2 {

    private static Integer[] createInteger() {
        log.info("createInteger");
        return new Integer[] { 1, 3, 5, 6 };
    }

    public static void main(String[] args) throws InterruptedException {

        Flux.fromArray(createInteger())
            .doOnNext(data -> log.info("# doOnNext: {}", data))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe. it means flux are started to be subscribed."))
            .publishOn(Schedulers.boundedElastic())
            .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(1000);
    }
}

// publishOn()은 Downstream으로 Signale을 전송할 때 실행되는 스레드를 선택함.
// publishOn() 이후의 코드만 스케쥴러에서 실행됨. 따라서 아래와 같이 실행됨.
// onNext(), onComplete() and onError() 같은 메서드들이 쓰레드 워커에서 실행되도록 도와줌.
/*
22:42:55.492 [main] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- createInteger
22:42:55.557 [main] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- # doOnSubscribe. it means flux are started to be subscribed.
22:42:55.559 [main] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- # doOnNext: 1
22:42:55.559 [main] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- # doOnNext: 3
22:42:55.559 [main] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- # doOnNext: 5
22:42:55.559 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- # onNext: 1
22:42:55.559 [main] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- # doOnNext: 6
22:42:55.559 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- # onNext: 3
22:42:55.560 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- # onNext: 5
22:42:55.560 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_2 -- # onNext: 6
 */
