package gg.solarmc.streamer;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class SpliteratorStream<T> implements Stream<T> {
    
    private final Spliterator<T> spliterator;
    private boolean used;

    SpliteratorStream(Spliterator<T> spliterator) {
        this.spliterator = Objects.requireNonNull(spliterator, "spliterator");
    }
    
    private void markUsed() {
        if (used) {
            throw new IllegalStateException("Already used");
        }
        used = true;
    }
    
    private Stream<T> jdkDelegate() {
        markUsed();
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        markUsed();
        spliterator.forEachRemaining(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        markUsed();
        // Spliterator handles order
        spliterator.forEachRemaining(action);
    }

    /*
     * Delegated to the JDK implementation
     */

    @Override
    public Stream<T> filter(Predicate<? super T> predicate) {
        return jdkDelegate().filter(predicate);
    }

    @Override
    public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return jdkDelegate().map(mapper);
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return jdkDelegate().mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return jdkDelegate().mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return jdkDelegate().mapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return jdkDelegate().flatMap(mapper);
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return jdkDelegate().flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return jdkDelegate().flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return jdkDelegate().flatMapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> mapMulti(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return jdkDelegate().mapMulti(mapper);
    }

    @Override
    public IntStream mapMultiToInt(BiConsumer<? super T, ? super IntConsumer> mapper) {
        return jdkDelegate().mapMultiToInt(mapper);
    }

    @Override
    public LongStream mapMultiToLong(BiConsumer<? super T, ? super LongConsumer> mapper) {
        return jdkDelegate().mapMultiToLong(mapper);
    }

    @Override
    public DoubleStream mapMultiToDouble(BiConsumer<? super T, ? super DoubleConsumer> mapper) {
        return jdkDelegate().mapMultiToDouble(mapper);
    }

    @Override
    public Stream<T> distinct() {
        return jdkDelegate().distinct();
    }

    @Override
    public Stream<T> sorted() {
        return jdkDelegate().sorted();
    }

    @Override
    public Stream<T> sorted(Comparator<? super T> comparator) {
        return jdkDelegate().sorted(comparator);
    }

    @Override
    public Stream<T> peek(Consumer<? super T> action) {
        return jdkDelegate().peek(action);
    }

    @Override
    public Stream<T> limit(long maxSize) {
        return jdkDelegate().limit(maxSize);
    }

    @Override
    public Stream<T> skip(long n) {
        return jdkDelegate().skip(n);
    }

    @Override
    public Stream<T> takeWhile(Predicate<? super T> predicate) {
        return jdkDelegate().takeWhile(predicate);
    }

    @Override
    public Stream<T> dropWhile(Predicate<? super T> predicate) {
        return jdkDelegate().dropWhile(predicate);
    }

    @Override
    public Object[] toArray() {
        return jdkDelegate().toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return jdkDelegate().toArray(generator);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return jdkDelegate().reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return jdkDelegate().reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return jdkDelegate().reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return jdkDelegate().collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return jdkDelegate().collect(collector);
    }

    @Override
    public List<T> toList() {
        return jdkDelegate().toList();
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return jdkDelegate().min(comparator);
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return jdkDelegate().max(comparator);
    }

    @Override
    public long count() {
        return jdkDelegate().count();
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return jdkDelegate().anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return jdkDelegate().allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return jdkDelegate().noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return jdkDelegate().findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return jdkDelegate().findAny();
    }

    @Override
    public Iterator<T> iterator() {
        return jdkDelegate().iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return jdkDelegate().spliterator();
    }

    @Override
    public boolean isParallel() {
        return jdkDelegate().isParallel();
    }

    @Override
    public Stream<T> sequential() {
        return jdkDelegate().sequential();
    }

    @Override
    public Stream<T> parallel() {
        return jdkDelegate().parallel();
    }

    @Override
    public Stream<T> unordered() {
        return jdkDelegate().unordered();
    }

    @Override
    public Stream<T> onClose(Runnable closeHandler) {
        return jdkDelegate().onClose(closeHandler);
    }

    @Override
    public void close() {
        jdkDelegate().close();
    }

}
