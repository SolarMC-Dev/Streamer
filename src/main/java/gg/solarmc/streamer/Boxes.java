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

/*
 * Beware the ugly code
 */
final class Boxes {

    private Boxes() { }

    static final class Bool {
        boolean value;

        private Bool() {}
    }

    private static final ThreadLocal<Bool> boolRef = ThreadLocal.withInitial(Bool::new);

    static Bool obtainBool() {
        return boolRef.get();
    }

    static final class Ref<T> {
        T value;

        private Ref() {}
    }

    private static final ThreadLocal<Ref<?>> boxRef = ThreadLocal.withInitial(Ref::new);

    static <T> Boxes.Ref<T> obtainRef() {
        @SuppressWarnings("unchecked")
        Ref<T> casted = (Ref<T>) boxRef.get();
        return casted;
    }

}
