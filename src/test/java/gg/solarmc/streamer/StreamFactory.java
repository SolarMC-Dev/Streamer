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
import java.util.stream.Stream;

public interface StreamFactory {

    <T> Stream<T> stream(Collection<T> source);

    class JdkStreamFactory implements StreamFactory {

        static final StreamFactory INSTANCE = new JdkStreamFactory();

        private JdkStreamFactory() {}

        @Override
        public <T> Stream<T> stream(Collection<T> source) {
            return source.stream();
        }

        @Override
        public String toString() {
            return "JdkStreamFactory{}";
        }
    }

    class StreamerStreamFactory implements StreamFactory {

        static final StreamFactory INSTANCE = new StreamerStreamFactory();

        private StreamerStreamFactory() {}

        @Override
        public <T> Stream<T> stream(Collection<T> source) {
            return Streamer.stream(source);
        }

        @Override
        public String toString() {
            return "StreamerStreamFactory";
        }
    }

}
