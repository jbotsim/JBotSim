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
 * This class encodes a vector of bits packed by groups of 6 bits as used by
 * Graph6 file format. The size of the vector increases with respect to calls to
 * get/set methods, i.e. if the requested bit is beyond the current size of the
 * vector then this latter is adjusted.
 */
class BitVector {
    public static final int NB_BITS_PER_BYTE = 6;
    private int nbBits;
    private byte[] vec;

    /**
     * Create a vector of size 0.
     */
    public BitVector() {
        this(0);
    }

    /**
     * Create a vector with a pre-allocated capacity of {@code sizeInBits} bits.
     *
     * @param sizeInBits the number of bits to allocate
     */
    public BitVector(int sizeInBits) {
        nbBits = sizeInBits;
        int nbBytes = (nbBits + NB_BITS_PER_BYTE - 1) / NB_BITS_PER_BYTE;

        vec = new byte[nbBytes];
    }

    /**
     * Create a vector by copying {@code len} bytes from the array {@code bytes}
     * starting from cell {@code from}. Only the first NB_BITS_PER_BYTE bits of
     * cells are relevant.
     *
     * @param bytes copied bytes
     * @param from  first index from (inclusive) which bytes are copied
     * @param len   the number of bytes to copy
     */
    public BitVector(final byte[] bytes, int from, int len) {
        nbBits = bytes.length * NB_BITS_PER_BYTE;
        vec = Arrays.copyOfRange(bytes, from, from + len);
    }

    /**
     * Return the value of the {@code i}-th bit. If {@code i} is greater
     * or equal the number of bits of the vector (see
     * {@link BitVector#getNbBits()} }, then:
     * <ul>
     * <li>{@code false} is returned and</li>
     * <li>the size of the vector is adjusted to {@code i+1}.</li>
     * </ul>
     *
     * @param i the index of the bit
     * @return the value of the {@code i}-th bit
     * @throws IllegalArgumentException if i < 0
     */
    public boolean get(int i) {
        if (i < 0)
            throw new IllegalArgumentException("i must be >= 0: " + i);
        if (i >= nbBits) {
            nbBits = i;
            return false;
        }
        int index = i / NB_BITS_PER_BYTE;
        int offset = NB_BITS_PER_BYTE - (i % NB_BITS_PER_BYTE) - 1;
        return (vec[index] & (1 << offset)) != 0;
    }

    /**
     * Set the value of the {@code i}-th bit to {@code value}. If {@code i} is
     * greater or equal the number of bits of the vector (see
     * {@link BitVector#getNbBits()} }, then the size of the vector is adjusted
     * to {@code i+1}.
     *
     * @param i     the index of the bit.
     * @param value the value assigned to {@code i}-th bit.
     * @throws IllegalArgumentException if i < 0
     */
    public void set(int i, boolean value) {
        if (i < 0)
            throw new IllegalArgumentException("i must be >= 0: " + i);
        int index = i / NB_BITS_PER_BYTE;
        if (i >= nbBits) {
            ensureSize(index + 1);
            nbBits = i + 1;
        }
        int offset = NB_BITS_PER_BYTE - (i % NB_BITS_PER_BYTE) - 1;
        if (value)
            vec[index] |= ((byte) (1 << offset));
        else
            vec[index] &= ((byte) (~(1 << offset)));

    }

    /**
     * Accessor to the number of bits stored in this vector.
     *
     * @return the current size in bits of the vector
     */
    public int getNbBits() {
        return nbBits;
    }

    /**
     * Return a copy of bytes storing bits of this vector.
     *
     * @return the array of bytes containing bits.
     */
    public byte[] getBytes() {
        return Arrays.copyOf(vec, vec.length);
    }

    /**
     * Copy the content of the vector into {@code dst} starting at cell
     * {@code dst[offset]}.
     *
     * @param dst    the destination buffer
     * @param offset the index of the cell from which the vector is copied
     */
    public void copyTo(byte[] dst, int offset) {
        int minlen = dst.length - offset;
        if (minlen >= vec.length)
            minlen = vec.length;

        if (minlen >= 0)
            System.arraycopy(vec, 0, dst, offset, minlen);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : vec) {
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private void ensureSize(int size) {
        if (size <= vec.length)
            return;
        vec = Arrays.copyOf(vec, size);
    }
}
