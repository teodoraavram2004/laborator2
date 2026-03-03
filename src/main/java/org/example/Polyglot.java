package org.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;

import javax.imageio.ImageIO;

public class Polyglot {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Nume fisier output (ex: plot.png): ");
        String fileName = scanner.nextLine();

        System.out.print("Cale salvare (ex: /home/user/Desktop): ");
        String path = scanner.nextLine();

        System.out.print("Culoare (red, blue, green, black): ");
        String colorInput = scanner.nextLine();

        Color plotColor = switch (colorInput.toLowerCase()) {
            case "red" -> Color.RED;
            case "blue" -> Color.BLUE;
            case "green" -> Color.GREEN;
            default -> Color.BLACK;
        };

        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        List<String> lines = Files.readAllLines(Path.of("dataset.txt"));
        for (String line : lines) {
            String[] parts = line.split("\\s+");
            x.add(Double.parseDouble(parts[0]));
            y.add(Double.parseDouble(parts[1]));
        }

        int n = x.size();

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += x.get(i);
            sumY += y.get(i);
            sumXY += x.get(i) * y.get(i);
            sumX2 += x.get(i) * x.get(i);
        }

        double b = (n * sumXY - sumX * sumY) /
                (n * sumX2 - sumX * sumX);
        double a = (sumY - b * sumX) / n;

        System.out.println("Ecuatia regresiei:");
        System.out.println("y = " + a + " + " + b + "x");

        int width = 800;
        int height = 600;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        g.drawLine(50, height - 50, width - 50, height - 50);
        g.drawLine(50, 50, 50, height - 50);

        double maxX = Collections.max(x);
        double maxY = Collections.max(y);

        for (int i = 0; i < n; i++) {
            int px = (int) (50 + (x.get(i) / maxX) * (width - 100));
            int py = (int) (height - 50 - (y.get(i) / maxY) * (height - 100));
            g.setColor(plotColor);
            g.fillOval(px - 4, py - 4, 8, 8);
        }

        g.setColor(Color.RED);
        int x1 = 0;
        int y1 = (int) (a + b * x1);
        int x2 = (int) maxX;
        int y2 = (int) (a + b * x2);

        int px1 = 50;
        int py1 = (int) (height - 50 - (y1 / maxY) * (height - 100));
        int px2 = (int) (50 + (x2 / maxX) * (width - 100));
        int py2 = (int) (height - 50 - (y2 / maxY) * (height - 100));

        g.drawLine(px1, py1, px2, py2);

        g.dispose();

        String fullPath = path + "/" + fileName;
        ImageIO.write(image, "png", new File(fullPath));

        System.out.println("Imagine salvată la: " + fullPath);

        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            Runtime.getRuntime().exec("xdg-open " + fullPath);
        } else {
            Runtime.getRuntime().exec("cmd /c start " + fullPath);
        }
    }
}