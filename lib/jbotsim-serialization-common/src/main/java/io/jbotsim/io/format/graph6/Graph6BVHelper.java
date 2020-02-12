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

import java.util.Arrays;

/**
 * This class gathers methods used to encode/decode vectors of bits as specified
 * by Graph6 file format.
 *
 * @see <a href="https://users.cecs.anu.edu.au/~bdm/data/formats.txt">https://users.cecs.anu.edu.au/~bdm/data/formats.txt</a>
 */
class Graph6BVHelper {
    /**
     * The greatest integer that can be encoded using Graph6 format.
     * Not private because used by tests.
     */
    static final long G6_MAX_LONG = (2L << 35) - 1;

    private static final byte G6_SHIFT =
            (byte) ((1 << BitVector.NB_BITS_PER_BYTE) - 1);

    // region API to package
    /**
     * Helper class that store the result returned by {@link Graph6BVHelper#readN} methods.
     * It contains the long value read from a buffer and the number of bytes read.
     */
    public static final class ReadLongResult {
        ReadLongResult(long v, int nbBytes) {
            value = v;
            nbBytesRead = nbBytes;
        }

        final long value;
        final int nbBytesRead;
    }

    /**
     * Encode a non-negative integer less than  {@link Graph6BVHelper#G6_MAX_LONG}.
     *
     * @param n the integer to be encoded
     * @return the buffer containing encoding of {@code n}
     * @throws IllegalArgumentException if {@code n} is not in the range
     *                                  [0, {@link Graph6BVHelper#G6_MAX_LONG}].
     */
    public static byte[] N(long n) {
        if (!(0 <= n && n <= G6_MAX_LONG))
            throw new IllegalArgumentException("argument out of range [0," + G6_MAX_LONG + "].");

        int offset, rsize;
        if (n <= 0x3EL) {
            offset = 0;
            rsize = 1;
        } else if (n <= 0x3EFFFL) {
            offset = 1;
            rsize = 4;
        } else {
            offset = 2;
            rsize = 8;
        }
        BitVector bv = new BitVector();
        writeLong(bv, n, BitVector.NB_BITS_PER_BYTE * (rsize - offset));
        byte[] result = new byte[rsize];
        for (int i = 0; i < offset; i++)
            result[i] = 2 * G6_SHIFT;
        R(bv, result, offset);

        return result;
    }

    /**
     * Read a long value from a buffer of bytes but starting at the
     * {@code offset}-th byte. The buffer must be in Graph6 format.
     *
     * @param bytes  the Graph6 buffer
     * @param offset index of the first byte to read
     * @return the value encoded in the buffer and the number of bytes read
     * @throws ArrayIndexOutOfBoundsException if {@code offset} >= {@code bytes.length}
     * @throws Graph6Exception if the buffer is malformed.
     */
    public static ReadLongResult readN(byte[] bytes, int offset) {
        if (offset >= bytes.length)
            throw new ArrayIndexOutOfBoundsException("offset:" + offset);
        if (bytes.length - offset < 1)
            throw new Graph6Exception("malformed N in Graph6 file.");
        int nbbytes;
        int shift = 0;

        if (bytes[offset] < 2 * G6_SHIFT)
            nbbytes = 1;
        else if (bytes[offset] != 2 * G6_SHIFT || bytes.length - offset < 4)
            throw new Graph6Exception("malformed N in Graph6 file.");
        else if (bytes[offset + 1] < 2 * G6_SHIFT) {
            nbbytes = 4;
            shift = 1;
        } else if (bytes[offset + 1] != 2 * G6_SHIFT || bytes.length - offset < 8)
            throw new Graph6Exception("malformed N in Graph6 file.");
        else {
            nbbytes = 8;
            shift = 2;
        }

        BitVector bv = readRBuffer(bytes, offset + shift, nbbytes - shift);
        return new ReadLongResult(
                readLong(bv, (nbbytes - shift) * BitVector.NB_BITS_PER_BYTE),
                nbbytes);
    }

    /**
     * Encode a bitvector in a buffer of bytes using Graph6 format.
     *
     * @param bv the bitvector to encode.
     * @return the buffer of bytes in Graph6 format
     */
    public static byte[] R(BitVector bv) {
        padBitVector(bv);
        byte[] result = bv.getBytes();
        for (int i = 0; i < result.length; i++) {
            result[i] += G6_SHIFT;
        }
        return result;
    }

    /**
     * Read a bitvector from a buffer of bytes starting at the
     * {@code offset}-th byte. The buffer must be in Graph6 format.
     *
     * @param bytes  the Graph6 buffer
     * @param offset the index of the first byte to read
     * @return the vector of bits encoded in the buffer.
     * @throws ArrayIndexOutOfBoundsException if {@code offset} >= {@code bytes.length}
     * @throws Graph6Exception if the buffer is malformed.
     */
    public static BitVector readR(byte[] bytes, int offset) {
        return readRBuffer(bytes, offset, bytes.length - offset);
    }
    // endregion API to package

    // region helper functions
    private static BitVector readRBuffer(byte[] bytes, int offset, int nbbytes) {
        byte[] tmp = Arrays.copyOfRange(bytes, offset, offset + nbbytes);
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i] < G6_SHIFT)
                throw new Graph6Exception("malformed file.");
            tmp[i] -= G6_SHIFT;
        }

        return new BitVector(tmp, 0, tmp.length);
    }

    private static void R(BitVector bv, byte[] dst, int offset) {
        padBitVector(bv);
        bv.copyTo(dst, offset);

        for (int i = offset; i < dst.length; i++) {
            dst[i] += G6_SHIFT;
        }
    }

    private static void padBitVector(BitVector bv) {
        int remain = bv.getNbBits() % BitVector.NB_BITS_PER_BYTE;
        if (remain == 0)
            return;
        int padSize = BitVector.NB_BITS_PER_BYTE - remain;
        for (int i = 0; i < padSize; i++) {
            bv.set(bv.getNbBits(), false);
        }
    }

    private static void writeLong(BitVector bv, long value, int bitSize) {
        int bi = 0;
        for (int i = bitSize - 1; i >= 0; i--) {
            boolean b = ((value & (1L << i)) != 0);
            bv.set(bi++, b);
        }
    }

    private static long readLong(BitVector bv, int bitSize) {
        long result = 0;
        int bi = 0;
        for (int i = bitSize - 1; i >= 0; i--) {
            if (bv.get(bi++)) {
                result |= (1L << i);
            }
        }

        return result;
    }
    // endregion helper functions
}