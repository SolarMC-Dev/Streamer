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
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestContextExtention.class)
public class MatchTest {

    @TestTemplate
    public void anyMatch(StreamFactory factory) {
        Predicate<Integer> even = (i) -> i % 2 == 0;
        assertTrue(factory.stream(List.of(1, 2, 3)).anyMatch(even));
        assertTrue(factory.stream(List.of(2, 4)).anyMatch(even));
        assertFalse(factory.stream(List.of(1, 3, 5)).anyMatch(even));
    }

    @TestTemplate
    public void allMatch(StreamFactory factory) {
        Predicate<Integer> even = (i) -> i % 2 == 0;
        assertFalse(factory.stream(List.of(1, 2, 3)).allMatch(even));
        assertTrue(factory.stream(List.of(2, 4)).allMatch(even));
        assertFalse(factory.stream(List.of(1, 3, 5)).allMatch(even));
    }

    @TestTemplate
    public void noneMatch(StreamFactory factory) {
        Predicate<Integer> even = (i) -> i % 2 == 0;
        assertFalse(factory.stream(List.of(1, 2, 3)).noneMatch(even));
        assertFalse(factory.stream(List.of(2, 4)).noneMatch(even));
        assertTrue(factory.stream(List.of(1, 3, 5)).noneMatch(even));
    }
}
