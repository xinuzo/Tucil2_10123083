import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class ImageCompression {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan alamat gambar: ");
        String imagePath = scanner.nextLine().trim();
        System.out.print("Pilih kalkulator galat (1-4): ");
        int method = scanner.nextInt();
        System.out.print("Masukkan ambang batas: ");
        double threshold = scanner.nextDouble();
        System.out.print("Masukkan ukuran blok minimum: ");
        int minBlockSize = scanner.nextInt();
        System.out.print("Masukkan target presentase galat (0 untuk dinonaktifkan): ");
        double targetCompression = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Masukkan alamat output gambar (.jpg): ");
        String outputPath = scanner.nextLine().trim();
        System.out.print("Masukkan alamat output GIF (opsional): ");
        String gifOutputPath = scanner.nextLine().trim();
        scanner.close();
         // Process output paths
         outputPath = replaceExtension(outputPath, "jpg");
         if (!gifOutputPath.isEmpty()) {
            gifOutputPath = replaceExtension(gifOutputPath, "gif");
        }
        BufferedImage image = ImageIO.read(new File(imagePath));
        ErrorCalculator errorCalculator;
        switch (method) {
            case 1: errorCalculator = new VarianceCalculator(); break;
            case 2: errorCalculator = new MADCalculator(); break;
            case 3: errorCalculator = new MaxDiffCalculator(); break;
            case 4: errorCalculator = new EntropyCalculator(); break;
            default: throw new IllegalArgumentException("Input tidak valid");
        }

        long startTime = System.currentTimeMillis();
        QuadTree quadTree = new QuadTree(image, errorCalculator, threshold, minBlockSize);
        BufferedImage compressedImage = quadTree.createCompressedImage();
        long endTime = System.currentTimeMillis();

        File originalFile = new File(imagePath);
        long originalSize = originalFile.length();
        ImageIO.write(compressedImage, "jpg", new File(outputPath));
        File compressedFile = new File(outputPath);
        long compressedSize = compressedFile.length();

        System.out.println("Execution time: " + (endTime - startTime) + " ms");
        System.out.println("Original size: " + originalSize + " bytes");
        System.out.println("Compressed size: " + compressedSize + " bytes");
        System.out.printf("Compression percentage: %.2f%%\n", (1.0 - (double) compressedSize / originalSize) * 100);
        System.out.println("Tree depth: " + quadTree.getMaxDepth());
        System.out.println("Node count: " + quadTree.getNodeCount());

        if (!gifOutputPath.isEmpty()) {
            List<BufferedImage> frames = new ArrayList<>();
            for (int depth = 0; depth <= quadTree.getMaxDepth(); depth++) {
                frames.add(quadTree.createCompressedImage(depth));
            }
            for (int i = 0; i < 4; i++) {
                frames.add(quadTree.createCompressedImage(quadTree.getMaxDepth()));
            }

            try (ImageOutputStream output = ImageIO.createImageOutputStream(new File(gifOutputPath))) {
                GifSequenceWriter writer = new GifSequenceWriter(output, BufferedImage.TYPE_INT_RGB, 500, true);
                for (BufferedImage frame : frames) {
                    writer.writeFrame(frame);
                }
                writer.close();
            }
        }
    }
    private static String replaceExtension(String path, String newExt) {
        if (path == null || path.isEmpty()) return path;
        int lastDot = path.lastIndexOf('.');
        if (lastDot != -1) path = path.substring(0, lastDot);
        return path + "." + newExt;
    }
}