import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

class Quadrant {
    private final Rectangle bbox;
    private final int depth;
    private Quadrant[] children;
    private boolean isLeaf;
    private final Color averageColor;
    private final double detail;

    public Quadrant(BufferedImage image, Rectangle bbox, int depth, ErrorCalculator errorCalculator, double threshold, int minBlockSize, int maxDepth) {
        this.bbox = bbox;
        this.depth = depth;

        int[] rHist = new int[256];
        int[] gHist = new int[256];
        int[] bHist = new int[256];
        int rSum = 0, gSum = 0, bSum = 0, totalPixels = 0;

        for (int y = bbox.y; y < bbox.y + bbox.height; y++) {
            for (int x = bbox.x; x < bbox.x + bbox.width; x++) {
                int pixel = image.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                rHist[r]++;
                gHist[g]++;
                bHist[b]++;
                rSum += r;
                gSum += g;
                bSum += b;
                totalPixels++;
            }
        }

        this.averageColor = new Color(rSum / totalPixels, gSum / totalPixels, bSum / totalPixels);
        this.detail = errorCalculator.calculateDetail(rHist, gHist, bHist, totalPixels);

        boolean canSplit = (bbox.width > minBlockSize && bbox.height > minBlockSize) && (depth < maxDepth);
        if (detail <= threshold || !canSplit) {
            this.isLeaf = true;
            this.children = null;
        } else {
            this.isLeaf = false;
            split(image, errorCalculator, threshold, minBlockSize, maxDepth);
        }
    }

    private void split(BufferedImage image, ErrorCalculator errorCalculator, double threshold, int minBlockSize, int maxDepth) {
        int halfWidth = bbox.width / 2;
        int halfHeight = bbox.height / 2;

        children = new Quadrant[]{
            new Quadrant(image, new Rectangle(bbox.x, bbox.y, halfWidth, halfHeight), depth + 1, errorCalculator, threshold, minBlockSize, maxDepth),
            new Quadrant(image, new Rectangle(bbox.x + halfWidth, bbox.y, bbox.width - halfWidth, halfHeight), depth + 1, errorCalculator, threshold, minBlockSize, maxDepth),
            new Quadrant(image, new Rectangle(bbox.x, bbox.y + halfHeight, halfWidth, bbox.height - halfHeight), depth + 1, errorCalculator, threshold, minBlockSize, maxDepth),
            new Quadrant(image, new Rectangle(bbox.x + halfWidth, bbox.y + halfHeight, bbox.width - halfWidth, bbox.height - halfHeight), depth + 1, errorCalculator, threshold, minBlockSize, maxDepth)
        };
    }

    public boolean isLeaf() { return isLeaf; }
    public Quadrant[] getChildren() { return children; }
    public Rectangle getBbox() { return bbox; }
    public Color getAverageColor() { return averageColor; }
    public int getDepth() { return depth; }
}
