package com.parkingwang.lang.data;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0.7
 */
final public class LatchedValue<T> {

    private final CountDownLatch mLatch = new CountDownLatch(1);
    private T mValue;

    public LatchedValue() {
    }

    public LatchedValue(T value) {
        mValue = value;
    }

    public boolean isSet() {
        return mLatch.getCount() == 0;
    }

    public boolean isNotSet(){
        return !isSet();
    }

    public synchronized void setValue(@NotNull T ref) {
        if (!isSet()){
            mValue = ref;
            mLatch.countDown();
        }
    }

    @NotNull
    public T get() throws InterruptedException {
        mLatch.await();
        synchronized (this){
            return mValue;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LatchedValue<?> ref = (LatchedValue<?>) o;
        return mValue != null ? mValue.equals(ref.mValue) : ref.mValue == null;
    }

    @Override
    public int hashCode() {
        return mValue != null ? mValue.hashCode() : 0;
    }
}