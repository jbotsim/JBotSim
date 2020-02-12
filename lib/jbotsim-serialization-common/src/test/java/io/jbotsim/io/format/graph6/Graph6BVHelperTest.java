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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class gathers tests related to basic encoding/decoding function of
 * Graph6 format i.e., R and N functions as described in the
 * <a href="https://users.cecs.anu.edu.au/~bdm/data/formats.txt">reference document</a>.
 *
 * Input byte vectors used for tests come from the reference document.
 */
class Graph6BVHelperTest {
    // region parameter streams
    private static final int SEED = 31415;
    private static final Random rnd = new Random(SEED);
    private static final int NB_RANDOM_TESTS = 100;

    private static Stream<Arguments> LONG_ENCODING_PARAMETERS() {
        return Stream.of(
                Arguments.of(30L,
                        new byte[]{93}),
                Arguments.of(12345L,
                        new byte[]{126, 66, 63, 120}),
                Arguments.of(460175067L,
                        new byte[]{126, 126, 63, 90, 90, 90, 90, 90}),
                Arguments.of(5L,
                        new byte[]{68})
        );
    }

    private static Stream<Arguments> BIT_SEQUENCES() {
        return Stream.of(
                Arguments.of("1000101100011100",
                        new byte[]{(byte) 0x22, (byte) 0x31, (byte) 0x30},
                        new byte[]{97, 112, 111}),
                Arguments.of("0100101001",
                        new byte[]{(byte) 0x12, (byte) 0x24},
                        new byte[]{81, 99}),
                Arguments.of("0010100000000000100100000",
                        new byte[]{(byte) 0x0A, (byte) 0x00, (byte) (0x02),
                                (byte) 0x10, (byte) 0x00},
                        new byte[]{73, 63, 65, 79, 63})
        );
    }

    private static String rndBitSequence(int len) {
        StringBuilder bv = new StringBuilder();
        for (int i = 0; i < len; i++) {
            bv.append(rnd.nextBoolean() ? '1' : '0');
        }
        return bv.toString();
    }

    private static Stream<String> RANDOM_BIT_SEQUENCES() {
        return Stream.iterate(rndBitSequence(0),
                i -> rndBitSequence(i.length() + 1)).limit(NB_RANDOM_TESTS);
    }

    private static long rndEncodable() {
        return (rnd.nextLong() & Long.MAX_VALUE) % Graph6BVHelper.G6_MAX_LONG;
    }

    private static Stream<Long> RANDOM_LONG_ENCODING_PARAMETERS() {
        return Stream.iterate(rndEncodable(), i -> rndEncodable())
                .limit(NB_RANDOM_TESTS);
    }
    // endregion

    // region test functions
    @ParameterizedTest
    @MethodSource("BIT_SEQUENCES")
    void checkBitVectorFromString(String bits, byte[] expectedBV,
                                  @SuppressWarnings("unused") byte[] expectedR) {
        byte[] bytes = bsFromString(bits).getBytes();
        assertArrayEquals(expectedBV, bytes);
    }

    @ParameterizedTest
    @MethodSource("BIT_SEQUENCES")
    void checkBitVectorFromPaddedString(String bits, byte[] expectedBV,
                                        @SuppressWarnings("unused") byte[] expectedR) {
        int j;
        String paddedString = padString(bits);
        byte[] bytes = bsFromString(paddedString).getBytes();
        for (j = 0; j < bytes.length - 1; j++) {
            assertEquals(expectedBV[j], bytes[j]);
        }
        byte lastByte = 0;
        if (j == expectedBV.length - 1)
            lastByte = expectedBV[j];
        assertEquals(lastByte, bytes[j]);
    }

    @ParameterizedTest
    @MethodSource("BIT_SEQUENCES")
    void checkREncoding(String bits,
                        @SuppressWarnings("unused") byte[] expectedBV,
                        byte[] expectedR) {
        checkR(bits, expectedR);
    }

    @ParameterizedTest
    @MethodSource("BIT_SEQUENCES")
    void checkREncodingPaddedStrings(String bits,
                                     @SuppressWarnings("unused") byte[] expectedBV,
                                     byte[] expectedR) {
        checkR(padString(bits), expectedR);
    }

    private void checkR(String bits, byte[] expected) {
        BitVector bv = bsFromString(bits);
        byte[] r = Graph6BVHelper.R(bv);

        assertArrayEquals(expected, r);
    }

    @ParameterizedTest
    @MethodSource("RANDOM_BIT_SEQUENCES")
    void checkRandomBitSequencesREncodingDecoding(String bits) {
        BitVector bv = bsFromString(bits);
        byte[] r = Graph6BVHelper.R(bv);
        BitVector bv2 = Graph6BVHelper.readR(r, 0);
        assertArrayEquals(bv.getBytes(), bv2.getBytes());
    }


    @ParameterizedTest
    @MethodSource("LONG_ENCODING_PARAMETERS")
    void checkLongEncoding(long n, byte[] expected) {
        byte[] bytes = Graph6BVHelper.N(n);
        assertArrayEquals(expected, bytes);
    }

    private void checkEncodeDecode(long n) {
        byte[] encodedN = Graph6BVHelper.N(n);
        Graph6BVHelper.ReadLongResult decodedN = Graph6BVHelper.readN(encodedN, 0);
        assertEquals(n, decodedN.value);
    }

    @ParameterizedTest
    @MethodSource("LONG_ENCODING_PARAMETERS")
    void checkLongEncodeDecode(long n,
                               @SuppressWarnings("unused") byte[] expected) {
        checkEncodeDecode(n);
    }

    @ParameterizedTest
    @MethodSource("RANDOM_LONG_ENCODING_PARAMETERS")
    void checkLongEncodeDecode(long n) {
        checkEncodeDecode(n);
    }
    // endregion

    // region helper functions
    private static String padString(String s) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() % 6 != 0) {
            sb.append('0');
        }
        return sb.toString();
    }

    private static BitVector bsFromString(String s) {
        byte[] bytes = s.getBytes();
        BitVector result = new BitVector();

        for (int i = 0; i < bytes.length; i++) {
            result.set(i, bytes[i] == '1');
        }

        return result;
    }
    // endregion
}
