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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(TestContextExtention.class)
public class LimitTest {

    @TestTemplate
    public void limitAndCollect(StreamFactory factory) {
        assertEquals(
                List.of(1, 2, 3),
                factory.stream(List.of(1, 2, 3, 4, 5)).limit(3).toList());
    }

    @TestTemplate
    public void limitAndForEach(StreamFactory factory) {
        IntConsumer intConsumer = mock(IntConsumer.class);
        factory.stream(List.of(1, 2, 3, 4, 5)).limit(3).forEach(intConsumer::accept);
        verify(intConsumer).accept(1);
        verify(intConsumer).accept(2);
        verify(intConsumer).accept(3);
    }

    // The JDK's limit(int) removes the SIZED characteristic

    @Test
    public void limitCharacteristics1() {
        limitCharacteristics(List.of(1, 2, 3));
    }

    @Test
    public void limitCharacteristics2() {
        limitCharacteristics(new ArrayList<>(List.of(1, 2, 3)));
    }

    @Test
    public void limitCharacteristics3() {
        limitCharacteristics(Arrays.asList(1, 2, 3));
    }

    @Test
    public void limitCharacteristics4() {
        limitCharacteristics(Set.of(1, 2, 3));
    }

    @Test
    public void limitCharacteristics5() {
        limitCharacteristics(new HashSet<>(Set.of(1, 2, 3)));
    }

    private void limitCharacteristics(Collection<Integer> source) {
        SpliteratorComparison.ofSource(StreamFactory.StreamerStreamFactory.INSTANCE, source)
                .operation((s) -> s.limit(5))
                .assertCharacteristics();
    }

}
