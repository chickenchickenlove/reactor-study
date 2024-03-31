package me.example.reactorstudy.chapter9;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.Many;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class Chapter9Example5 {

    public static void main(String[] args) throws InterruptedException {
        // Cold Sequence를 원한다면, multicast() -> replay().all()
        // replay().limit(2)를 하면 SinksMany에 데이터가 공급되자마자 소비됨.
        final Many<String> many = Sinks.many()
                                       .replay().limit(2);


        final Flux<String> multicastFlux = many.asFlux();

        many.emitNext("1", EmitFailureHandler.FAIL_FAST);
        many.emitNext("2", EmitFailureHandler.FAIL_FAST);
        many.emitNext("3", EmitFailureHandler.FAIL_FAST);
        // 얘는 2,3,4만 받을 수 있음. 즉, 1이 버려짐
        multicastFlux.subscribe(data -> log.info("# Subscribe1 : {}", data));

        many.emitNext("4", EmitFailureHandler.FAIL_FAST);
        // 얘는 3,4만 받을 수 있음. 앞의 1,2는 버렴.
        multicastFlux.subscribe(data -> log.info("# Subscribe2 : {}", data));

        Thread.sleep(2000);
    }
}

// 구독 시점을 기준으로 가장 최신 2개까지만 리플레이해서 받음.
/*
23:36:15.707 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example5 -- # Subscribe1 : 2
23:36:15.709 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example5 -- # Subscribe1 : 3
23:36:15.709 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example5 -- # Subscribe1 : 4
23:36:15.709 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example5 -- # Subscribe2 : 3
23:36:15.709 [main] INFO me.example.reactorstudy.chapter9.Chapter9Example5 -- # Subscribe2 : 4
 */
