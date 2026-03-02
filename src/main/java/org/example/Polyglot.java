package org.example;
import org.graalvm.polyglot.*;

public class Polyglot {

    public static void main(String[] args) {

        // Creează context Polyglot
        Context polyglot = Context.create();

        // Tablou de cuvinte definit în JavaScript
        Value array = polyglot.eval("js","[\"If\",\"we\",\"run\",\"the\",\"java\",\"command\"]");

        // Parcurge fiecare cuvânt
        for (int i = 0; i < array.getArraySize(); i++) {
            String element = array.getArrayElement(i).asString();

            // Suma de control calculată în Python
            int crc = SumCRC(element);

            System.out.println(element + " -> " + crc);
        }

        polyglot.close();
    }

    // Metoda care calculează checksum folosind Python
    private static int SumCRC(String token) {

        Context polyglot = Context.newBuilder().allowAllAccess(true).build();

        // Polinom de grad 5: x + 2x^2 + 3x^3 + 4x^4 + 5x^5
        Value result = polyglot.eval("python",
                "sum((x + 2*(x**2) + 3*(x**3) + 4*(x**4) + 5*(x**5)) " + "for x in [ord(ch) for ch in '" + token + "'])");

        return result.asInt();
    }
}