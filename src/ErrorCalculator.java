import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public interface ErrorCalculator {
    double calculateDetail(int[] rHist, int[] gHist, int[] bHist, int totalPixels);
}
