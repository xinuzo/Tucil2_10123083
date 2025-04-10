import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

class QuadTree {
    private final Quadrant root;
    private int maxDepth;
    private int nodeCount;

    public QuadTree(BufferedImage image, ErrorCalculator errorCalculator, double threshold, int minBlockSize) {
        int maxDimension = Math.max(image.getWidth(), image.getHeight());
        int calculatedMaxDepth = 0;
        while (maxDimension > minBlockSize) {
            maxDimension /= 2;
            calculatedMaxDepth++;
        }

        this.root = new Quadrant(image, new Rectangle(0, 0, image.getWidth(), image.getHeight()), 0, errorCalculator, threshold, minBlockSize, calculatedMaxDepth);
        this.maxDepth = 0;
        this.nodeCount = 0;
        buildTree(root);
    }

    private void buildTree(Quadrant node) {
        nodeCount++;
        if (node.isLeaf()) {
            if (node.getDepth() > maxDepth)
                maxDepth = node.getDepth();
            return;
        }
        for (Quadrant child : node.getChildren())
            buildTree(child);
    }

    public BufferedImage createCompressedImage() {
        return createCompressedImage(Integer.MAX_VALUE);
    }

    public BufferedImage createCompressedImage(int maxFrameDepth) {
        BufferedImage img = new BufferedImage(root.getBbox().width, root.getBbox().height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        List<Quadrant> leaves = new ArrayList<>();
        getLeaves(root, leaves, maxFrameDepth);
        for (Quadrant leaf : leaves) {
            Rectangle rect = leaf.getBbox();
            g.setColor(leaf.getAverageColor());
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
        g.dispose();
        return img;
    }

    private void getLeaves(Quadrant node, List<Quadrant> leaves, int maxFrameDepth) {
        if (node.isLeaf() || node.getDepth() == maxFrameDepth) {
            leaves.add(node);
        } else {
            for (Quadrant child : node.getChildren())
                getLeaves(child, leaves, maxFrameDepth);
        }
    }

    public int getMaxDepth() { return maxDepth; }
    public int getNodeCount() { return nodeCount; }
}