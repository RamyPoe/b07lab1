import java.util.Arrays;

public class Driver {
    public static void main(String[] args) {


        // double[] c1 = {6, -9, 5};
        // int[] e1 = {0, 2, 3};
        // // Polynomial p1 = new Polynomial(c1, e1);
        // Polynomial p1 = new Polynomial("-54.0x^4+81.0x^6-45.0x^7");
        
        
        // double[] c2 = {-2, 9, -9};
        // int[] e2 = {1, 2, 4};
        // // Polynomial p2 = new Polynomial(c2, e2);
        // // Polynomial p2 = new Polynomial("-12.0x^1+54.0x^2+18.0x^3-91.0x^4+45.0x^5");

        // System.out.println(p1);
        // System.out.println(p2); 
        // System.out.println();

        // Polynomial m1 = p2.add(p1);
        // // Polynomial m2 = p1.multiply(p2);

        // System.out.println();
        // System.out.println(m1);
        // // System.out.println(m2);



        
        System.out.println("\n=====================================\n");
        

        
        double[] c1 = {6, -9, 5};
        int[] e1 = {0, 2, 3};
        Polynomial p1 = new Polynomial(c1, e1);
        System.out.println("[P1] = " + p1);
        
        Polynomial p2 = new Polynomial("-9x^4-2x^1+9x^2");
        System.out.println("[P2] = " + p2);



        Polynomial s = p2.add(p1);
        System.out.println("[S] = [P1] + [P2] = " + s + "\n");

        System.out.println("S(0.1) = " + s.evaluate(0.1));
        if(s.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");
        

        System.out.println("\n=====================================\n");
        

        Polynomial m = p2.multiply(p1);
        System.out.println("[M] = [P1] * [P2] = " + m + "\n");
        
        System.out.println("P(0.1) = " + m.evaluate(0.1));
        if(m.hasRoot(-1))
            System.out.println("-1 is a root of s");
        else
        System.out.println("-1 is not a root of s");

        System.out.println("\n=====================================\n");
        
        p1 = new Polynomial("2-1x^2");
        p2 = new Polynomial("2+1x^2");
        m = p1.multiply(p2);
        
        System.out.println("[P1] = " + p1);
        System.out.println("[P2] = " + p2);
        System.out.println();
        System.out.println("[M] = [P1] * [P2] = " + m);
        

        System.out.println("\n=====================================\n");
    }
}
