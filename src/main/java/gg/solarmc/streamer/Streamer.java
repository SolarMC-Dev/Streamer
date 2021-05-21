package gg.solarmc.streamer;

import java.util.Collection;
import java.util.List;
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
        return new SpliteratorStream<>(spliterator);
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
