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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestContextExtention.class)
public class LimitTest {

    @TestTemplate
    public void limitAndCollect(StreamFactory factory) {
        assertEquals(
                List.of(1, 2, 3),
                factory.stream(List.of(1, 2, 3, 4, 5)).limit(3).toList());
    }

    @Disabled("JDK limit(int) removes SIZED")
    @TestTemplate
    public void limitCharacteristics1(StreamFactory factory) {
        limitCharacteristics(factory, List.of(1, 2, 3));
    }

    @Disabled("JDK limit(int) removes SIZED")
    @TestTemplate
    public void limitCharacteristics2(StreamFactory factory) {
        limitCharacteristics(factory, new ArrayList<>(List.of(1, 2, 3)));
    }

    @Disabled("JDK limit(int) removes SIZED")
    @TestTemplate
    public void limitCharacteristics3(StreamFactory factory) {
        limitCharacteristics(factory, Arrays.asList(1, 2, 3));
    }

    @Disabled("JDK limit(int) removes SIZED")
    @TestTemplate
    public void limitCharacteristics4(StreamFactory factory) {
        limitCharacteristics(factory, Set.of(1, 2, 3));
    }

    @Disabled("JDK limit(int) removes SIZED")
    @TestTemplate
    public void limitCharacteristics5(StreamFactory factory) {
        limitCharacteristics(factory, new HashSet<>(Set.of(1, 2, 3)));
    }

    private void limitCharacteristics(StreamFactory factory, Collection<Integer> source) {
        SpliteratorComparison.ofSource(factory, source)
                .operation((s) -> s.limit(5))
                .assertCharacteristics();
    }

}
