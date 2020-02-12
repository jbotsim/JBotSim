/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.jbotsim.io.format.graph6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

class BitVectorTest {
    private static final int MAX_VEC_SIZE = 999;
    private static final int SEED = 31415;

    /*
     * Returns a stream of couple (size,seed).
     */
    private static Stream<Arguments> SIZE_AND_SEED_PARAMETERS() {
        Random rnd = new Random(SEED);
        return Stream.iterate(0, i->i+1)
                .limit(MAX_VEC_SIZE+1)
                .flatMap(i -> Stream.of(Arguments.of(i, rnd.nextInt())));
    }

    @ParameterizedTest(name = "bv size = {0}, seed = {1}")
    @MethodSource("SIZE_AND_SEED_PARAMETERS")
    void randomGetSet(int bvSize, int seed) {
        Random rnd = new Random(seed);
        BitVector bv = new BitVector();
        boolean[] vec = new boolean[bvSize];

        for (int i = 0; i < bvSize; i++) {
            int index = rnd.nextInt(bvSize);
            boolean b = rnd.nextBoolean();
            vec[index] = b;
            bv.set(index, b);
            Assertions.assertEquals(b, bv.get(index));
        }
        assertEquals(bv, vec);
    }

    private void assertEquals(BitVector bv, boolean[] vec) {
        for (int i = 0; i < vec.length; i++) {
            Assertions.assertEquals(vec[i], bv.get(i));
        }
    }
}