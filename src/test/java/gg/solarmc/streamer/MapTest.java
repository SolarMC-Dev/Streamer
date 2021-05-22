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

import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.SORTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(TestContextExtention.class)
public class MapTest {

    @TestTemplate
    public void mapAndCollect(StreamFactory factory) {
        assertEquals(
                List.of("1", "2"),
                factory.stream(List.of(1, 2)).map(Object::toString).toList());
    }

    @TestTemplate
    public void mapAndForEach(StreamFactory factory) {
        IntConsumer intConsumer = mock(IntConsumer.class);
        factory.stream(List.of("1", "2")).map(Integer::parseInt).forEach(intConsumer::accept);
        verify(intConsumer).accept(1);
        verify(intConsumer).accept(2);
    }

    @TestTemplate
    public void mapCharacteristics1(StreamFactory factory) {
        mapCharacteristics(factory, List.of(1, 2, 3, 4));
    }

    @TestTemplate
    public void mapCharacteristics2(StreamFactory factory) {
        mapCharacteristics(factory, new ArrayList<>(List.of(1, 2, 3, 4)));
    }

    @TestTemplate
    public void mapCharacteristics3(StreamFactory factory) {
        mapCharacteristics(factory, Arrays.asList(1, 2, 3, 4));
    }

    @TestTemplate
    public void mapCharacteristics4(StreamFactory factory) {
        mapCharacteristics(factory, Set.of(1, 2, 3, 4));
    }

    @TestTemplate
    public void mapCharacteristics5(StreamFactory factory) {
        mapCharacteristics(factory, new HashSet<>(Set.of(1, 2, 3, 4)));
    }

    private void mapCharacteristics(StreamFactory factory, Collection<Integer> source) {
        SpliteratorComparison.ofSource(factory, source)
                .operation((s) -> s.map(Object::toString))
                .removedCharacteristics(DISTINCT | SORTED | NONNULL)
                .assertCharacteristics();
    }

}
