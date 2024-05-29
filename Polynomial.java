import java.io.*;

public class Polynomial {
    // For double comparison
    private static final double ERR = 0.00001;

    // Non-zero coeff
    private double[] coeff;

    // Corresponding to coeff, should be sorted ascending
    private int[] exponents;

    // Zero polynomial
    public Polynomial() {
        coeff = null;
        exponents = null;
    }

    public Polynomial(double[] coeff, int[] exponents) {
        // Backup in case of bad input
        this();

        // Make sure the lengths are matching
        if (exponents.length == 0 || coeff.length != exponents.length) { return; }

        // Internal structure will use sorted terms
        sortTerms(coeff, exponents);
        this.coeff = coeff;
        this.exponents = exponents;
    } 

    // Reading polynomial from file
    public Polynomial(File f) throws IOException {
        this();

        // Read the line from the file
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine().strip();
        fr.close();

        // Set the arrays based on the string
        setPolynomialFromString(line);
    }

    // Setting the polynomial from a string
    public Polynomial(String line) { this(); setPolynomialFromString(line); }

    // Takes string and assigns arrays based on parsing, used in constructor
    private void setPolynomialFromString(String line) {
        // Edge case, do nothing
        if (line.length() == 0) return;

        // Correct the first term
        if (line.charAt(0) != '-') { line = "+" + line; }

        // Positive lookahead to keep the sign of term
        String[] terms = line.split("((?=[+-]))");

        double[] co = new double[terms.length];
        int[] ex = new int[terms.length];

        // Save coefficient and expoonent for each term
        for (int i = 0; i < terms.length; i++) {
            
            // Implied coefficient and powers
            if (terms[i].charAt(1) == 'x') { terms[i] = new StringBuilder(terms[i]).insert(1, "1").toString(); }
            if (terms[i].charAt(terms[i].length()-1) == 'x') { terms[i] = terms[i] + "1"; }

            // Convert and save the numbers
            String[] term = terms[i].split("x");
            co[i] = Double.parseDouble(term[0]);
            if (term.length > 1) { ex[i] = Integer.parseInt(term[1]); } else { ex[i] = 0; }
        }

        // Underlying structure assumes it is sorted
        sortTerms(co, ex);

        // Save the terms
        this.coeff = co;
        this.exponents = ex;
    }
    
    // Writes the string equivalent of polynomial to file
    public void saveToFile(String filename) throws IOException {
        FileWriter fw = new FileWriter(filename);
        fw.write(this.toString());
        fw.close();
    }

    
    public Polynomial add(Polynomial p) {
        // Edge cases
        if (coeff == null) { return p.copy(); }
        if (p.coeff == null) { return this.copy(); }
        
        // Subtract common exponents to find length of sum
        int poly_sum_length = exponents.length + p.exponents.length;
        int i1 = 0; int i2 = 0;
        while (i1 < exponents.length && i2 < p.exponents.length) {
            if (exponents[i1] == p.exponents[i2]) {
                poly_sum_length--;
                
                // The terms cancel out
                if (doubleEquals(coeff[i1], -p.coeff[i2])) { poly_sum_length--; }

                i1++; i2++;
                continue;
            }
            if (exponents[i1] > p.exponents[i2]) { i2++; } else { i1++; }
        }
        
        // Arrays for the new polynomial
        double[] nCoeff = new double[poly_sum_length];
        int[] nExponents = new int[poly_sum_length];
        
        // Add the terms
        i1 = 0; i2 = 0;
        for (int i = 0; i < poly_sum_length; i++) {

            // Have exhausted first polynomial or ( none are exhausted and second polynomial has smaller exponent )
            if (i1 >= exponents.length || (i2 < p.exponents.length && exponents[i1] > p.exponents[i2])) {
                nCoeff[i] += p.coeff[i2];
                nExponents[i] = p.exponents[i2];
                i2++; continue;
            }
            
            // Have exhausted second polynomial or ( none are exhausted and first polynomial has smaller exponent )
            if (i2 >= p.exponents.length || (i1 < exponents.length && exponents[i1] < p.exponents[i2])) {
                nCoeff[i] += coeff[i1];
                nExponents[i] = exponents[i1];
                i1++; continue;
            }

            // Equal exponents, add both coefficients
            if (exponents[i1] == p.exponents[i2]) {
                // Terms cancel out, don't count as an iteration
                if (doubleEquals(coeff[i1], -p.coeff[i2])) { i--; i1++; i2++; continue; }

                nCoeff[i] += coeff[i1] + p.coeff[i2];
                nExponents[i] = exponents[i1];
                i1++; i2++;
            }

        }

        // Return the new polynomial
        return new Polynomial(nCoeff, nExponents);
    }

    public Polynomial multiply(Polynomial p) {
        // Edge case
        if (coeff == null || p.coeff == null) { return new Polynomial(); }

        // Polynomial accumulator, start with zero polynomial
        Polynomial accPolynomial = new Polynomial();

        // For every term in this polynomial
        for (int i = 0; i < coeff.length; i++) {
            Polynomial tmp = p.copy();
            
            // Multiply first term into polynomial
            for (int j = 0; j < tmp.coeff.length; j++) {
                tmp.coeff[j] *= coeff[i];
                tmp.exponents[j] += exponents[i];
            }

            // Add to the last product
            accPolynomial = accPolynomial.add(tmp);
        }

        // Return the repeated sum
        return accPolynomial;

    }

    // Passes the value of x to the function
    public double evaluate(double x) {
        if (coeff == null) { return 0; }

        double result = 0;
        for (int i = 0; i < coeff.length; i++) {
            result += coeff[i] * pow(x, exponents[i]);
        }
        return result;
    }

    // Returns copy that uses new arrays
    private Polynomial copy() {
        double[] cArr = new double[coeff.length];
        for (int i = 0; i < cArr.length; i++) { cArr[i] = coeff[i]; }
        int[] eArr = new int[exponents.length];
        for (int i = 0; i < eArr.length; i++) { eArr[i] = exponents[i]; }
        return new Polynomial(cArr, eArr);
    }

    // Given the two arrays it will sort them by increasing exponent while keeping correspondence
    private void sortTerms(double[] cArr, int[] eArr) {
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < eArr.length-1; i++) {
                if (eArr[i] > eArr[i+1]) {
                    swapArr(cArr, i, i+1);
                    swapArr(eArr, i, i+1);
                    sorted = false;
                }
            }
        }
    }

    // Used for sorting
    private static void swapArr(int[] arr, int i1, int i2)    { int t = arr[i1]; arr[i1] = arr[i2]; arr[i2] = t; }
    private static void swapArr(double[] arr, int i1, int i2) { double t = arr[i1]; arr[i1] = arr[i2]; arr[i2] = t; }

    // Calculating integer powers
    private static double pow(double b, int e) {
        double result = 1;
        for (int i = 0; i < e; i++) { result *= b; }
        return result;
    }

    private static boolean doubleEquals(double a, double b) { return Math.abs(a-b) <= ERR; }

    public boolean hasRoot(double x) { return doubleEquals(evaluate(x), 0d); }

    @Override
    public String toString() {
        if (coeff == null) { return ""; }

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < exponents.length; i++) {
            if (i != 0 && coeff[i] > 0) out.append("+");
            out.append(String.valueOf(coeff[i]));

            if (exponents[i] == 0) continue;
            out.append("x");
            out.append(String.valueOf(exponents[i]));
        }
        return out.toString();
    }


}