package com.sjk.examples.rxjava;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.observables.GroupedObservable;
import rx.observers.TestSubscriber;

import java.util.function.Consumer;

import static com.sjk.help.DataFactory.getNumbers;
import static com.sjk.help.DataFactory.getStringNumbers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sebastian on 2016-05-30.
 */

public class OperatorsTests {

    TestSubscriber<String> subscriber = new TestSubscriber<>();

    @Test
    public void filterTest() {

        getStringNumbers()
                .filter(number -> number.contains("f"))
                .subscribe(subscriber);

        subscriber.getOnNextEvents().forEach(numberWithFLetter -> {
            System.out.println(numberWithFLetter);
            assertTrue("Should contain the letter f", numberWithFLetter.contains("f"));
        });
    }

    @Test
    public void takeLastTest() {
        getStringNumbers()
                .takeLast(2)
                .subscribe(subscriber);

        assertEquals("The size of list should be 2",2,subscriber.getOnNextEvents().size());

        subscriber.getOnNextEvents().forEach(System.out::println);
    }

    @Test
    public void skipElementsTest() {
        getStringNumbers()
                .skip(7)
                .subscribe(subscriber);
        assertEquals("Should have 3 elements", 3, subscriber.getOnNextEvents().size());
        subscriber.getOnNextEvents().forEach(System.out::println);
    }

    @Test
    public void firstNoConditionTest() {
        getStringNumbers()
                .first()
                .subscribe(subscriber);

        assertEquals("The result should have one elements", 1, subscriber.getOnNextEvents().size());
        subscriber.getOnNextEvents().forEach(System.out::println);
    }

    @Test
    public void firstConditionTest() {
        getStringNumbers()
                .first(s -> s.contains("f"))
                .subscribe(subscriber);

        assertEquals("The result should have one elements", 1, subscriber.getOnNextEvents().size());
        subscriber.getOnNextEvents().forEach(System.out::println);
    }

    @Test
    public void ignoreElementsTest() {

        Subscriber<String> subscriber = getSimpleSubscriber();
        getStringNumbers()
                .ignoreElements()
                .subscribe(subscriber);

    }

    @Test
    public void mergeTest() {
        Observable<String> firstSource = getStringNumbers(3);
        Observable<String> secondSource = getStringNumbers().takeLast(3);

        Observable.merge(firstSource, secondSource).subscribe(subscriber);
        subscriber.assertNoErrors();
        assertEquals("Should have 6 elements", 6, subscriber.getOnNextEvents().size());
        subscriber.getOnNextEvents().forEach(System.out::println);
    }

    @Test
    public void zipTest() {
        Observable<String> firstSource = getStringNumbers(3);
        Observable<String> secondSource = getStringNumbers().takeLast(3);
        Observable.zip(firstSource, secondSource, (a, b) -> a + "_" + b).subscribe(subscriber);

        subscriber.assertNoErrors();
        assertEquals("Should have 3 elements", 3, subscriber.getOnNextEvents().size());
        subscriber.getOnNextEvents().forEach(System.out::println);
    }

    @Test
    public void onErrorReturnTest() {
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable<Integer> firstSource;
        firstSource = Observable
                .just("1", "2", "three")
                .map(s -> Integer.valueOf(s))
                .onErrorReturn(throwable -> {
                    System.out.println("OnErrorReturn");
                    return -1;
                });
        firstSource.subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
    }

    @Test
    public void onRetryTest() {
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        Observable<Integer> firstSource;
        firstSource = Observable
                .just("1", "2", "three")
                .map(Integer::valueOf)
                .retry(2);
        firstSource.subscribe(testSubscriber);
        assertEquals("Should have one error", 1, testSubscriber.getOnErrorEvents().size());
    }

    @Test
    public void mapTest() {
        TestSubscriber<Integer> subscriber = new TestSubscriber<>();
        Observable<Integer> firstSource = Observable
                .just("1", "2", "4")
                .map(Integer::valueOf);
        firstSource.subscribe(subscriber);
        subscriber.getOnNextEvents().forEach(System.out::println);
    }


    @Test
    public void groupByTestFake() {
        TestSubscriber<GroupedObservable<String, Integer>> subscriber = new TestSubscriber<>();
        TestSubscriber<Integer> subscriberNumbers = new TestSubscriber<>();
        Observable<GroupedObservable<String, Integer>> firstSource;
        firstSource = getNumbers(7)
                .groupBy(number -> {
                    if (number % 2 == 0) {
                        return "Even";
                    } else {
                        return "Odd";
                    }
                });
        firstSource.subscribe(subscriber);
        subscriber.getOnNextEvents().forEach(stringIntegerGroupedObservable -> {
            System.out.println(stringIntegerGroupedObservable.getKey());
            stringIntegerGroupedObservable.subscribe(subscriberNumbers);
            subscriberNumbers.getOnNextEvents().forEach(System.out::println);

        });
    }

    @Test
    public void groupByTest() {
        TestSubscriber<GroupedObservable<String, Integer>> subscriber = new TestSubscriber<>();
        Observable<GroupedObservable<String, Integer>> firstSource;
        firstSource = getNumbers(7)
                .groupBy(number -> {
                    if (number % 2 == 0) {
                        return "Even";
                    } else {
                        return "Odd";
                    }
                });
        firstSource.subscribe(subscriber);
        subscriber.getOnNextEvents().forEach(stringIntegerGroupedObservable -> {
            TestSubscriber<Integer> subscriberNumbers = new TestSubscriber<>();
            System.out.println(stringIntegerGroupedObservable.getKey());
            stringIntegerGroupedObservable.subscribe(subscriberNumbers);
            subscriberNumbers.getOnNextEvents().forEach(s -> System.out.println(s));
        });
    }

    private <T> Subscriber<T> getSimpleSubscriber() {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Error");
            }

            @Override
            public void onNext(T o) {
                System.out.println("Next element: " + o.toString());
            }
        };
    }
}
