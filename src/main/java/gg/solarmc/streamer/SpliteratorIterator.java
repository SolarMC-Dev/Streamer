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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

final class SpliteratorIterator<T> implements Iterator<T> {

    private final Spliterator<T> delegate;

    private T cachedNext;
    private static final Object NULL = new Object();

    SpliteratorIterator(Spliterator<T> delegate) {
        this.delegate = delegate;
    }

    private static <T> T boxNull(T value) {
        @SuppressWarnings("unchecked")
        T boxed = (value == null) ? (T) NULL : value;
        return boxed;
    }

    private static <T> T unboxNull(T value) {
        return (value == NULL) ? null : value;
    }

    @Override
    public boolean hasNext() {
        if (cachedNext != null) {
            return true;
        }
        return delegate.tryAdvance((next) -> {
            cachedNext = boxNull(next);
        });
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T unboxed = unboxNull(cachedNext);
        cachedNext = null;
        return unboxed;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        delegate.forEachRemaining(action);
    }
}
