package me.example.reactorstudy.chapter10;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Chapter10_1 {

    private static Integer[] createInteger() {
        log.info("createInteger");
        return new Integer[] { 1, 3, 5, 6 };
    }

    public static void main(String[] args) throws InterruptedException {


        // PublishOn() 0번 사용한 경우.
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5 })
            .doOnNext(data -> log.info("# doOnNext fromArray: {}", data))
            .filter(data -> data > 3)
            .doOnNext(data -> log.info("# doOnNext filter: {}", data))
            .map(data -> data * 10)
            .doOnNext(data -> log.info("# doOnNext map: {}", data))
            .subscribe(data -> log.info("# onNext: {}", data));

        // PublishOn() 1번 사용한 경우.
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5 })
            .doOnNext(data -> log.info("# doOnNext fromArray: {}", data))
            .publishOn(Schedulers.single())
            .filter(data -> data > 3)
            .doOnNext(data -> log.info("# doOnNext filter: {}", data))
            .map(data -> data * 10)
            .doOnNext(data -> log.info("# doOnNext map: {}", data))
            .subscribe(data -> log.info("# onNext: {}", data));

        // PublishOn() 2번 사용한 경우.
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5 })
            .doOnNext(data -> log.info("# doOnNext fromArray: {}", data))
            .publishOn(Schedulers.single())
            .filter(data -> data > 3)
            .doOnNext(data -> log.info("# doOnNext filter: {}", data))
            .publishOn(Schedulers.boundedElastic())
            .map(data -> data * 10)
            .doOnNext(data -> log.info("# doOnNext map: {}", data))
            .subscribe(data -> log.info("# onNext: {}", data));

        // SubscribeOn + PublishOn() 사용한 경우.
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5 })
            .doOnNext(data -> log.info("# doOnNext fromArray: {}", data))
            .subscribeOn(Schedulers.boundedElastic())
            .filter(data -> data > 3)
            .doOnNext(data -> log.info("# doOnNext filter: {}", data))
            .publishOn(Schedulers.single())
            .map(data -> data * 10)
            .doOnNext(data -> log.info("# doOnNext map: {}", data))
            .subscribe(data -> log.info("# onNext: {}", data));

        // PublishOn() 사용한 경우.
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5 })
            .publishOn(Schedulers.single())
            .doOnNext(data -> log.info("# doOnNext: {}", data))
            .doOnSubscribe(
                    subscription -> log.info("# doOnSubscribe. it means flux are started to be subscribed."))
            .subscribe(data -> log.info("# onNext: {}", data));




        Flux.fromArray(createInteger())
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(data -> log.info("# doOnNext: {}", data))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe. it means flux are started to be subscribed."))
            .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(1000);
    }
}

// subscribeOn()은 구독 직후 사용되는 쓰레드를 정한다.
// 1. doOnSubscribe()는 main 쓰레드에서 실행됨. 최초 실행 스레드가 Main이기 때문임.
// 2. 이후 코드는 boundedElstaice 스케쥴러에서 모두 처리됨.
// 즉,
/*
22:40:35.740 [main] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- createInteger
22:40:35.810 [main] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- # doOnSubscribe. it means flux are started to be subscribed.
22:40:35.812 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- # doOnNext: 1
22:40:35.813 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- # onNext: 1
22:40:35.813 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- # doOnNext: 3
22:40:35.813 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- # onNext: 3
22:40:35.813 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- # doOnNext: 5
22:40:35.813 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- # onNext: 5
22:40:35.813 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- # doOnNext: 6
22:40:35.813 [boundedElastic-1] INFO me.example.reactorstudy.chapter10.Chapter10_1 -- # onNext: 6
 */
