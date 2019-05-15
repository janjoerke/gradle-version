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
package com.github.janjoerke.gradle.version.impldep.com.github.zafarkhaja.semver;

import static com.github.janjoerke.gradle.version.impldep.com.github.zafarkhaja.semver.expr.CompositeExpression.Helper.gte;
import static com.github.janjoerke.gradle.version.impldep.com.github.zafarkhaja.semver.expr.CompositeExpression.Helper.lt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.github.janjoerke.gradle.version.impldep.com.github.zafarkhaja.semver.Version;

/**
 *
 * @author Zafar Khaja <zafarkhaja@gmail.com>
 */
@RunWith(Enclosed.class)
public class VersionTest {

    public static class CoreFunctionalityTest {

        @Test
        public void mayHavePreReleaseFollowingPatchAppendedWithHyphen() {
            Version v = Version.valueOf("1.2.3.0-alpha");
            assertEquals("alpha", v.getPreReleaseVersion());
        }

        @Test
        public void preReleaseShouldHaveLowerPrecedenceThanAssociatedNormal() {
            Version v1 = Version.valueOf("1.3.7.0");
            Version v2 = Version.valueOf("1.3.7.0-alpha");
            assertTrue(0 < v1.compareTo(v2));
            assertTrue(0 > v2.compareTo(v1));
        }

        @Test
        public void mayHaveBuildFollowingPatchOrPreReleaseAppendedWithPlus() {
            Version v = Version.valueOf("1.2.3.0+build");
            assertEquals("build", v.getBuildMetadata());
        }

        @Test
        public void shouldIgnoreBuildMetadataWhenDeterminingVersionPrecedence() {
            Version v1 = Version.valueOf("1.3.7.0-beta");
            Version v2 = Version.valueOf("1.3.7.0-beta+build.1");
            Version v3 = Version.valueOf("1.3.7.0-beta+build.2");
            assertTrue(0 == v1.compareTo(v2));
            assertTrue(0 == v1.compareTo(v3));
            assertTrue(0 == v2.compareTo(v3));
        }

        @Test
        public void shouldHaveGreaterThanMethodReturningBoolean() {
            Version v1 = Version.valueOf("2.3.7.0");
            Version v2 = Version.valueOf("1.3.7.0");
            assertTrue(v1.greaterThan(v2));
            assertFalse(v2.greaterThan(v1));
            assertFalse(v1.greaterThan(v1));
        }

        @Test
        public void shouldHaveGreaterThanOrEqualToMethodReturningBoolean() {
            Version v1 = Version.valueOf("2.3.7.0");
            Version v2 = Version.valueOf("1.3.7.0");
            assertTrue(v1.greaterThanOrEqualTo(v2));
            assertFalse(v2.greaterThanOrEqualTo(v1));
            assertTrue(v1.greaterThanOrEqualTo(v1));
        }

        @Test
        public void shouldHaveLessThanMethodReturningBoolean() {
            Version v1 = Version.valueOf("2.3.7.0");
            Version v2 = Version.valueOf("1.3.7.0");
            assertFalse(v1.lessThan(v2));
            assertTrue(v2.lessThan(v1));
            assertFalse(v1.lessThan(v1));
        }

        @Test
        public void shouldHaveLessThanOrEqualToMethodReturningBoolean() {
            Version v1 = Version.valueOf("2.3.7.0");
            Version v2 = Version.valueOf("1.3.7.0");
            assertFalse(v1.lessThanOrEqualTo(v2));
            assertTrue(v2.lessThanOrEqualTo(v1));
            assertTrue(v1.lessThanOrEqualTo(v1));
        }

        @Test
        public void shouldOverrideEqualsMethod() {
            Version v1 = Version.valueOf("2.3.7.0");
            Version v2 = Version.valueOf("2.3.7.0");
            Version v3 = Version.valueOf("1.3.7.0");
            assertTrue(v1.equals(v1));
            assertTrue(v1.equals(v2));
            assertFalse(v1.equals(v3));
        }

        @Test
        public void shouldCorrectlyCompareAllVersionsFromSpecification() {
            String[] versions = { "1.0.0.0-alpha", "1.0.0.0-alpha.1", "1.0.0.0-alpha.beta", "1.0.0.0-beta",
                    "1.0.0.0-beta.2", "1.0.0.0-beta.11", "1.0.0.0-rc.1", "1.0.0.0", "2.0.0.0", "2.1.0.0", "2.1.1.0" };
            for (int i = 1; i < versions.length; i++) {
                Version v1 = Version.valueOf(versions[i - 1]);
                Version v2 = Version.valueOf(versions[i]);
                assertTrue(v1.lessThan(v2));
            }
        }

