import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;


class EntropyCalculator implements ErrorCalculator {
    @Override
    public double calculateDetail(int[] rHist, int[] gHist, int[] bHist, int totalPixels) {
        double rEntropy = calculateEntropy(rHist, totalPixels);
        double gEntropy = calculateEntropy(gHist, totalPixels);
        double bEntropy = calculateEntropy(bHist, totalPixels);
        return 0.2989 * rEntropy + 0.5870 * gEntropy + 0.1140 * bEntropy;
    }

    private double calculateEntropy(int[] hist, int total) {
        if (total == 0) return 0.0;
        double entropy = 0.0;
        for (int i = 0; i < hist.length; i++) {
            if (hist[i] > 0) {
                double p = hist[i] / (double) total;
                entropy -= p * (Math.log(p) / Math.log(2));
            }
        }
        return entropy;
    }
}
