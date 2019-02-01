package com.salmon;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Callable;

/**
 * zip 中apply 方法的工作线程与 zip 参数中最后发送消息的线程保持一致，同时默认情况下，subscribe 的方法也与apply 的线程一致
 */
public class ZipTest {

    public static void main(String[] args) {
        Observable o1 = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                System.out.println("o1 " + Thread.currentThread());
                return "1";

            }
        }).subscribeOn(Schedulers.io());

        Observable o2 = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("o2 " + Thread.currentThread());
                return "2";

            }
        }).subscribeOn(Schedulers.io());

        Observable o3 = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("o3 " + Thread.currentThread());
                return "3";

            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(o1, o2, o3, new Function3<String, String, String, String>() {
            @Override
            public String apply(String s, String s2, String s3) throws Exception {
                System.out.println("zip apply: " + Thread.currentThread());
                return "1234";
            }
        })
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("zip accept: " + Thread.currentThread());
                    }
                });

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
