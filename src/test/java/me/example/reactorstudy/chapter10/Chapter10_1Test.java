package me.example.reactorstudy.chapter10;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLOutput;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
class Chapter10_1Test {

    @Test
    void test1() throws InterruptedException {
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
                .doOnNext(integer -> log.info("# doOnNext : {}", integer))
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
            .map(number -> number + 10)
                .doOnNext(integer -> log.info("# doOnNext2 : {} ", integer))
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe2"))
            .subscribe(integer -> log.info("# Completed : {} ", integer));

        Thread.sleep(1000);
    }

    @Test
    void test2() throws InterruptedException {
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
            .subscribeOn(Schedulers.single())
            .doOnNext(integer -> log.info("# doOnNext : {}", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
            .map(number -> number + 10)
            .doOnNext(integer -> log.info("# doOnNext2 : {} ", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe2"))
            .subscribe(integer -> log.info("# Completed : {} ", integer));

        Thread.sleep(1000);
    }

    @Test
    void test3() throws InterruptedException {
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
            .doOnNext(integer -> log.info("# doOnNext : {}", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
            .map(number -> number + 10)
            .doOnNext(integer -> log.info("# doOnNext2 : {} ", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe2"))
            .subscribeOn(Schedulers.single())
            .subscribe(integer -> log.info("# Completed : {} ", integer));

        Thread.sleep(1000);
    }

    @Test
    void test4() throws InterruptedException {
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
            .doOnNext(integer -> log.info("# doOnNext : {}", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
            .subscribeOn(Schedulers.single())
            .map(number -> number + 10)
            .doOnNext(integer -> log.info("# doOnNext2 : {} ", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe2"))
            .subscribe(integer -> log.info("# Completed : {} ", integer));

        Thread.sleep(1000);
    }


    @Test
    void test5() throws InterruptedException {
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
            .doOnNext(integer -> log.info("# doOnNext : {}", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
            .subscribeOn(Schedulers.single())
            .map(number -> number)
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(integer -> log.info("# doOnNext2 : {} ", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe2"))
            .subscribe(integer -> log.info("# Completed : {} ", integer));

        Thread.sleep(1000);
    }


    @Test
    void test6() throws InterruptedException {
        Flux.fromArray(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 })
            .doOnNext(integer -> log.info("# doOnNext : {}", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
            .subscribeOn(Schedulers.boundedElastic())
            .map(number -> number)
            .doOnNext(integer -> log.info("# doOnNext2 : {} ", integer))
            .doOnSubscribe(subscription -> log.info("# doOnSubscribe2"))
            .subscribe(integer -> log.info("# Completed : {} ", integer));

        Thread.sleep(1000);
    }

}
