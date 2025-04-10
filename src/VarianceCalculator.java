import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

class VarianceCalculator implements ErrorCalculator {
    @Override
    public double calculateDetail(int[] rHist, int[] gHist, int[] bHist, int totalPixels) {
        double rVar = calculateVariance(rHist, totalPixels);
        double gVar = calculateVariance(gHist, totalPixels);
        double bVar = calculateVariance(bHist, totalPixels);
        return 0.2989 * rVar + 0.5870 * gVar + 0.1140 * bVar;
    }

    private double calculateVariance(int[] hist, int total) {
        if (total == 0) return 0.0;
        double sum = 0.0, sumSquares = 0.0;
        for (int i = 0; i < hist.length; i++) {
            sum += i * hist[i];
            sumSquares += (i * i) * hist[i];
        }
        double mean = sum / total;
        return (sumSquares / total) - (mean * mean);
    }
}