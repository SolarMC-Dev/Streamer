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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.IntConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(TestContextExtention.class)
public class IteratorTest {

    @TestTemplate
    public void iterator1(StreamFactory factory) {
        Iterator<Integer> iter = factory.stream(List.of(1, 2, 3)).iterator();
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertEquals(1, iter.next());
        assertEquals(2, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(3, iter.next());
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @TestTemplate
    public void iterator2(StreamFactory factory) {
        Iterator<Integer> iter = factory.stream(List.of(1, 2, 3)).iterator();
        assertEquals(1, iter.next());
        assertEquals(2, iter.next());
        assertEquals(3, iter.next());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @TestTemplate
    public void iterator3(StreamFactory factory) {
        Iterator<Object> iter = factory.stream(List.of()).iterator();
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @TestTemplate
    public void iterator4(StreamFactory factory) {
        Iterator<Object> iter = factory.stream(List.of()).iterator();
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @TestTemplate
    public void iterator5(StreamFactory factory) {
        Iterator<Integer> iter = factory.stream(List.of(1, 3, 5)).iterator();
        assertTrue(iter.hasNext());
        IntConsumer intConsumer = mock(IntConsumer.class);
        iter.forEachRemaining(intConsumer::accept);
        verify(intConsumer).accept(1);
        verify(intConsumer).accept(3);
        verify(intConsumer).accept(5);
    }

}
