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

package int_complex;

public class ComplexNumber {
    // The real part of the complex number
    private /*@ spec_public @*/ int realPart;

    // The imaginary part of the complex number
    private /*@ spec_public @*/ int imgPart;

    //Initialize to (0 + (0*i))

    //@  ensures this.realPart == 0 && this.imgPart == 0;
    public ComplexNumber() {
        this(0);
    }

    //Initialize to (real + (0 * i))

    /*@  requires !Double.isNaN(real);
      @  ensures this.realPart == real && this.imgPart == 0;
      @ also
      @  requires Double.isNaN(real);
      @  ensures Double.isNaN(this.realPart) && this.imgPart == 0;
      @*/
    public ComplexNumber(int real) {
        this(real, 0);
    }

    //Initialize to (real + (img*i))

    /*@
      @ ensures !Double.isNaN(real) ==> this.realPart == real;
      @ ensures !Double.isNaN(img) ==> this.imgPart == img;
      @ ensures Double.isNaN(real) ==> Double.isNaN(this.realPart);
      @ ensures Double.isNaN(img) ==> Double.isNaN(this.imgPart);
      @*/
    public ComplexNumber(int real, int img) {
        this.realPart = real;
        this.imgPart = img;
    }

    //Return the real part of the complex number

    /*@ ensures \result == this.realPart;
      @*/  
    public int getRealPart() {
        return realPart;
    }

    //return the imaginary part of the complex number

    /*@ ensures \result == this.imgPart;
      @*/
    public int getImgPart() {
        return imgPart;
    }

    //return the magnitude of the complex number

    /*@ ensures \result == StrictMath.sqrt(realPart*realPart
      @                       + imgPart*imgPart);
      @*/
    public double getMagnitude() {
        return StrictMath.sqrt(realPart*realPart + imgPart*imgPart);
    }

    //return the angle of the complex number

    /*@ ensures \result == StrictMath.atan2(this.imgPart, this.realPart);
      @*/
    public double getAngle() {
        return StrictMath.atan2(imgPart, realPart);
    }

    //return sum of a complex number with this

    /*@ requires_redundantly num != null;
      @ ensures_redundantly \result != null;
      @ ensures \result.realPart == this.realPart + num.realPart;
      @ ensures \result.imgPart == this.imgPart + num.imgPart;
      @*/
    public ComplexNumber addComplexNumber(ComplexNumber num) {
        return new ComplexNumber(
                                 realPart + num.getRealPart(),
                                 imgPart + num.getImgPart()
                                );
    }
    
    //return difference of this and a complex number

    /*@ requires_redundantly num != null; 
      @ ensures_redundantly \result != null;
      @ ensures \result.realPart == this.realPart - num.realPart;
      @ ensures \result.imgPart == this.imgPart - num.imgPart;
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
      @  ensures \result.realPart == this.realPart * num.realPart - this.imgPart * num.imgPart;
      @  ensures \result.imgPart == this.realPart * num.imgPart + imgPart * num.realPart;
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
      @ ensures \result.realPart == (this.realPart * num.realPart + this.imgPart * num.realPart)
      @                             /(num.realPart * num.realPart - num.imgPart * num.imgPart);
      @ ensures \result.imgPart == (this.imgPart * num.realPart - this.realPart * num.imgPart)
      @                            /(num.realPart * num.realPart - num.imgPart * num.imgPart);
      @*/
    public ComplexNumber divideComplexNumber(ComplexNumber num) {
        ComplexNumber temp = new ComplexNumber(num.getRealPart(), (-1 * num.getImgPart()));
        temp = multiplyComplexNumber(temp);
        double magnitudeSquare = num.getMagnitude() * num.getMagnitude();
        return new ComplexNumber(
                                 (int)(temp.getRealPart()/magnitudeSquare),
                                 (int)(temp.getImgPart()/magnitudeSquare)
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
            result += " - " + Math.abs(this.getImgPart()) + "i)";
        }
        else {
            result += " + " + this.getImgPart() + "i)";
        }
        return result;
    }
};
