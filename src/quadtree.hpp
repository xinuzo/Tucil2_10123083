#ifndef QUADTREE_HPP
#define QUADTREE_HPP

#include <opencv2/opencv.hpp>
#include <vector>

// Struktur Node Quadtree
struct QuadTreeNode {
    int x, y, width, height;
    cv::Vec3b color; 
    std::vector<QuadTreeNode> children;

    QuadTreeNode(int x, int y, int w, int h);
};

// Kelas untuk Menghitung Error
class ErrorCalculator {
public:
    enum Method { VARIANCE, MAD, MAX_DIFF, ENTROPY };

    static double calculateError(const cv::Mat& block, Method method);
    
private:
    static double calculateVariance(const cv::Mat& block);
    static double calculateMAD(const cv::Mat& block);
    static double calculateMaxDiff(const cv::Mat& block);
    static double calculateEntropy(const cv::Mat& block);
};

// Deklarasi Fungsi Utama
void compressBlock(QuadTreeNode& node, const cv::Mat& image, double threshold, int minSize, ErrorCalculator::Method method);
void drawQuadtree(cv::Mat& output, QuadTreeNode& node);

#endif