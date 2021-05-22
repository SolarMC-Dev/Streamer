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

class PeekingSpliterator<T> implements Spliterator<T> {

    private final Spliterator<T> delegate;
    private final Consumer<? super T> peek;

    PeekingSpliterator(Spliterator<T> delegate, Consumer<? super T> peek) {
        this.delegate = delegate;
        this.peek = Objects.requireNonNull(peek, "peek");
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return delegate.tryAdvance((element) -> {
            peek.accept(element);
            action.accept(element);
        });
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        delegate.forEachRemaining((element) -> {
            peek.accept(element);
            action.accept(element);
        });
    }

    @Override
    public Spliterator<T> trySplit() {
        Spliterator<T> splitDelegate = delegate.trySplit();
        if (splitDelegate == null) {
            return null;
        }
        return new PeekingSpliterator<>(splitDelegate, peek);
    }

    @Override
    public long estimateSize() {
        return delegate.estimateSize();
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
