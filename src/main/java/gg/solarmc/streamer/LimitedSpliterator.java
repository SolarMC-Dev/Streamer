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
import java.util.Spliterator;
import java.util.function.Consumer;

final class LimitedSpliterator<T> implements Spliterator<T> {

    private final Spliterator<T> delegate;
    private long limit;

    LimitedSpliterator(Spliterator<T> delegate, long limit) {
        this.delegate = delegate;
        this.limit = limit;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (limit == 0) {
            return false;
        }
        limit--;
        return delegate.tryAdvance(action);
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        do {
            if (limit == 0) {
                return;
            }
            limit--;
        } while (delegate.tryAdvance(action));
    }

    @Override
    public Spliterator<T> trySplit() {
        // Not desirable to split here
        return null;
    }

    @Override
    public long estimateSize() {
        return Math.min(delegate.estimateSize(), limit);
    }

    @Override
    public int characteristics() {
        return delegate.characteristics();
    }

    @Override
    public Comparator<? super T> getComparator() {
        return delegate.getComparator();
    }
}
