/*******************************************************************************
 * The MIT License
 *
 * Copyright 2019 Jan Jörke <janjoerke@gmail.com>.
 * Copyright 2012-2015 Zafar Khaja <zafarkhaja@gmail.com>
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
package com.github.janjoerke.gradle.version.impldep.com.github.zafarkhaja.semver.expr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.janjoerke.gradle.version.impldep.com.github.zafarkhaja.semver.Version;
import com.github.janjoerke.gradle.version.impldep.com.github.zafarkhaja.semver.expr.LessOrEqual;

/**
 *
 * @author Zafar Khaja <zafarkhaja@gmail.com>
 */
public class LessOrEqualTest {

    @Test
    public void shouldCheckIfVersionIsLessThanOrEqualToParsedVersion() {
        Version parsed = Version.valueOf("2.0.0.0");
        LessOrEqual le = new LessOrEqual(parsed);
        assertTrue(le.interpret(Version.valueOf("1.2.3.0")));
        assertTrue(le.interpret(Version.valueOf("2.0.0.0")));
        assertFalse(le.interpret(Version.valueOf("3.2.1.0")));
    }
}
