package com.sjk.help;

import rx.Observable;
/**
 * Created by Sebastian on 2016-05-30.
 */
public class DataFactory {

    public static Observable<String> getStringNumbers() {
        return Observable.just("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    }

    public static Observable<String> getStringNumbers(int count) {
        return getStringNumbers().take(count);
    }

    public static Observable<Integer> getNumbers() {
        return Observable.just(0,1,2,3,4,5,6,7,8,9);
    }

    public static Observable<Integer> getNumbers(int count) {
        return getNumbers().take(count);
    }
}
