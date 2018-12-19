package com.salmon;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Callable;

/**
 * Concat, 当前一个消息产生，发送完毕后，后一个才会去产生，发送
 */
public class ConcatTest {

    public static void main(String[] args) {
        Observable o1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1 1");

                System.out.println("o1 start 1");
                Thread.sleep(1000);
                System.out.println("o1 end 1");

                emitter.onNext("1 2");

                System.out.println("o1 start 2");
                Thread.sleep(1000);
                System.out.println("o1 end 2");

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable o2 = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("o2 start");
                Thread.sleep(1000);
                System.out.println("o2 end");
                return "2";

            }
        }).subscribeOn(Schedulers.io());

        Observable o3 = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("o3 start");
                Thread.sleep(1000);
                System.out.println("o3 end");
                return "3";

            }
        }).subscribeOn(Schedulers.io());

        Observable.concatArray(o1, o2, o3)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("accept " + o);
                        System.out.println("");
                    }
                });

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