        @Test
        public void shouldHaveStaticFactoryMethod() {
            Version v = Version.valueOf("1.0.0.0-rc.1+build.1");
            assertEquals(1, v.getMajorVersion());
            assertEquals(0, v.getMinorVersion());
            assertEquals(0, v.getPatchVersion());
            assertEquals("1.0.0.0", v.getNormalVersion());
            assertEquals("rc.1", v.getPreReleaseVersion());
            assertEquals("build.1", v.getBuildMetadata());
        }

        @Test
        public void shouldProvideIncrementMajorVersionMethod() {
            Version v = Version.valueOf("1.2.3.0");
            Version incrementedMajor = v.incrementMajorVersion();
            assertEquals("2.0.0.0", incrementedMajor.toString());
        }

        @Test
        public void shouldIncrementMajorVersionWithPreReleaseIfProvided() {
            Version v = Version.valueOf("1.2.3.0");
            Version incrementedMajor = v.incrementMajorVersion("beta");
            assertEquals("2.0.0.0-beta", incrementedMajor.toString());
        }

        @Test
        public void shouldProvideIncrementMinorVersionMethod() {
            Version v = Version.valueOf("1.2.3.0");
            Version incrementedMinor = v.incrementMinorVersion();
            assertEquals("1.3.0.0", incrementedMinor.toString());
        }

        @Test
        public void shouldIncrementMinorVersionWithPreReleaseIfProvided() {
            Version v = Version.valueOf("1.2.3.0");
            Version incrementedMinor = v.incrementMinorVersion("alpha");
            assertEquals("1.3.0.0-alpha", incrementedMinor.toString());
        }

        @Test
        public void shouldProvideIncrementBuildVersionMethod() {
            Version v = Version.valueOf("1.2.3.0");
            Version incrementedBuild = v.incrementBuildVersion();
            assertEquals("1.2.4.0", incrementedBuild.toString());
        }

        @Test
        public void shouldIncrementBuildVersionWithPreReleaseIfProvided() {
            Version v = Version.valueOf("1.2.3.0");
            Version incrementedPatch = v.incrementBuildVersion("rc");
            assertEquals("1.2.4.0-rc", incrementedPatch.toString());
        }

        @Test
        public void shouldProvideIncrementSubBuildVersionMethod() {
            Version v = Version.valueOf("1.2.3.0");
            Version incrementedBuild = v.incrementSubBuildVersion();
            assertEquals("1.2.3.1", incrementedBuild.toString());
        }

        @Test
        public void shouldIncrementSubBuildVersionWithPreReleaseIfProvided() {
            Version v = Version.valueOf("1.2.3.0");
            Version incrementedPatch = v.incrementSubBuildVersion("rc");
            assertEquals("1.2.3.1-rc", incrementedPatch.toString());
        }

        @Test
        public void shouldDropBuildMetadataWhenIncrementing() {
            Version v = Version.valueOf("1.2.3.0-alpha+build");

            Version major1 = v.incrementMajorVersion();
            assertEquals("2.0.0.0", major1.toString());
            Version major2 = v.incrementMajorVersion("beta");
            assertEquals("2.0.0.0-beta", major2.toString());

            Version minor1 = v.incrementMinorVersion();
            assertEquals("1.3.0.0", minor1.toString());
            Version minor2 = v.incrementMinorVersion("beta");
            assertEquals("1.3.0.0-beta", minor2.toString());

            Version build1 = v.incrementBuildVersion();
            assertEquals("1.2.4.0", build1.toString());
            Version build2 = v.incrementBuildVersion("beta");
            assertEquals("1.2.4.0-beta", build2.toString());

            Version subBuild1 = v.incrementSubBuildVersion();
            assertEquals("1.2.3.1", subBuild1.toString());
            Version subBuild2 = v.incrementSubBuildVersion("beta");
            assertEquals("1.2.3.1-beta", subBuild2.toString());
        }

        @Test
        public void shouldProvideSetPreReleaseVersionMethod() {
            Version v1 = Version.valueOf("1.0.0.0");
            Version v2 = v1.setPreReleaseVersion("alpha");
            assertEquals("1.0.0.0-alpha", v2.toString());
        }

