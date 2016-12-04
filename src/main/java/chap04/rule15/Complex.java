package chap04.rule15;

/**
 * @author Kj Nam
 * @since 2016-12-04
 */
public final class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart() {
        return re;
    }

    public double imagenaryPart() {
        return im;
    }

    public Complex add(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex subtract(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex multiple(Complex c) {
        return new Complex(re * c.re - im * c.im,
                        re * c.im + im * c.re);
    }

    public Complex divide(Complex c) {
        double temp = (c.re * c.re) + (c.im * c.im);
        return new Complex((re * c.re + im * c.im) / temp,
                    (im * c.re + re * c.im) / temp);
    }

    @Override
    public int hashCode() {
        int result = 17 + hashDouble(re);
        result = 31 * result + hashDouble(im);
        return result;
    }

    public static int hashDouble(double val) {
        long longBits = Double.doubleToLongBits(val);
        return (int) (longBits ^ (longBits >>> 32));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Complex)) {
            return false;
        }
        Complex c = (Complex) o;

        return Double.compare(re, c.re) == 0
                && Double.compare(im, c.im) == 0;
    }

    @Override
    public String toString() {
        return "(" + re + " + " + im + "i)";
    }
}
