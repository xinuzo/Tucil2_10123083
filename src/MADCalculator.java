import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

class MADCalculator implements ErrorCalculator {
    @Override
    public double calculateDetail(int[] rHist, int[] gHist, int[] bHist, int totalPixels) {
        double rMad = calculateMAD(rHist, totalPixels);
        double gMad = calculateMAD(gHist, totalPixels);
        double bMad = calculateMAD(bHist, totalPixels);
        return 0.2989 * rMad + 0.5870 * gMad + 0.1140 * bMad;
    }

    private double calculateMAD(int[] hist, int total) {
        if (total == 0) return 0.0;
        double sum = 0.0;
        for (int i = 0; i < hist.length; i++)
            sum += i * hist[i];
        double mean = sum / total;
        double mad = 0.0;
        for (int i = 0; i < hist.length; i++)
            mad += Math.abs(i - mean) * hist[i];
        return mad / total;
    }
}