        @Test
        public void shouldDropBuildMetadataWhenSettingPreReleaseVersion() {
            Version v1 = Version.valueOf("1.0.0.0-alpha+build");
            Version v2 = v1.setPreReleaseVersion("beta");
            assertEquals("1.0.0.0-beta", v2.toString());
        }

        @Test
        public void shouldProvideSetBuildMetadataMethod() {
            Version v1 = Version.valueOf("1.0.0.0");
            Version v2 = v1.setBuildMetadata("build");
            assertEquals("1.0.0.0+build", v2.toString());
        }

        @Test
        public void shouldProvideIncrementPreReleaseVersionMethod() {
            Version v1 = Version.valueOf("1.0.0.0-beta.1");
            Version v2 = v1.incrementPreReleaseVersion();
            assertEquals("1.0.0.0-beta.2", v2.toString());
        }

        @Test
        public void shouldThrowExceptionWhenIncrementingPreReleaseIfItsNull() {
            Version v1 = Version.valueOf("1.0.0.0");
            try {
                v1.incrementPreReleaseVersion();
            } catch (NullPointerException e) {
                return;
            }
            fail("Method was expected to throw NullPointerException");
        }

        @Test
        public void shouldDropBuildMetadataWhenIncrementingPreReleaseVersion() {
            Version v1 = Version.valueOf("1.0.0.0-beta.1+build");
            Version v2 = v1.incrementPreReleaseVersion();
            assertEquals("1.0.0.0-beta.2", v2.toString());
        }

        @Test
        public void shouldProvideIncrementBuildMetadataMethod() {
            Version v1 = Version.valueOf("1.0.0.0+build.1");
            Version v2 = v1.incrementBuildMetadata();
            assertEquals("1.0.0.0+build.2", v2.toString());
        }

        @Test
        public void shouldThrowExceptionWhenIncrementingBuildIfItsNull() {
            Version v1 = Version.valueOf("1.0.0.0");
            try {
                v1.incrementBuildMetadata();
            } catch (NullPointerException e) {
                return;
            }
            fail("Method was expected to throw NullPointerException");
        }

        @Test
        public void shouldBeImmutable() {
            Version version = Version.valueOf("1.2.3.0-alpha+build");

            Version incementedMajor = version.incrementMajorVersion();
            assertNotSame(version, incementedMajor);

            Version incementedMinor = version.incrementMinorVersion();
            assertNotSame(version, incementedMinor);

            Version incementedPatch = version.incrementBuildVersion();
            assertNotSame(version, incementedPatch);

            Version preReleaseSet = version.setPreReleaseVersion("alpha");
            assertNotSame(version, preReleaseSet);

            Version buildSet = version.setBuildMetadata("build");
            assertNotSame(version, buildSet);

            Version incrementedPreRelease = version.incrementPreReleaseVersion();
            assertNotSame(version, incrementedPreRelease);

            Version incrementedBuild = version.incrementBuildMetadata();
            assertNotSame(version, incrementedBuild);
        }

        @Test
        public void shouldBeAbleToCompareWithoutIgnoringBuildMetadata() {
            Version v1 = Version.valueOf("1.3.7.0-beta+build.1");
            Version v2 = Version.valueOf("1.3.7.0-beta+build.2");
            assertTrue(0 == v1.compareTo(v2));
            assertTrue(0 > v1.compareWithBuildsTo(v2));
        }

        @Test
        public void shouldCheckIfVersionSatisfiesExpression() {
            Version v = Version.valueOf("2.0.0.0-beta");
            assertTrue(v.satisfies(gte("1.0.0.0").and(lt("2.0.0.0"))));
            assertFalse(v.satisfies(gte("2.0.0.0").and(lt("3.0.0.0"))));
        }
    }

    public static class EqualsMethodTest {

        @Test
        public void shouldBeReflexive() {
            Version v1 = Version.valueOf("2.3.7.0");
            assertTrue(v1.equals(v1));
        }

        @Test
        public void shouldBeSymmetric() {
            Version v1 = Version.valueOf("2.3.7.0");
            Version v2 = Version.valueOf("2.3.7.0");
            assertTrue(v1.equals(v2));
            assertTrue(v2.equals(v1));
        }

        @Test
        public void shouldBeTransitive() {
            Version v1 = Version.valueOf("2.3.7.0");
            Version v2 = Version.valueOf("2.3.7.0");
            Version v3 = Version.valueOf("2.3.7.0");
            assertTrue(v1.equals(v2));
            assertTrue(v2.equals(v3));
            assertTrue(v1.equals(v3));
        }

