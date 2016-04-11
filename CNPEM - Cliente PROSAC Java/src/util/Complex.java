/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

public class Complex
{
    private double real, imag;
    
    public Complex()
    {
        real = imag = 0;
    }
    
    public Complex(double real, double imag)
    {
        this.real = real;
        this.imag = imag;
    }
    
    public Complex times(Complex value)
    {
        double ret_real, ret_imag;
        
        ret_real = real*value.getReal() - imag*value.getImag();
        ret_imag = real*value.getImag() + imag*value.getReal();
        
        return new Complex(ret_real, ret_imag);
    }
    
    public Complex times(double value)
    {
        return new Complex(real*value, imag*value);
    }

    public double getImag() {
        return imag;
    }

    public double getReal() {
        return real;
    }

    public void setImag(double imag) {
        this.imag = imag;
    }

    public void setReal(double real) {
        this.real = real;
    }
    
    public double magnitude()
    {
        return Math.sqrt(real*real + imag*imag);
    }
    
    public Complex conjugate()
    {
        return new Complex(real, -imag);
    }
    
    public Complex plus(Complex value)
    {
        return new Complex(real + value.getReal(), imag + value.getImag());
    }
    
    public Complex minus(Complex value)
    {
        return new Complex(real - value.getReal(), imag - value.getImag());
    }
    
    @Override
    public String toString()
    {        
        return String.valueOf(real) + " | " + String.valueOf(imag);
    }
    
}
