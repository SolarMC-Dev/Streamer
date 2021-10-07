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

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.SIZED;
import static java.util.Spliterator.SORTED;
import static java.util.Spliterator.SUBSIZED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(TestContextExtention.class)
public class FlatMapTest {

    @TestTemplate
    public void flatMapAndForEach1(StreamFactory factory) {
        IntConsumer intConsumer = mock(IntConsumer.class);
        factory.stream(List.of(1, 2)).flatMap((n) -> (n == 1) ? Stream.of(1, 5) : Stream.of())
                .forEach(intConsumer::accept);
        verify(intConsumer).accept(1);
        verify(intConsumer).accept(5);
    }

    @TestTemplate
    public void flatMapAndForEach2(StreamFactory factory) {
        IntConsumer intConsumer = mock(IntConsumer.class);
        factory.stream(List.of(1, 3)).flatMap((n) -> Stream.of(n, n + 1, n + 2)).forEach(intConsumer::accept);
        verify(intConsumer).accept(1);
        verify(intConsumer).accept(2);
        verify(intConsumer, times(2)).accept(3);
        verify(intConsumer).accept(4);
        verify(intConsumer).accept(5);
    }

    @TestTemplate
    public void flatMapTryAdvanceAndForEach(StreamFactory factory) {
        IntConsumer intConsumer = mock(IntConsumer.class);
        var stream = factory.stream(List.of(0, 3, 4)).flatMap((n) -> Stream.of(n, n + 1));
        Spliterator<Integer> spliterator = stream.spliterator();
        assertTrue(spliterator.tryAdvance(intConsumer::accept));
        spliterator.forEachRemaining(intConsumer::accept);
        verify(intConsumer).accept(0);
        verify(intConsumer).accept(1);
        verify(intConsumer).accept(3);
        verify(intConsumer, times(2)).accept(4);
        verify(intConsumer).accept(5);
    }

    @TestTemplate
    public void flatMapTryAdvance(StreamFactory factory) {
        IntConsumer intConsumer = mock(IntConsumer.class);
        var stream = factory.stream(List.of(0, 1)).flatMap((n) -> Stream.of(n, n + 1));
        Spliterator<Integer> spliterator = stream.spliterator();
        assertTrue(spliterator.tryAdvance(intConsumer::accept));
        assertTrue(spliterator.tryAdvance(intConsumer::accept));
        assertTrue(spliterator.tryAdvance(intConsumer::accept));
        assertTrue(spliterator.tryAdvance(intConsumer::accept));
        assertFalse(spliterator.tryAdvance(intConsumer::accept));
        verify(intConsumer).accept(0);
        verify(intConsumer, times(2)).accept(1);
        verify(intConsumer).accept(2);
    }

    @TestTemplate
    public void flatMapCharacteristics1(StreamFactory factory) {
        flatMapCharacteristics(factory, List.of(1, 2, 3, 4));
    }

    @TestTemplate
    public void flatMapCharacteristics2(StreamFactory factory) {
        flatMapCharacteristics(factory, new ArrayList<>(List.of(1, 2, 3, 4)));
    }

    @TestTemplate
    public void flatMapCharacteristics3(StreamFactory factory) {
        flatMapCharacteristics(factory, Arrays.asList(1, 2, 3, 4));
    }

    @TestTemplate
    public void flatMapCharacteristics4(StreamFactory factory) {
        flatMapCharacteristics(factory, Set.of(1, 2, 3, 4));
    }

    @TestTemplate
    public void flatMapCharacteristics5(StreamFactory factory) {
        flatMapCharacteristics(factory, new HashSet<>(Set.of(1, 2, 3, 4)));
    }

    private void flatMapCharacteristics(StreamFactory factory, Collection<Integer> source) {
        SpliteratorComparison.ofSource(factory, source)
                .operation((s) -> s.flatMap((n) -> Stream.of(n.toString(), Integer.toString(n + 1))))
                .removedCharacteristics(DISTINCT | SORTED | NONNULL | SIZED | SUBSIZED)
                .assertCharacteristics();
    }

}
