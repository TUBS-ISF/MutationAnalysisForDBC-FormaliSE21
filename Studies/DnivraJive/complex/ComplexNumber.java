/* === This file is part of Jive JML dynamic program verifier ===
 *
 *   Copyright 2012, Arvind S Raj <sraj[dot]arvind[at]gmail[dot]com>
 *
 *   Jive JML dynamic program verifier is free software: you can redistribute it
 *   and/or modify it under the terms of the GNU General Public License as
 *   published by the Free Software Foundation, either version 3 of the License,
 *   or (at your option) any later version.
 *
 *   Jive JML Dynamic Program Verifier is distributed in the hope that it
 *   will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *   warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Jive JML Dynamic Program Verifier. If not, see
 *   <http://www.gnu.org/licenses/>.
 *
 *   This class is based on the sample Complex class available along with
 *   Java Modelling Language examples.
 */

package complex;

//@ model import org.jmlspecs.models.JMLDouble;

public class ComplexNumber {
    //@ public ghost static final double tolerance = 0.005;

    // The real part of the complex number
    private /*@ spec_public @*/ double realPart;

    // The imaginary part of the complex number
    private /*@ spec_public @*/ double imgPart;

    //Initialize to (0 + (0*i))

    //@  ensures this.realPart == 0.0 && this.imgPart == 0.0;
    public ComplexNumber() {
        this(0.0);
    }

    //Initialize to (real + (0 * i))

    /*@  requires !Double.isNaN(real);
      @  ensures this.realPart == real && this.imgPart == 0.0;
      @ also
      @  requires Double.isNaN(real);
      @  ensures Double.isNaN(this.realPart) && this.imgPart == 0.0;
      @*/
    public ComplexNumber(double real) {
        this(real, 0);
    }

    //Initialize to (real + (img*i))

    /*@
      @ ensures !Double.isNaN(real) ==> this.realPart == real;
      @ ensures !Double.isNaN(img) ==> this.imgPart == img;
      @ ensures Double.isNaN(real) ==> Double.isNaN(this.realPart);
      @ ensures Double.isNaN(img) ==> Double.isNaN(this.imgPart);
      @*/
    public ComplexNumber(double real, double img) {
        this.realPart = real;
        this.imgPart = img;
    }

    //Return the real part of the complex number

    /*@ ensures \result == this.realPart;
      @*/  
    public double getRealPart() {
        return realPart;
    }

    //return the imaginary part of the complex number

    /*@ ensures \result == this.imgPart;
      @*/
    public double getImgPart() {
        return imgPart;
    }

    //return the magnitude of the complex number

    /*@ ensures JMLDouble.approximatelyEqualTo(
      @             StrictMath.sqrt(realPart*realPart
      @                       + imgPart*imgPart),
      @             \result,
      @             tolerance);
      @*/
    public double getMagnitude() {
        return StrictMath.sqrt(realPart*realPart + imgPart*imgPart);
    }

    //return the angle of the complex number

    /*@ ensures JMLDouble.approximatelyEqualTo(
      @             StrictMath.atan2(this.imgPart, this.realPart),
      @             \result,
      @             tolerance);
      @*/
    public double getAngle() {
        return StrictMath.atan2(imgPart, realPart);
    }

    //return sum of a complex number with this

    //@ requires_redundantly num != null;
    //@ ensures_redundantly \result != null;
    /*@ ensures JMLDouble.approximatelyEqualTo(
      @             this.realPart + num.realPart,
      @             \result.realPart,
      @             tolerance);
      @ ensures JMLDouble.approximatelyEqualTo(
      @             this.imgPart + num.imgPart,
      @             \result.imgPart,
      @             tolerance);
      @*/
    public ComplexNumber addComplexNumber(ComplexNumber num) {
        return new ComplexNumber(
                                 realPart + num.getRealPart(),
                                 imgPart + num.getImgPart()
                                );
    }
    
    //return difference of this and a complex number

    //@ requires_redundantly num != null; 
    //@ ensures_redundantly \result != null;
    /*@ ensures JMLDouble.approximatelyEqualTo(
      @             this.realPart - num.realPart,
      @             \result.realPart,
      @             tolerance);
      @ ensures JMLDouble.approximatelyEqualTo(
      @             this.imgPart - num.imgPart,
      @             \result.imgPart,
      @             tolerance);
      @*/
    public ComplexNumber subtractComplexNumber(ComplexNumber num) {
        return new ComplexNumber(
                                 realPart - num.getRealPart(),
                                 imgPart - num.getImgPart()
                                );
    }

    //returns the produce of num and this

    /*@  requires_redundantly num != null;
      @  ensures_redundantly \result != null;
      @  ensures JMLDouble.approximatelyEqualTo(
      @             this.realPart * num.realPart - this.imgPart * num.imgPart,
      @             \result.realPart,
      @             tolerance);
      @  ensures JMLDouble.approximatelyEqualTo(
      @             this.realPart * num.imgPart + imgPart * num.realPart,
      @             \result.imgPart,
      @             tolerance);
      @*/
    public ComplexNumber multiplyComplexNumber(ComplexNumber num) {
        return new ComplexNumber(
                                 (realPart * num.getRealPart()) - (imgPart * num.getImgPart()),
                                 (realPart * num.getImgPart()) + (imgPart * num.getRealPart())
                                );
    }

    /*@
      @ requires_redundantly num != null;
      @ ensures_redundantly \result != null;
      @ ensures JMLDouble.approximatelyEqualTo(
      @             (this.realPart * num.realPart + this.imgPart * num.realPart)/(num.realPart * num.realPart - num.imgPart * num.imgPart),
      @             \result.realPart,
      @             tolerance);
      @ ensures JMLDouble.approximatelyEqualTo(
      @             (this.imgPart * num.realPart - this.realPart * num.imgPart)/(num.realPart * num.realPart - num.imgPart * num.imgPart),
      @             \result.imgPart,
      @             tolerance);
      @*/
    public ComplexNumber divideComplexNumber(ComplexNumber num) {
        ComplexNumber temp = new ComplexNumber(num.getRealPart(), (-1 * num.getImgPart()));
        temp = multiplyComplexNumber(temp);
        double magnitudeSquare = num.getMagnitude() * num.getMagnitude();
        return new ComplexNumber(
                                 temp.getRealPart()/magnitudeSquare,
                                 temp.getImgPart()/magnitudeSquare
                                );
    }

    //Checks if num equals this

    /*@ requires num != null;
      @ ensures \result == ((this.realPart == num.realPart)&&(this.imgPart == num.imgPart));
      @*/
    public boolean equals(ComplexNumber num) {
        return ((realPart == num.getRealPart()) && (imgPart == num.getImgPart()));
    }

    public String toString() {
        String result = "(" + this.getRealPart();
        if(this.getImgPart() < 0) {
            result += " - " + Math.abs(this.getImgPart()) + ")";
        }
        else {
            result += " + " + this.getImgPart() + ")";
        }
        return result;
    }
};
