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

final class MappingSpliterator<T, R> implements Spliterator<R> {

    private final Spliterator<T> delegate;
    private final Function<? super T, ? extends R> mapper;

    MappingSpliterator(Spliterator<T> delegate, Function<? super T, ? extends R> mapper) {
        this.delegate = delegate;
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        return delegate.tryAdvance((element) -> action.accept(mapper.apply(element)));
    }

    @Override
    public void forEachRemaining(Consumer<? super R> action) {
        delegate.forEachRemaining((element) -> {
            action.accept(mapper.apply(element));
        });
    }

    @Override
    public Spliterator<R> trySplit() {
        Spliterator<T> splitDelegate = delegate.trySplit();
        if (splitDelegate == null) {
            return null;
        }
        return new MappingSpliterator<>(splitDelegate, mapper);
    }

    @Override
    public long estimateSize() {
        return delegate.estimateSize();
    }

    @Override
    public int characteristics() {
        return delegate.characteristics() & (~SORTED);
    }

    // Not sorted; no comparator

}
