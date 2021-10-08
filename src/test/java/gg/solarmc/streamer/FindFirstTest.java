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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(TestContextExtention.class)
public class FindFirstTest {

    @TestTemplate
    public void findFirstEmpty(StreamFactory factory) {
        assertEquals(Optional.empty(), factory.stream(List.of()).findFirst());
    }

    @TestTemplate
    public void findFirstPresent(StreamFactory factory) {
        assertEquals(Optional.of(1), factory.stream(List.of(1, 2, 3)).findFirst());
    }

    @TestTemplate
    public void nullElement(StreamFactory factory) {
        var stream = factory.stream(Arrays.asList(null, 2, 3));
        assertThrows(NullPointerException.class, stream::findFirst);
    }
}
