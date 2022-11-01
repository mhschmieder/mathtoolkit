/**
 * MIT License
 *
 * Copyright (c) 2020, 2022 Mark Schmieder
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the MathToolkit Library
 *
 * You should have received a copy of the MIT License along with the
 * MathToolkit Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/mathtoolkit
 */
package com.mhschmieder.mathtoolkit;

import java.math.RoundingMode;
import java.util.Random;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

import com.mhschmieder.commonstoolkit.lang.NumberUtilities;

/**
 * This class provides methods that go slightly beyond the scope of core
 * java.math.Math, such as additional trigonometry methods.
 */
public final class MathUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private MathUtilities() {}

    // Reference for random number generator, to make sure there is only one
    // instance each time an application that uses this class is invoked.
    // Otherwise, successive calls would use the same root and generally find
    // the same first random number, defeating the purpose altogether.
    private static Random _randomNumberGenerator = null;

    public static double boundedValue( final double value, final double min, final double max ) {
        // if max < value, return max
        // if min > value, return min
        // if min > max, return min
        return FastMath.min( FastMath.max( value, min ), FastMath.max( min, max ) );
    }

    // Trigonometric functions.
    public static double sec( final double x ) {
        // NOTE: Using home-grown NumberUtilities check for finite number for
        // now, as we currently have some clients that are stuck on Java 7.
        // TODO: Also check for non-computable values by first unwrapping the
        // period.
        if ( !NumberUtilities.isFinite( x ) ) {
            return Double.NaN;
        }

        final double cosx = FastMath.cos( x );
        if ( cosx == 0.0d ) {
            return Double.NaN;
        }

        return 1.0d / cosx;
    }

    public static double cot( final double x ) {
        // NOTE: Using home-grown NumberUtilities check for finite number for
        // now, as we currently have some clients that are stuck on Java 7.
        // TODO: Also check for non-computable values by first unwrapping the
        // period.
        if ( !NumberUtilities.isFinite( x ) ) {
            return Double.NaN;
        }

        final double tanx = FastMath.tan( x );
        if ( tanx == 0.0d ) {
            return Double.NaN;
        }

        return 1.0d / tanx;
    }

    public static double csc( final double x ) {
        // NOTE: Using home-grown NumberUtilities check for finite number for
        // now, as we currently have some clients that are stuck on Java 7.
        // TODO: Also check for non-computable values by first unwrapping the
        // period.
        if ( !NumberUtilities.isFinite( x ) ) {
            return Double.NaN;
        }

        final double sinx = FastMath.sin( x );
        if ( sinx == 0.0d ) {
            return Double.NaN;
        }

        return 1.0d / FastMath.sin( x );
    }

    // Return the function sinc(x), where sinc(x) is defined as sin(x) / x.
    public static double sinc( final double x ) {
        return ( FastMath.abs( x ) < 1.0e-30 ) ? 1.0d : FastMath.sin( x ) / x;
    }

    // Return the number of fractional digits required to display the given
    // number. No number larger than 16 is returned (if more than 16 digits are
    // required, 16 is returned).
    public static int getNumberOfFractionalDigits( final double num ) {
        int numdigits = 0;
        double numAdjusted = num;
        while ( ( numdigits <= 15 ) && ( numAdjusted != FastMath.floor( numAdjusted ) ) ) {
            numAdjusted *= 10.0d;
            numdigits += 1;
        }
        return numdigits;
    }

    // Return the number of integer digits required to display the given number.
    // No number larger than 16 is returned (if more than 16 digits are
    // required, 16 is returned).
    public static int getNumberOfIntegerDigits( final double num ) {
        int numdigits = 0;
        double numAdjusted = num;
        while ( ( numdigits <= 15 ) && ( ( int ) numAdjusted != 0.0 ) ) {
            numAdjusted /= 10.0d;
            numdigits += 1;
        }
        return numdigits;
    }

    // Return the mantissa of a number represented by mantissa * 2^ (exponent).
    public static double getMantissa( final double number ) {
        final int exponent = FastMath.getExponent( number );
        return number / FastMath.pow( 2.0d, exponent );
    }

    // Logarithm taken to base 2, as a fast convenience method.
    public static double log2( final double x ) {
        return FastMath.log( x ) * MathConstants.LN2_SCALE;
    }

    /**
     * Rounds a float to the specified number of decimal places.
     * 
     * @param number
     *            The float to be rounded
     * @param numberOfDecimalPlaces
     *            The number of decimal places to preserve in rounding
     * @return A float rounded to the specified decimal place
     */
    public static float roundDecimal( final float number, final int numberOfDecimalPlaces )
            throws IllegalArgumentException, NumberFormatException {
        // Use the classroom rounding method, not the one recommended by Apache
        // Commons Math that is mostly relevant to USA based financial
        // institutions (specifically, ROUND_EVEN from Java RoundingMode enum).
        final float numberRounded = roundDecimal( number,
                                                  numberOfDecimalPlaces,
                                                  RoundingMode.HALF_UP.ordinal() );

        return numberRounded;
    }

    /**
     * Rounds a double to the specified number of decimal places.
     * 
     * @param number
     *            The double to be rounded
     * @param numberOfDecimalPlaces
     *            The number of decimal places to preserve in rounding
     * @return A double rounded to the specified decimal place
     */
    public static double roundDecimal( final double number, final int numberOfDecimalPlaces )
            throws IllegalArgumentException, NumberFormatException {
        // Use the classroom rounding method, not the one recommended by Apache
        // Commons Math that is mostly relevant to USA based financial
        // institutions (specifically, ROUND_EVEN from Java RoundingMode enum).
        final double numberRounded = roundDecimal( number,
                                                   numberOfDecimalPlaces,
                                                   RoundingMode.HALF_UP.ordinal() );

        return numberRounded;
    }

    /**
     * Rounds up a float to the specified number of decimal places.
     * 
     * @param number
     *            The float to be rounded
     * @param numberOfDecimalPlaces
     *            The number of decimal places to preserve in rounding
     * @return A float rounded up to the specified decimal place
     */
    public static float roundUpDecimal( final float number, final int numberOfDecimalPlaces )
            throws IllegalArgumentException, NumberFormatException {
        final float numberRounded = roundDecimal( number,
                                                  numberOfDecimalPlaces,
                                                  RoundingMode.UP.ordinal() );

        return numberRounded;
    }

    /**
     * Rounds up a double to the specified number of decimal places.
     * 
     * @param number
     *            The double to be rounded
     * @param numberOfDecimalPlaces
     *            The number of decimal places to preserve in rounding
     * @return A double rounded up to the specified decimal place
     */
    public static double roundUpDecimal( final double number, final int numberOfDecimalPlaces )
            throws IllegalArgumentException, NumberFormatException {
        final double numberRounded = roundDecimal( number,
                                                   numberOfDecimalPlaces,
                                                   RoundingMode.UP.ordinal() );

        return numberRounded;
    }

    /**
     * Rounds down a float to the specified number of decimal places.
     * 
     * @param number
     *            The float to be rounded
     * @param numberOfDecimalPlaces
     *            The number of decimal places to preserve in rounding
     * @return A float rounded down to the specified decimal place
     */
    public static float roundDownDecimal( final float number, final int numberOfDecimalPlaces )
            throws IllegalArgumentException, NumberFormatException {
        final float numberRounded = roundDecimal( number,
                                                  numberOfDecimalPlaces,
                                                  RoundingMode.DOWN.ordinal() );

        return numberRounded;
    }

    /**
     * Rounds down a double to the specified number of decimal places.
     * 
     * @param number
     *            The double to be rounded
     * @param numberOfDecimalPlaces
     *            The number of decimal places to preserve in rounding
     * @return A double rounded down to the specified decimal place
     */
    public static double roundDownDecimal( final double number, final int numberOfDecimalPlaces )
            throws IllegalArgumentException, NumberFormatException {
        final double numberRounded = roundDecimal( number,
                                                   numberOfDecimalPlaces,
                                                   RoundingMode.DOWN.ordinal() );

        return numberRounded;
    }

    /**
     * Rounds a float to the specified number of decimal places given a provided
     * rounding method.
     * 
     * @param number
     *            The float to be rounded
     * @param numberOfDecimalPlaces
     *            The number of decimal places to preserve in rounding
     * @param roundingMethod
     *            The rounding method to use, compatible with BigDecimal
     * @return A float rounded to the specified decimal place with specific
     *         rounding
     */
    public static float roundDecimal( final float number,
                                      final int numberOfDecimalPlaces,
                                      int roundingMethod )
            throws IllegalArgumentException, NumberFormatException {
        // Number of digits after decimal point < 0, is invalid input, so return
        // the number unchanged.
        if ( numberOfDecimalPlaces < 0 ) {
            throw new IllegalArgumentException();
        }

        // Use Apache Commons Math to do the rounding, which wraps FastMath with
        // safe edge case detection and handling for zero decimal places, etc.
        // TODO: Check Apache Commons Math Version 4 to see if anything replaces
        // the removal of this method that was introduced in Version 3.
        final float numberRounded =
                                  Precision.round( number, numberOfDecimalPlaces, roundingMethod );

        return numberRounded;
    }

    /**
     * Rounds a double to the specified number of decimal places given a
     * provided rounding method.
     * 
     * @param number
     *            The double to be rounded
     * @param numberOfDecimalPlaces
     *            The number of decimal places to preserve in rounding
     * @param roundingMethod
     *            The rounding method to use, compatible with BigDecimal
     * @return A double rounded to the specified decimal place with specific
     *         rounding
     */
    public static double roundDecimal( final double number,
                                       final int numberOfDecimalPlaces,
                                       int roundingMethod )
            throws IllegalArgumentException, NumberFormatException {
        // Number of digits after decimal point < 0, is invalid input, so return
        // the number unchanged.
        if ( numberOfDecimalPlaces < 0 ) {
            throw new IllegalArgumentException();
        }

        // Use Apache Commons Math to do the rounding, which wraps FastMath with
        // safe edge case detection and handling for zero decimal places, etc.
        // TODO: Check Apache Commons Math Version 4 to see if anything replaces
        // the removal of this method that was introduced in Version 3.
        final double numberRounded =
                                   Precision.round( number, numberOfDecimalPlaces, roundingMethod );

        return numberRounded;
    }

    /**
     * Discretizes a float to the nearest multiple of a given multiplier.
     * 
     * @param number
     *            The float to be discretized
     * @param multiplier
     *            The multiplier to use for discretization
     * @return The nearest multiple of a given multiplier to a specified float
     */
    public static float discretize( final float number, final float multiplier ) {
        if ( multiplier == 0.0f ) {
            return number;
        }

        final float result = multiplier * FastMath.round( number / multiplier );

        return result;
    }

    /**
     * Discretizes a double to the nearest multiple of a given multiplier.
     * 
     * @param number
     *            The double to be discretized
     * @param multiplier
     *            The multiplier to use for discretization
     * @return The nearest multiple of a given multiplier to a specified double
     */
    public static double discretize( final double number, final double multiplier ) {
        if ( multiplier == 0.0d ) {
            return number;
        }

        final double result = multiplier * FastMath.round( number / multiplier );

        return result;
    }

    /**
     * Discretizes a float to the next highest multiple of a given multiplier.
     * 
     * @param number
     *            The float to be discretized
     * @param multiplier
     *            The multiplier to use for discretization
     * @return The next highest multiple of a given multiplier to a specified
     *         float
     */
    public static float discretizeUp( final float number, final float multiplier ) {
        if ( multiplier == 0.0f ) {
            return number;
        }

        final float result = ( float ) ( multiplier * FastMath.ceil( number / multiplier ) );

        return result;
    }

    /**
     * Discretizes a double to the next highest multiple of a given multiplier.
     * 
     * @param number
     *            The double to be discretized
     * @param multiplier
     *            The multiplier to use for discretization
     * @return The next highest multiple of a given multiplier to a specified
     *         double
     */
    public static double discretizeUp( final double number, final double multiplier ) {
        if ( multiplier == 0.0d ) {
            return number;
        }

        final double result = multiplier * FastMath.ceil( number / multiplier );

        return result;
    }

    /**
     * Discretizes a float to the next lowest multiple of a given multiplier.
     * 
     * @param number
     *            The float to be discretized
     * @param multiplier
     *            The multiplier to use for discretization
     * @return The next lowest multiple of a given multiplier to a specified
     *         float
     */
    public static float discretizeDown( final float number, final float multiplier ) {
        if ( multiplier == 0.0f ) {
            return number;
        }

        final float result = ( float ) ( multiplier * FastMath.floor( number / multiplier ) );

        return result;
    }

    /**
     * Discretizes a double to the next lowest multiple of a given multiplier.
     * 
     * @param number
     *            The double to be discretized
     * @param multiplier
     *            The multiplier to use for discretization
     * @return The next lowest multiple of a given multiplier to a specified
     *         double
     */
    public static double discretizeDown( final double number, final double multiplier ) {
        if ( multiplier == 0.0d ) {
            return number;
        }

        final double result = multiplier * FastMath.floor( number / multiplier );

        return result;
    }

    // Return a Rayleigh-distributed Gaussian random number.
    public static double nextRayleighRandom() {
        if ( _randomNumberGenerator == null ) {
            _randomNumberGenerator = new Random();
        }

        final double x = _randomNumberGenerator.nextGaussian();
        final double y = _randomNumberGenerator.nextGaussian();

        return FastMath.hypot( x, y );
    }

    // Given a number, round up to the nearest power of ten (times 1, 2, or 5).
    // NOTE: The argument must be strictly positive.
    public static double roundUp( final double val ) {
        final int base10Exponent = ( int ) FastMath.floor( FastMath.log10( val ) );
        double valAdjusted = val;
        valAdjusted *= FastMath.pow( 10.0d, -base10Exponent );
        if ( valAdjusted > 5.0d ) {
            valAdjusted = 10.0d;
        }
        else if ( valAdjusted > 2.0d ) {
            valAdjusted = 5.0d;
        }
        else if ( valAdjusted > 1.0d ) {
            valAdjusted = 2.0d;
        }
        valAdjusted *= FastMath.pow( 10.0d, base10Exponent );
        return valAdjusted;
    }

    // Return the simple square of a double.
    public static double sqr( final double x ) {
        return x * x;
    }

    /**
     * Returns a new {@link Complex} object from an existing {@link Complex}.
     *
     * @param z
     *            A {@link Complex} object to use for cloning
     * @return A new {@link Complex} object cloned from a reference
     *         {@link Complex}
     */
    public static Complex cloneComplex( final Complex z ) {
        final Complex newComplex = new Complex( z.getReal(), z.getImaginary() );
        return newComplex;
    }

    /**
     * Returns a new {@link Complex} as the square of a referenced
     * {@link Complex} value.
     *
     * @param z
     *            A {@link Complex} object to use for computing the square
     * @return <tt>Complex</tt> square z1 * z1
     */
    public static Complex sqrComplex( final Complex z ) {
        return new Complex( sqr( z.getReal() ) - sqr( z.getImaginary() ),
                            2.0d * ( z.getReal() * z.getImaginary() ) );
    }

    /**
     * Returns the L2 norm of a {@code Complex} number, which is the sum of
     * the squares of the real and imaginary parts.
     *
     * @param z
     *            A {@link Complex} object to use to compute the norm
     * @return a {@code double} containing the norm of the {@code Complex}
     *         number
     */
    public static double norm( final Complex z ) {
        return ( sqr( z.getReal() ) + sqr( z.getImaginary() ) );
    }

    /**
     * Returns the phase of a {@code Complex} number. Note that this method
     * duplicates the functionality of the {@code arg()} method.
     *
     * @param z
     *            A {@link Complex} object to measure for phase
     * @return Phase of <tt>Complex</tt> object in <i> radians</i>
     */
    public static double phase( final Complex z ) {
        return FastMath.atan2( z.getImaginary(), z.getReal() );
    }

    /**
     * Compares two <code>Complex</code> values numerically according to their
     * absolute values.
     *
     * @param complex
     *            the <code>Complex</code> value to be compared to.
     * @param anotherComplex
     *            the <code>Complex</code> to use for comparing to the first.
     * @return the value <code>0</code> if the absolute value of the argument
     *         <code>anotherComplex</code> is equal to the absolute value of
     *         this <code>Complex</code>; a value less than <code>0</code> if
     *         the absolute value of this <code>Complex</code> is numerically
     *         less than the <code>Complex</code> argument; and a the absolute
     *         value of this <code>Complex</code> is numerically greater than
     *         the <code>Complex</code> argument. (signed comparison).
     * @see java.lang.Comparable
     */
    public static int compare( final Complex complex, final Complex anotherComplex ) {
        final double thisVal = complex.abs();
        final double anotherVal = anotherComplex.abs();

        return ( thisVal < anotherVal ? -1 : ( thisVal == anotherVal ? 0 : 1 ) );
    }
}
