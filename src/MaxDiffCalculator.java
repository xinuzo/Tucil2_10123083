import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

class MaxDiffCalculator implements ErrorCalculator {
    @Override
    public double calculateDetail(int[] rHist, int[] gHist, int[] bHist, int totalPixels) {
        int rMaxDiff = calculateMaxDiff(rHist);
        int gMaxDiff = calculateMaxDiff(gHist);
        int bMaxDiff = calculateMaxDiff(bHist);
        return Math.max(rMaxDiff, Math.max(gMaxDiff, bMaxDiff));
    }

    private int calculateMaxDiff(int[] hist) {
        int min = 255, max = 0;
        for (int i = 0; i < hist.length; i++) {
            if (hist[i] > 0) {
                if (i < min) min = i;
                if (i > max) max = i;
            }
        }
        return max - min;
    }
}
