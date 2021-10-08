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

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Spliterator.SIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
Some tests in this class rely on JDK implementation details, and
may therefore need to be updated when upgrading the Java version
 */
@ExtendWith(TestContextExtention.class)
public class CountTest {

    @TestTemplate
    public void countFromSpliteratorSize(StreamFactory factory) {
        // Relies on that immutable List spliterators are SIZED
        List<Integer> list = List.of(1, 2, 3);
        assertTrue(list.spliterator().hasCharacteristics(SIZED), "Expectations not met");
        assertEquals(3, factory.stream(list).count());
    }

    @TestTemplate
    public void countFromManualIteration(StreamFactory factory) {
        // Relies on that ConcurrentHashMap.KeySetView spliterators NOT SIZED
        Set<Integer> set = ConcurrentHashMap.newKeySet();
        set.addAll(Set.of(1, 2, 3));
        assertFalse(set.spliterator().hasCharacteristics(SIZED), "Expectations not met");
        assertEquals(3, factory.stream(set).count());
    }
}
