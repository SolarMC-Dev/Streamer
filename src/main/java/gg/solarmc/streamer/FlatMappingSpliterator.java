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

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

final class FlatMappingSpliterator<T, R> implements Spliterator<R> {

    private final Spliterator<T> sourceDelegate;
    private final Function<? super T, ? extends Stream<? extends R>> mapper;

    private Spliterator<? extends R> currentDelegate;

    FlatMappingSpliterator(Spliterator<T> sourceDelegate, Function<? super T, ? extends Stream<? extends R>> mapper) {
        this.sourceDelegate = sourceDelegate;
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    private boolean obtainNextDelegate() {
        Boxes.Ref<T> refBox = Boxes.obtainRef();
        boolean found = sourceDelegate.tryAdvance((sourceElement) -> {
            refBox.value = sourceElement;
        });
        if (!found) {
            return false;
        }
        Stream<? extends R> nextStream = mapper.apply(refBox.value);
        currentDelegate = nextStream.spliterator();
        return true;
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        while (true) {
            if (currentDelegate == null && !obtainNextDelegate()) {
                return false;
            }
            if (currentDelegate.tryAdvance(action)) {
                return true;
            }
            currentDelegate = null;
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super R> action) {
        if (currentDelegate != null) {
            currentDelegate.forEachRemaining(action);
        }
        sourceDelegate.forEachRemaining((element) -> {
            mapper.apply(element).forEach(action);
        });
    }

    @Override
    public Spliterator<R> trySplit() {
        // It is OK to ignore the currentDelegate here
        Spliterator<T> splitDelegate = sourceDelegate.trySplit();
        if (splitDelegate == null) {
            return null;
        }
        return new FlatMappingSpliterator<>(splitDelegate, mapper);
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        int characteristicsNot = DISTINCT | SORTED | NONNULL | SIZED | SUBSIZED;
        return sourceDelegate.characteristics() & (~characteristicsNot);
    }

    // Not sorted; no comparator

}
