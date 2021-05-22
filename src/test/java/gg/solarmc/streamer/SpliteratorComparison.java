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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Spliterator.CONCURRENT;
import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterator.SIZED;
import static java.util.Spliterator.SORTED;
import static java.util.Spliterator.SUBSIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

final class SpliteratorComparison {

    private final Spliterator<?> spliteratorOne;
    private final Spliterator<?> spliteratorTwo;

    SpliteratorComparison(Spliterator<?> spliteratorOne, Spliterator<?> spliteratorTwo) {
        this.spliteratorOne = spliteratorOne;
        this.spliteratorTwo = spliteratorTwo;
    }

    void assertEqualCharacteristicsFor(int...characteristics) {
        for (int characteristic : characteristics) {
            assertEquals(
                    spliteratorOne.hasCharacteristics(characteristic),
                    spliteratorTwo.hasCharacteristics(characteristic),
                    "Spliterator " + spliteratorOne + " and " + spliteratorTwo
                            + " should have " + characteristic + " in common");
        }
    }

    public static <T> Builder<T, ?> ofSource(StreamFactory streamFactory, Collection<T> source) {
        return new Builder<>(streamFactory, source);
    }

    public static final class Builder<T, R> {

        private final StreamFactory streamFactory;
        private final Collection<T> source;
        private Function<Stream<T>, Stream<R>> operation;
        private int removedCharacteristics;

        private Builder(StreamFactory streamFactory, Collection<T> source) {
            this.streamFactory = Objects.requireNonNull(streamFactory ,"factory");
            this.source = Objects.requireNonNull(source, "soruce");
        }

        public <R2> Builder<T, R2> operation(Function<Stream<T>, Stream<R2>> operation) {
            Function<?, ?> strippedOperation = operation;
            @SuppressWarnings("unchecked")
            Function<Stream<T>, Stream<R>> castedOperation = (Function<Stream<T>, Stream<R>>) strippedOperation;
            this.operation = castedOperation;
            @SuppressWarnings("unchecked")
            Builder<T, R2> castedThis = (Builder<T, R2>) this;
            return castedThis;
        }

        /**
         * Sets the characteristics which will be removed by the operation
         *
         * @param removedCharacteristics the removed characteristics
         * @return this builder
         */
        public Builder<T, R> removedCharacteristics(int removedCharacteristics) {
            this.removedCharacteristics = removedCharacteristics;
            return this;
        }

        private static final List<Integer> allCharacteristics = List.of(
                DISTINCT, SORTED, ORDERED, SIZED, NONNULL, IMMUTABLE, CONCURRENT, SUBSIZED);

        public void assertCharacteristics() {
            Stream<T> sourceStream = streamFactory.stream(source);
            Stream<R> resultStream = operation.apply(sourceStream);
            Spliterator<T> sourceSpliterator = source.spliterator();
            Spliterator<R> resultSpliterator = resultStream.spliterator();
            if (removedCharacteristics != 0) {
                assertFalse(resultSpliterator.hasCharacteristics(removedCharacteristics));
            }
            SpliteratorComparison comparison = new SpliteratorComparison(sourceSpliterator, resultSpliterator);
            for (int characteristic : allCharacteristics) {
                if ((characteristic & removedCharacteristics) == 0) {
                    comparison.assertEqualCharacteristicsFor(characteristic);
                }
            }
        }
    }

}
