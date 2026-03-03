package org.example;

import org.graalvm.polyglot.*;
import java.util.*;

public class Polyglot {

    static void main(String[] args) {

        Context polyglot = Context.create();
        Value array = polyglot.eval("js","[\"If\",\"we\",\"run\",\"the\",\"java\",\"command\"]");

        Map<Long, List<String>> crcMap = new HashMap<>();

        for (int i = 0; i < array.getArraySize(); i++) {
            String element = array.getArrayElement(i).asString();
            long crc = SumCRC(element);

            crcMap.computeIfAbsent(crc, k -> new ArrayList<>()).add(element);
        }

        for (Map.Entry<Long, List<String>> entry : crcMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                System.out.println("CRC " + entry.getKey() + " -> " + entry.getValue());
            }
        }

        polyglot.close();
    }

    private static long SumCRC(String token) {

        if (token.length() > 2) {
            token = token.substring(1, token.length() - 1);
        } else {
            return 0;
        }

        Context polyglot = Context.newBuilder().allowAllAccess(true).build();

        Value result = polyglot.eval("python","sum((x + 2*(x**2) + 3*(x**3) + 4*(x**4) + 5*(x**5)) " + "for x in [ord(ch) for ch in '" + token + "'])");

        polyglot.close();

        return result.asLong();
    }
}