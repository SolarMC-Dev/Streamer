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

import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

final class FilteringSpliterator<T> implements Spliterator<T> {

    private final Spliterator<T> delegate;
    private final Predicate<? super T> filter;

    FilteringSpliterator(Spliterator<T> delegate, Predicate<? super T> filter) {
        this.delegate = delegate;
        this.filter = Objects.requireNonNull(filter, "filter");
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        Boxes.Bool boolBox = Boxes.obtainBool();
        delegate.tryAdvance((element) -> {
            if (filter.test(element)) {
                action.accept(element);
                boolBox.value = true;
            }
        });
        return boolBox.value;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        delegate.forEachRemaining((element) -> {
            if (filter.test(element)) {
                action.accept(element);
            }
        });
    }

    @Override
    public Spliterator<T> trySplit() {
        Spliterator<T> splitDelegate = delegate.trySplit();
        if (splitDelegate == null) {
            return null;
        }
        return new FilteringSpliterator<>(splitDelegate, filter);
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        int characteristicsNot = SIZED | SUBSIZED;
        return delegate.characteristics() & (~characteristicsNot);
    }

    @Override
    public Comparator<? super T> getComparator() {
        return delegate.getComparator();
    }
}
