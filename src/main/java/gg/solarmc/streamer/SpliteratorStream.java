/*
 * Streamer
 * Copyright © 2021 SolarMC Developers
 *
 * Streamer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Streamer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Streamer. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */

package gg.solarmc.streamer;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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
        this.spliterator = spliterator;
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

    private <U> Stream<U> createStream(Spliterator<U> spliterator) {
        markUsed();
        return new SpliteratorStream<>(spliterator);
    }

    @Override
    public Stream<T> filter(Predicate<? super T> predicate) {
        return createStream(new FilteringSpliterator<>(spliterator, predicate));
    }

    @Override
    public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return createStream(new MappingSpliterator<>(spliterator, mapper));
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return createStream(new FlatMappingSpliterator<>(spliterator, mapper));
    }

    @Override
    public Iterator<T> iterator() {
        markUsed();
        return new SpliteratorIterator<>(spliterator);
    }

    @Override
    public Spliterator<T> spliterator() {
        markUsed();
        return spliterator;
    }

    @Override
    public Stream<T> peek(Consumer<? super T> action) {
        return createStream(new PeekingSpliterator<>(spliterator, action));
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        markUsed();
        R result = supplier.get();
        spliterator.forEachRemaining((element) -> accumulator.accept(result, element));
        return result;
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        markUsed();
        A container = collector.supplier().get();
        var accumulator = collector.accumulator();
        spliterator.forEachRemaining((element) -> accumulator.accept(container, element));
        return collector.finisher().apply(container);
    }

    @Override
    public Stream<T> limit(long maxSize) {
        if (maxSize < 0) {
            throw new IllegalArgumentException("maxSize must not be negative");
        }
        return createStream(new LimitedSpliterator<>(spliterator, maxSize));
    }

    /*
     * Delegated to the JDK implementation
     */

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

    /*
     * Extraneous non-delegated implementations
     */

    @Override
    public long count() {
        markUsed();
        long exactSize = spliterator.getExactSizeIfKnown();
        if (exactSize != -1L) {
            return exactSize;
        }
        long count = 0;
        while (spliterator.tryAdvance((e) -> {})) {
            count++;
        }
        return count;
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        markUsed();
        Boxes.Bool boolBox = Boxes.obtainBool();
        // Track boolBox usage to ensure it is cleaned up
        while (spliterator.tryAdvance((element) -> {
            if (predicate.test(element)) {
                boolBox.value = true;
            }
        })) {
            if (boolBox.value) {
                // Passed predicate
                boolBox.value = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return noneMatch(predicate.negate());
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return !anyMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        markUsed();
        Boxes.Ref<T> refBox = Boxes.obtainRef();
        // Track refBox usage to ensure it is cleaned up
        if (spliterator.tryAdvance((element) -> refBox.value = element)) {
            T value = refBox.value;
            refBox.value = null;
            return Optional.of(value);
        };
        return Optional.empty();
    }

    @Override
    public Optional<T> findAny() {
        return findFirst();
    }

    @Override
    public boolean isParallel() {
        return false;
    }

    @Override
    public Stream<T> sequential() {
        return this;
    }

    @Override
    public Stream<T> parallel() {
        return jdkDelegate().parallel();
    }

    // Extraneous delegated implementations

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