        @Test
        public void shouldBeConsistent() {
            Version v1 = Version.valueOf("2.3.7.0");
            Version v2 = Version.valueOf("2.3.7.0");
            assertTrue(v1.equals(v2));
            assertTrue(v1.equals(v2));
            assertTrue(v1.equals(v2));
        }

        @Test
        public void shouldReturnFalseIfOtherVersionIsOfDifferentType() {
            Version v1 = Version.valueOf("2.3.7.0");
            assertFalse(v1.equals(new String("2.3.7.0")));
        }

        @Test
        public void shouldReturnFalseIfOtherVersionIsNull() {
            Version v1 = Version.valueOf("2.3.7.0");
            Version v2 = null;
            assertFalse(v1.equals(v2));
        }

        @Test
        public void shouldIgnoreBuildMetadataWhenCheckingForEquality() {
            Version v1 = Version.valueOf("2.3.7.0-beta+build");
            Version v2 = Version.valueOf("2.3.7.0-beta");
            assertTrue(v1.equals(v2));
        }
    }

    public static class HashCodeMethodTest {

        @Test
        public void shouldReturnSameHashCodeIfVersionsAreEqual() {
            Version v1 = Version.valueOf("2.3.7.0-beta+build");
            Version v2 = Version.valueOf("2.3.7.0-beta");
            assertTrue(v1.equals(v2));
            assertEquals(v1.hashCode(), v2.hashCode());
        }
    }

    public static class ToStringMethodTest {

        @Test
        public void shouldReturnStringRepresentation() {
            String value = "1.2.3.0-beta+build";
            Version v = Version.valueOf(value);
            assertEquals(value, v.toString());
        }
    }

    public static class BuilderTest {

        @Test
        public void shouldBuildVersionInSteps() {
            Version.Builder builder = new Version.Builder();
            builder.setNormalVersion("1.0.0.0");
            builder.setPreReleaseVersion("alpha");
            builder.setBuildMetadata("build");
            assertEquals(Version.valueOf("1.0.0.0-alpha+build"), builder.build());
        }

        @Test
        public void shouldBuildVersionFromNormalVersion() {
            Version.Builder builder = new Version.Builder("1.0.0.0");
            assertEquals(Version.valueOf("1.0.0.0"), builder.build());
        }

        @Test
        public void shouldBuildVersionWithPreReleaseVersion() {
            Version.Builder builder = new Version.Builder("1.0.0.0");
            builder.setPreReleaseVersion("alpha");
            assertEquals(Version.valueOf("1.0.0.0-alpha"), builder.build());
        }

        @Test
        public void shouldBuildVersionWithBuildMetadata() {
            Version.Builder builder = new Version.Builder("1.0.0.0");
            builder.setBuildMetadata("build");
            assertEquals(Version.valueOf("1.0.0.0+build"), builder.build());
        }

        @Test
        public void shouldBuildVersionWithPreReleaseVersionAndBuildMetadata() {
            Version.Builder builder = new Version.Builder("1.0.0.0");
            builder.setPreReleaseVersion("alpha");
            builder.setBuildMetadata("build");
            assertEquals(Version.valueOf("1.0.0.0-alpha+build"), builder.build());
        }

        @Test
        public void shouldImplementFluentInterface() {
            Version.Builder builder = new Version.Builder();
            Version version = builder.setNormalVersion("1.0.0.0").setPreReleaseVersion("alpha")
                    .setBuildMetadata("build").build();
            assertEquals(Version.valueOf("1.0.0.0-alpha+build"), version);
        }
    }

    public static class BuildAwareOrderTest {

        @Test
        public void shouldCorrectlyCompareAllVersionsWithBuildMetadata() {
            String[] versions = { "1.0.0.0-alpha", "1.0.0.0-alpha.1", "1.0.0.0-beta.2", "1.0.0.0-beta.11",
                    "1.0.0.0-rc.1", "1.0.0.0-rc.1+build.1", "1.0.0.0", "1.0.0.0+0.3.7", "1.3.7.0+build",
                    "1.3.7.0+build.2.b8f12d7", "1.3.7.0+build.11.e0f985a" };
            for (int i = 1; i < versions.length; i++) {
                Version v1 = Version.valueOf(versions[i - 1]);
                Version v2 = Version.valueOf(versions[i]);
                assertTrue(0 > Version.BUILD_AWARE_ORDER.compare(v1, v2));
            }
        }
    }
}
