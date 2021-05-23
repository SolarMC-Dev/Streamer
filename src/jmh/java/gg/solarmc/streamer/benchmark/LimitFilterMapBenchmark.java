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

package gg.solarmc.streamer.benchmark;

import gg.solarmc.streamer.Streamer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@BenchmarkMode({Mode.Throughput, Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 8, time = 4)
@Measurement(iterations = 10, time = 4)
public class LimitFilterMapBenchmark {

    private static final Set<Integer> source = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    private static final int LIMIT = 8;

    @Benchmark
    public void jdkStream(Blackhole bh) {
        bh.consume(source.stream()
                .limit(LIMIT)
                .filter((n) -> n % 2 == 0)
                .map(Object::toString)
                .collect(Collectors.toUnmodifiableSet()));
    }

    @Benchmark
    public void streamerStream(Blackhole bh) {
        bh.consume(Streamer.stream(source)
                .limit(LIMIT)
                .filter((n) -> n % 2 == 0)
                .map(Object::toString)
                .collect(Collectors.toUnmodifiableSet()));
    }

    @Benchmark
    public void forLoop(Blackhole bh) {
        Set<String> result = new HashSet<>();

        int count = 0;
        for (Integer n : source) {
            if (count++ == LIMIT) {
                break;
            }
            if (n % 2 == 0) {
                result.add(n.toString());
            }
        }
        bh.consume(Set.copyOf(result));
    }
}
