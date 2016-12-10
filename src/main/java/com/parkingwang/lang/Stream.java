package com.parkingwang.lang;

import com.parkingwang.lang.data.ImmutableList;
import com.parkingwang.lang.kit.ListKit;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0
 */
final public class Stream<E> {

    private Collection<E> mElementData = Collections.emptyList();

    private Stream(){}

    private Stream(Collection<E> data){
        this.mElementData = data;
    }

    @NotNull
    public static <T> Stream<T> of(@NotNull T...items) {
        return new Stream<>(Arrays.asList(items));
    }

    @NotNull
    public static <T> Stream<T> arrayOf(@NotNull T[] items) {
        return new Stream<>(Arrays.asList(items));
    }

    @NotNull
    public static <T> Stream<T> listOf(@NotNull Collection<T> data){
        return new Stream<>(data);
    }

    @NotNull
    public Stream<E> filter(@NotNull Filter<E> filter) {
        mElementData = filterWith(mElementData, filter);
        return this;
    }

    @NotNull
    public <R> Stream<R> map(@NotNull Transformer<E, R> transformer) {
        final Stream<R> stream = new Stream<>();
        stream.mElementData = mapWith(mElementData, transformer);
        return stream;
    }

    @NotNull
    public E reduce(@NotNull E defaultValue, @NotNull Accumulator<E> action){
        return reduceOrDefault(defaultValue, action);
    }

    public E reduceOrDefault(@NotNull Accumulator<E> action){
        return reduceOrDefault(null, action);
    }

    @NotNull
    public Stream<E> sort(@NotNull Comparator<E> comparator){
        final List<E> sorted = ListKit.toArrayList(mElementData);
        Collections.sort(sorted, comparator);
        mElementData = sorted;
        return this;
    }

    public E firstOrNull(){
        if (isEmpty()) {
            return null;
        }else{
            return toList().get(0);
        }
    }

    public E lastOrNull() {
        if (isEmpty()) {
            return null;
        }else{
            return toList().get(mElementData.size() - 1);
        }
    }

    public boolean isEmpty(){
        invokeLazy();
        return mElementData.isEmpty();
    }

    public boolean isNotEmpty(){
        invokeLazy();
        return !isEmpty();
    }

    public int size(){
        invokeLazy();
        return mElementData.size();
    }

    @NotNull
    public List<E> toList(){
        invokeLazy();
        return ListKit.toArrayList(mElementData);
    }

    @NotNull
    public E[] toArray(){
        invokeLazy();
        return (E[]) mElementData.toArray();
    }

    @NotNull
    public ImmutableList<E> toImmutableList(){
        invokeLazy();
        return ImmutableList.listOf(mElementData);
    }

    @NotNull
    public Set<E> toHashSet(){
        invokeLazy();
        return new LinkedHashSet<>(mElementData);
    }

    private E reduceOrDefault(E defaultValue, @NotNull Accumulator<E> action){
        E output = defaultValue;
        if (isNotEmpty()){
            final List<E> list = toList();
            output = list.get(0);
            final int size = list.size();
            for (int i = 1; i < size; i++) {
                output = action.invoke(output, list.get(i));
            }
        }
        return output;
    }

    private void invokeLazy(){
        // TODO
    }

    @NotNull
    public static <T> Collection<T> filterWith(Collection<T> items, Filter<T> action) {
        final List<T> output = new ArrayList<>();
        for (T item : items) {
            if (action.filter(item)) {
                output.add(item);
            }
        }
        return output;
    }

    @NotNull
    public static <E_IN, E_OUT> Collection<E_OUT> mapWith(Collection<E_IN> items, Transformer<E_IN, E_OUT> action) {
        final List<E_OUT> output = new ArrayList<>();
        for (E_IN in : items) {
            output.add(action.transform(in));
        }
        return output;
    }

}
