package com.sjk.examples.rxjava;

import static org.junit.Assert.*;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

/**
 * Created by Sebastian on 2016-06-05.
 */
public class ObservableTest {

    @Test
    public void createObservable() {

        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };

        myObservable.subscribe(mySubscriber);

    }

    @Test
    public void createObservableJust() {

        Observable<String> myObservable = Observable.just("Hello world");

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };

        myObservable.subscribe(mySubscriber);
    }

    @Test
    public void createObservableAction() {

        Observable.just("Hello world")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                        assertEquals("Hello world", s);
                    }
                });

    }

    @Test
    public void createObservableActionLambda() {

        Observable.just("Hello world")
                .subscribe(System.out::println);

    }

    @Test
    public void createObservableTestSubscriber() {
        TestSubscriber testSubscriber = new TestSubscriber();
        Observable.just("Hello world").subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        assertEquals("The list should have one element", 1, testSubscriber.getOnNextEvents().size());
        assertEquals("Hello world", testSubscriber.getOnNextEvents().get(0));
    }
}
