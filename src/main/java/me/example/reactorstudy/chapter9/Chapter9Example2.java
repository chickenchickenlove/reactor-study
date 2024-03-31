package me.example.reactorstudy.chapter9;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.Many;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Chapter9Example2 {

    public static void main(String[] args) throws InterruptedException {

        int tasks = 6;

        /*
        ManySpec many = Sinks.many();
        UnicastSpec unicast = many.unicast();
        Many<String> unicastSink = unicast.onBackpressureBuffer();
         */

        final Many<String> unicastSink = Sinks.many()
                                       .unicast()
                                       .onBackpressureBuffer();

        final Flux<String> fluxView = unicastSink.asFlux();
        IntStream
                .range(1, tasks)
                .forEach(n -> {
                             try {
                                 new Thread(() -> {
                                     unicastSink.emitNext(doTask(n), EmitFailureHandler.FAIL_FAST);
                                 }).start();
                                 Thread.sleep(100L);
                             } catch (InterruptedException e) {
                                 log.error(e.getMessage());
                             }
                         }
                );

        fluxView
                .publishOn(Schedulers.parallel())
                .map(result -> result + " success!")
                .doOnNext(n -> log.info("# map(): {}", n))
                .publishOn(Schedulers.parallel())
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(200L);

        try {
            fluxView
                    .subscribe(data -> log.info("# onNext: {}", data));
        } catch (IllegalStateException e) {
            // Unicast이기 때문에 오직 하나의 Subscriber만 가능하다.
            log.error(e.getMessage());
        }
    }
    private static String doTask(int taskNumber) {
        // now tasking.
        // complete to task
        log.info("# doTask(): {}", taskNumber);
        return "task " + taskNumber + " result";
    }
}

/*
// Instream에 의해서 위 Thread-0~4가 실행된다.
// 그런데 이 쓰레드는 메인 쓰레드이기 때문에 Blocking이 완료될 때까지 기다리게 된다.
23:07:50.719 [Thread-0] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # doTask(): 1
23:07:50.820 [Thread-1] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # doTask(): 2
23:07:50.924 [Thread-2] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # doTask(): 3
23:07:51.029 [Thread-3] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # doTask(): 4
23:07:51.133 [Thread-4] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # doTask(): 5

// 쓰레드 스케쥴링에 따라 각 스트림이 다르게 동작한다.
23:07:51.268 [parallel-2] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # map(): task 1 result success!
23:07:51.268 [parallel-2] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # map(): task 2 result success!
23:07:51.269 [parallel-2] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # map(): task 3 result success!
23:07:51.269 [parallel-2] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # map(): task 4 result success!
23:07:51.269 [parallel-1] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # onNext: task 1 result success!
23:07:51.269 [parallel-2] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # map(): task 5 result success!
23:07:51.269 [parallel-1] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # onNext: task 2 result success!
23:07:51.269 [parallel-1] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # onNext: task 3 result success!
23:07:51.269 [parallel-1] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # onNext: task 4 result success!
23:07:51.269 [parallel-1] INFO me.example.reactorstudy.chapter9.Chapter9Example2 -- # onNext: task 5 result success!
*/
