package me.example.reactorstudy.chapter7;

import java.net.URI;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class HotSeqExample7_1 {

    public static void main(String[] args) throws InterruptedException {
        URI worldTimeUri = UriComponentsBuilder.newInstance()
                                       .scheme("http")
                                       .host("worldtimeapi.org")
                                       .port(80)
                                       .path("/api/timezone/Asia/Seoul")
                                       .build()
                                       .encode()
                                       .toUri();

        Mono<String> mono = getWorldTime(worldTimeUri).cache();
        mono.subscribe(dateTime -> log.info("# dateTime 1 : {}", dateTime));

        Thread.sleep(2000);
        mono.subscribe(dateTime -> log.info("# dateTime 2 : {}", dateTime));

        // 이걸 추가하지 않으면, 마지막 mono.subscribe()의 값을 볼 수 없음.
        // Reactor는 비동기적으로 동작하는데, mono의 비동기적 작업이 완료될 때까지 시간이 필요함.
        // 아래 코드가 없으면 쓰레드가 바로 종료해버리므로 결과를 볼 수 없어짐.
        Thread.sleep(2000);

        // cache 된 것은 main 쓰레드가 가져감.
        // 09:38:29.553 [reactor-http-nio-2] INFO me.example.reactorstudy.chapter7.ColdSeqExample7_2 -- # dateTime 1 : 2024-03-31T09:38:29.348083+09:00
        // 09:38:31.139 [main] INFO me.example.reactorstudy.chapter7.ColdSeqExample7_2 -- # dateTime 2 : 2024-03-31T09:38:29.348083+09:00
    }

    private static Mono<String> getWorldTime(URI worldTimeUri) {
        return WebClient.create()
                        .get()
                        .uri(worldTimeUri)
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(response -> {
                            DocumentContext jsonContext = JsonPath.parse(response);
                            String dateTime = jsonContext.read("$.datetime");
                            return dateTime;
                        });

    }


}
