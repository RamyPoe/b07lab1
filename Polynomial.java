public class Polynomial {
    private static final double ERR = 0.00001;

    double[] coeff;

    public Polynomial() {
        coeff = new double[]{0};
    }

    public Polynomial(double[] coeff) {
        this.coeff = coeff;
    }
    
    public Polynomial add(Polynomial p) {
        int max_deg = p.coeff.length > coeff.length ? p.coeff.length : coeff.length;

        double[] nCoeff = new double[max_deg];
        for (int i = 0; i < max_deg; i++) {
            if (i < p.coeff.length) { nCoeff[i] += p.coeff[i]; };
            if (i < coeff.length) { nCoeff[i] += coeff[i]; };
        }
        return new Polynomial(nCoeff);
    }

    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coeff.length; i++) {
            result += coeff[i] * pow(x, i);
            // System.out.println(coeff[i] + " * " + pow(coeff[i], i) + " = " + coeff[i] * pow(coeff[i], i));
        }
        return result;
    }

    private double pow(double b, int e) {
        double result = 1;
        for (int i = 0; i < e; i++) { result *= b; }
        return result;
    }

    public boolean hasRoot(int x) {
        double num = evaluate(x);
        return -ERR < num && num < ERR;
    }
}