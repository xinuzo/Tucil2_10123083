import javax.imageio.ImageIO;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class ImageCompression {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Masukkan alamat gambar: ");
            String imagePath = scanner.nextLine().trim();
            File imageFile = new File(imagePath);
            if (!imageFile.exists() || !imageFile.isFile()) {
                System.err.println("Error: Alamat gambar tidak valid atau file tidak ditemukan.");
                return;
            }
            
            // Coba membaca gambar
            BufferedImage image = ImageIO.read(imageFile);
            if (image == null) {
                System.err.println("Error: File yang diberikan bukan file gambar yang dapat dikenali.");
                return;
            }
            
            System.out.print("Pilih kalkulator galat (1-4): ");
            int method;
            try {
                method = Integer.parseInt(scanner.nextLine().trim());
                if (method < 1 || method > 4) {
                    System.err.println("Error: Pilihan kalkulator galat harus antara 1 dan 4.");
                    return;
                }
            } catch (NumberFormatException ex) {
                System.err.println("Error: Input kalkulator galat harus berupa angka.");
                return;
            }
            
            System.out.print("Masukkan ambang batas: ");
            double threshold;
            try {
                threshold = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.err.println("Error: Ambang batas harus berupa angka.");
                return;
            }
            
            System.out.print("Masukkan ukuran blok minimum: ");
            int minBlockSize;
            try {
                minBlockSize = Integer.parseInt(scanner.nextLine().trim());
                if (minBlockSize <= 0) {
                    System.err.println("Error: Ukuran blok minimum harus lebih besar dari 0.");
                    return;
                }
            } catch (NumberFormatException ex) {
                System.err.println("Error: Ukuran blok minimum harus berupa angka.");
                return;
            }
            
            System.out.print("Masukkan alamat output gambar (.jpg): ");
            String outputPath = scanner.nextLine().trim();
            if (outputPath.isEmpty()) {
                System.err.println("Error: Alamat output gambar tidak boleh kosong.");
                return;
            }
            outputPath = replaceExtension(outputPath, "jpg");
            
            System.out.print("Masukkan alamat output GIF (opsional): ");
            String gifOutputPath = scanner.nextLine().trim();
            if (!gifOutputPath.isEmpty()) {
                gifOutputPath = replaceExtension(gifOutputPath, "gif");
            }
            
            // Memilih kalkulator galat
            ErrorCalculator errorCalculator;
            switch (method) {
                case 1: 
                    errorCalculator = new VarianceCalculator();
                    break;
                case 2: 
                    errorCalculator = new MADCalculator();
                    break;
                case 3: 
                    errorCalculator = new MaxDiffCalculator();
                    break;
                case 4: 
                    errorCalculator = new EntropyCalculator();
                    break;
                default: 
                    System.err.println("Error: Input kalkulator galat tidak valid.");
                    return;
            }
            
            // Proses kompresi
            long startTime = System.currentTimeMillis();
            QuadTree quadTree = new QuadTree(image, errorCalculator, threshold, minBlockSize);
            BufferedImage compressedImage = quadTree.createCompressedImage();
            long endTime = System.currentTimeMillis();
            
            // Menyimpan gambar terkompresi
            if (!ImageIO.write(compressedImage, "jpg", new File(outputPath))) {
                System.err.println("Error: Gagal menulis gambar ke " + outputPath);
                return;
            }
            
            // Mengambil ukuran file asli dan terkompresi
            long originalSize = imageFile.length();
            long compressedSize = new File(outputPath).length();
            
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
            System.out.println("Original size: " + originalSize + " bytes");
            System.out.println("Compressed size: " + compressedSize + " bytes");
            System.out.printf("Compression percentage: %.2f%%\n", (1.0 - (double) compressedSize / originalSize) * 100);
            System.out.println("Tree depth: " + quadTree.getMaxDepth());
            System.out.println("Node count: " + quadTree.getNodeCount());
            
            // Proses pembuatan GIF jika alamat output GIF tidak kosong
            if (!gifOutputPath.isEmpty()) {
                List<BufferedImage> frames = new ArrayList<>();
                for (int depth = 0; depth <= quadTree.getMaxDepth(); depth++) {
                    frames.add(quadTree.createCompressedImage(depth));
                }
                // Tambahkan beberapa frame terakhir untuk jeda
                BufferedImage finalFrame = quadTree.createCompressedImage(quadTree.getMaxDepth());
                for (int i = 0; i < 4; i++) {
                    frames.add(finalFrame);
                }
                
                try (ImageOutputStream output = ImageIO.createImageOutputStream(new File(gifOutputPath))) {
                    GifSequenceWriter writer = new GifSequenceWriter(output, BufferedImage.TYPE_INT_RGB, 500, true);
                    for (BufferedImage frame : frames) {
                        writer.writeFrame(frame);
                    }
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Error saat menulis GIF: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Terjadi kesalahan I/O: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    private static String replaceExtension(String path, String newExt) {
        if (path == null || path.isEmpty()) return path;
        int lastDot = path.lastIndexOf('.');
        if (lastDot != -1) {
            path = path.substring(0, lastDot);
        }
        return path + "." + newExt;
    }
}
