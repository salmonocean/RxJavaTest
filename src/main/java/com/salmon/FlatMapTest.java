package com.salmon;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Callable;

/**
 * 1. FlatMap 新产生的消息，可能会交叉在一起，不保证与之前收到的消息顺序一致
 * 要保证顺序，使用ConcatMap
 * 2. FlatMap 时新生成消息的线程会影响接下来流程的运行线程
 */
public class FlatMapTest {

    public static void main(String[] args) {
        Observable.range(1, 10)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {

                        System.out.println("before: " + integer + ", " + Thread.currentThread());

                        return Observable.fromCallable(new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                System.out.println("after: " + integer + ", " + Thread.currentThread());
                                return String.valueOf(integer);
                            }
                        }).subscribeOn(Schedulers.io());

                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("accept " + s + ", " + Thread.currentThread());
            }
        });

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
