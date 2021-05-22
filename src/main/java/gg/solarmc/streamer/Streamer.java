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
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 * Entry point for obtaining streams from various sources
 *
 */
public final class Streamer {

    private Streamer() {}

    private static <T> Stream<T> streamFor(Spliterator<T> spliterator) {
        return new SpliteratorStream<>(Objects.requireNonNull(spliterator, "spliterator"));
    }

    /**
     * Creates a stream from a set of elements
     *
     * @param source the source set
     * @param <T> the element type
     * @return the stream
     */
    public static <T> Stream<T> stream(Set<T> source) {
        return streamFor(source.spliterator());
    }

    /**
     * Creates a stream from a list of elements
     *
     * @param source the source list
     * @param <T> the element type
     * @return the stream
     */
    public static <T> Stream<T> stream(List<T> source) {
        return streamFor(source.spliterator());
    }

    /**
     * Creates a stream from a collection of elements
     *
     * @param source the source collection
     * @param <T> the element type
     * @return the stream
     */
    public static <T> Stream<T> stream(Collection<T> source) {
        return streamFor(source.spliterator());
    }

}
