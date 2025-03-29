#include "quadtree.hpp"
#include <iostream>

// Implementasi Konstruktor QuadTreeNode
QuadTreeNode::QuadTreeNode(int x, int y, int w, int h) 
    : x(x), y(y), width(w), height(h) {}

// Implementasi ErrorCalculator
double ErrorCalculator::calculateError(const cv::Mat& block, Method method) {
    switch (method) {
        case VARIANCE: return calculateVariance(block);
        case MAD: return calculateMAD(block);
        case MAX_DIFF: return calculateMaxDiff(block);
        case ENTROPY: return calculateEntropy(block);
        default: return 0.0;
    }
}

// Implementasi Fungsi compressBlock dan drawQuadtree
void compressBlock(QuadTreeNode& node, const cv::Mat& image, double threshold, int minSize, ErrorCalculator::Method method) {
    // ... (sama seperti sebelumnya)
}

void drawQuadtree(cv::Mat& output, QuadTreeNode& node) {
    if (node.children.empty()) {
        cv::rectangle(output, cv::Rect(node.x, node.y, node.width, node.height), node.color, cv::FILLED);
    } else {
        for (auto& child : node.children) {
            drawQuadtree(output, child);
        }
    }
}

// Fungsi main()
int main(int argc, char** argv) {
    // Handle input dari command-line
    if (argc < 6) {
        std::cerr << "Usage: ./compressor input.png output.png [method] [threshold] [min_size]\n";
        return 1;
    }

    // Baca gambar
    cv::Mat image = cv::imread(argv[1]);
    QuadTreeNode root(0, 0, image.cols, image.rows);

    // Parse parameter
    ErrorCalculator::Method method;
    std::string methodStr = argv[3];
    if (methodStr == "variance") method = ErrorCalculator::VARIANCE;
    // ... (handle metode lainnya)

    double threshold = std::stod(argv[4]);
    int minSize = std::stoi(argv[5]);

    // Proses kompresi
    compressBlock(root, image, threshold, minSize, method);

    // Simpan hasil
    cv::Mat output(image.size(), image.type(), cv::Scalar(0, 0, 0));
    drawQuadtree(output, root);
    cv::imwrite(argv[2], output);

    return 0;
}