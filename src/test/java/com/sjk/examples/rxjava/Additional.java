package com.sjk.examples.rxjava;

import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sebastian on 2016-06-06.
 */
public class Additional {

    @Test
    public void joinTest() {
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        Observable<String> firstSource = Observable.just("one", "two", "three").delay(10, TimeUnit.MILLISECONDS);
        Observable<String> result;
        Observable<String> secondSource = Observable.just("1", "2", "3");
        result = firstSource.join(secondSource, new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                //left Duration selector
//                System.out.println("Left slector " + s);
                return Observable.never();//just("Left Selector duration").delay(10, TimeUnit.SECONDS);

            }
        }, new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
//                System.out.println("Right slector " + s);
                return Observable.just("Left Selector duration").delay(1, TimeUnit.SECONDS);
            }
        }, new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
//                System.out.println("Result slector " + s + " " + s2);
                return s + s2;
            }
        });
        result.subscribe(subscriber);

        subscriber.getOnNextEvents().forEach(numberWithFLetter -> System.out.println(numberWithFLetter));
    }
}
