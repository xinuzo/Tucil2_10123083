# Image Compression dengan Quadtree

Image Compression dengan Quadtree adalah program kompresi gambar berbasis Java yang menggunakan struktur data Quadtree dan algoritma divide-and-conquer. Program ini membagi gambar menjadi blok-blok kecil berdasarkan keseragaman warna dan menggambar ulang blok-bloknya dengan berbagai metode perhitungan galat.

## Instalasi & Penggunaan

### Prasyarat
- Java Development Kit (JDK) 8+

### Cara Menjalankan Program
**Clone Repository**
```bash
git clone https://github.com/xinuzo/Tucil2_10123083.git
cd Tucil2_10123083/src
```
**Kompilasi Program**
```bash
javac -d Tucil2_10123083/bin *.java
```
**Jalankan Program**
```bash
cd..
cd bin
java ImageCompression
```

## Masukan
Program akan meminta input berikut secara interaktif:

| Parameter | Contoh | Keterangan |
|-----------|--------|------------|
| Alamat gambar | `C:/Users/rendy/Documents/test.jpg` | Path absolut/relatif |
| Metode kalkulator galat (1-4) | `2` | `1` Variansi<br>`2` MAD<br>`3` Max Pixel Difference<br>`4` Entropy |
| Ambang batas | `25` | Variansi/MAD: 0-100<br>MaxDiff: 0-255<br>Entropy: 0-8 |
| Ukuran blok minimum | `8` | 4, 8, 16 |
| Alamat output | `result/compressed.jpg` | Path untuk gambar terkompresi |
| Alamat output GIF | `result/process.gif` | Opsional |

## Keluaran
Program akan menghasilkan:
- Gambar terkompresi dalam format .jpg
- Execution Tiem (Waktu eksekusi)
- Original size (Ukuran asli gambar)
- Tree depth (Kedalaman pohon)
- Compression Percentage (Presentase kompresi)
- Node count (Jumlah simpul)
- File GIF (opsional) yang menampilkan proses pembagian Quadtree

---

## Author 
Rendi Adinata  
NIM: 10123083  
Kelas: K1

