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
import java.util.function.IntConsumer;
import java.util.function.Predicate;

import static java.util.Spliterator.SIZED;
import static java.util.Spliterator.SUBSIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(TestContextExtention.class)
public class FilterTest {

    @TestTemplate
    public void filterAndCollect(StreamFactory factory) {
        Predicate<Integer> even = (n) -> n % 2 == 0;
        assertEquals(
                List.of(2),
                factory.stream(List.of(1, 2, 3)).filter(even).toList());
        assertEquals(
                List.of(2, 4),
                factory.stream(List.of(1, 2, 3, 4)).filter(even).toList());
    }

    @TestTemplate
    public void filterAndForEach(StreamFactory factory) {
        Predicate<Integer> even = (n) -> n % 2 == 0;
        IntConsumer intConsumer = mock(IntConsumer.class);
        factory.stream(List.of(1, 2, 3, 4)).filter(even).forEach(intConsumer::accept);
        verify(intConsumer).accept(2);
        verify(intConsumer).accept(4);
    }

    @TestTemplate
    public void filterAndTryAdvance(StreamFactory factory) {
        Predicate<Integer> even = (n) -> n % 2 == 0;
        IntConsumer intConsumer = mock(IntConsumer.class);
        var spliterator = factory.stream(List.of(1, 2, 3, 4)).filter(even).spliterator();
        assertTrue(spliterator.tryAdvance(intConsumer::accept));
        assertTrue(spliterator.tryAdvance(intConsumer::accept));
        assertFalse(spliterator.tryAdvance(intConsumer::accept));
        verify(intConsumer).accept(2);
        verify(intConsumer).accept(4);
    }

    @TestTemplate
    public void filterCharacteristics1(StreamFactory factory) {
        filterCharacteristics(factory, List.of(1, 2, 3, 4));
    }

    @TestTemplate
    public void filterCharacteristics2(StreamFactory factory) {
        filterCharacteristics(factory, new ArrayList<>(List.of(1, 2, 3, 4)));
    }

    @TestTemplate
    public void filterCharacteristics3(StreamFactory factory) {
        filterCharacteristics(factory, Arrays.asList(1, 2, 3, 4));
    }

    @TestTemplate
    public void filterCharacteristics4(StreamFactory factory) {
        filterCharacteristics(factory, Set.of(1, 2, 3, 4));
    }

    @TestTemplate
    public void filterCharacteristics5(StreamFactory factory) {
        filterCharacteristics(factory, new HashSet<>(Set.of(1, 2, 3, 4)));
    }

    private void filterCharacteristics(StreamFactory factory, Collection<Integer> source) {
        Predicate<Integer> even = (n) -> n % 2 == 0;
        SpliteratorComparison.ofSource(factory, source)
                .operation((s) -> s.filter(even))
                .removedCharacteristics(SIZED | SUBSIZED)
                .assertCharacteristics();
    }

}
