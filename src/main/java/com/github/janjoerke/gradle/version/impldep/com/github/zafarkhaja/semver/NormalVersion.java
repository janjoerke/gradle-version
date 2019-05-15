/*******************************************************************************
 * The MIT License
 *
 * Copyright 2019 Jan Jörke <janjoerke@gmail.com>.
 * Copyright 2012-2015 Zafar Khaja <zafarkhaja@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package com.github.janjoerke.gradle.version.impldep.com.github.zafarkhaja.semver;

/**
 * The {@code NormalVersion} class represents the version core.
 *
 * This class is immutable and hence thread-safe.
 *
 * @author Zafar Khaja <zafarkhaja@gmail.com>
 * @since 0.2.0
 */
class NormalVersion implements Comparable<NormalVersion> {

    /**
     * The major version number.
     */
    private final int major;

    /**
     * The minor version number.
     */
    private final int minor;

    /**
     * The build version number.
     */
    private final int build;

    /**
     * The subBuild version number.
     */
    private final int subBuild;

    /**
     * Constructs a {@code NormalVersion} with the major, minor and patch version
     * numbers.
     *
     * @param major    the major version number
     * @param minor    the minor version number
     * @param build    the build version number
     * @param subBuild the subBuild version number
     * @throws IllegalArgumentException if one of the version numbers is a negative
     *                                  integer
     */
    NormalVersion(int major, int minor, int build, int subBuild) {
        if (major < 0 || minor < 0 || build < 0 || subBuild < 0) {
            throw new IllegalArgumentException(
                    "Major, minor, build and subBuild versions MUST be non-negative integers.");
        }
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.subBuild = subBuild;
    }

    /**
     * Returns the major version number.
     *
     * @return the major version number
     */
    int getMajor() {
        return major;
    }

    /**
     * Returns the minor version number.
     *
     * @return the minor version number
     */
    int getMinor() {
        return minor;
    }

    /**
     * Returns the build version number.
     *
     * @return the build version number
     */
    int getBuild() {
        return build;
    }

    /**
     * Returns the subBuild version number.
     *
     * @return the subBuild version number
     */
    int getSubBuild() {
        return subBuild;
    }

    /**
     * Increments the major version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    NormalVersion incrementMajor() {
        return new NormalVersion(major + 1, 0, 0, 0);
    }

    /**
     * Increments the minor version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    NormalVersion incrementMinor() {
        return new NormalVersion(major, minor + 1, 0, 0);
    }

    /**
     * Increments the build version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    NormalVersion incrementBuild() {
        return new NormalVersion(major, minor, build + 1, 0);
    }

    /**
     * Increments the subBuild version number.
     *
     * @return a new instance of the {@code NormalVersion} class
     */
    NormalVersion incrementSubBuild() {
        return new NormalVersion(major, minor, build, subBuild + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(NormalVersion other) {
        int result = major - other.major;
        if (result == 0) {
            result = minor - other.minor;
            if (result == 0) {
                result = build - other.build;
                if (result == 0) {
                    result = subBuild - other.subBuild;
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NormalVersion)) {
            return false;
        }
        return compareTo((NormalVersion) other) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + major;
        hash = 31 * hash + minor;
        hash = 31 * hash + build;
        hash = 31 * hash + subBuild;
        return hash;
    }

    /**
     * Returns the string representation of this normal version.
     *
     * A normal version number MUST take the form W.X.Y.Z where W, X, Y, and Z are
     * non-negative integers. W is the major version, X is the minor version, Y is
     * the build version, and Z is the subBuid version.
     *
     * @return the string representation of this normal version
     */
    @Override
    public String toString() {
        return String.format("%d.%d.%d.%d", major, minor, build, subBuild);
    }
}
