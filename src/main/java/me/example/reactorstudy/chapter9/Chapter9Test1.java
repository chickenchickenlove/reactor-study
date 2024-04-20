package me.example.reactorstudy.chapter9;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public class Chapter9Test1 {

    public static void main(String[] args) throws InterruptedException {

        Mono.just("Hello")
            .map(s -> s + " really hello")
            .subscribe();

        Thread.sleep(10000);

    }

}
