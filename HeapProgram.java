import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class Data {
    int id;
    String nama;

    Data(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    @Override
    public String toString() {
        return id + " - " + nama;
    }
}

public class HeapProgram {
    private static PriorityQueue<Data> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a.id));
    private static PriorityQueue<Data> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b.id, a.id));
    private static Map<Integer, Data> dataMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input path file Excel awal
        System.out.print("Masukkan path file Excel untuk membaca data awal (kosongkan jika tidak ada): ");
        String filePath = scanner.nextLine().trim();
        if (!filePath.isEmpty()) {
            bacaExcel(filePath);
        } else {
            System.out.println("Memulai dengan heap kosong.");
        }

        int pilihan;
        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Tambah data (id, nama)");
            System.out.println("2. Tampilkan ascending (Min-Heap)");
            System.out.println("3. Tampilkan descending (Max-Heap)");
            System.out.println("4. Hapus data dari Min-Heap (dan Max-Heap)");
            System.out.println("5. Hapus data dari Max-Heap (dan Min-Heap)");
            System.out.println("6. Ekspor data ke file Excel");
            System.out.println("7. Keluar");
            System.out.print("Pilihan: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    System.out.print("ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nama: ");
                    String nama = scanner.nextLine();
                    tambahData(id, nama);
                    break;
                case 2:
                    tampilAscending();
                    break;
                case 3:
                    tampilDescending();
                    break;
                case 4:
                    System.out.print("ID data yang akan dihapus: ");
                    int idHapus = scanner.nextInt();
                    hapusData(idHapus);  // sama untuk min dan max
                    break;
                case 5:
                    System.out.print("ID data yang akan dihapus: ");
                    idHapus = scanner.nextInt();
                    hapusData(idHapus);
                    break;
                case 6:
                    System.out.print("Masukkan path file Excel tujuan (contoh: output.xlsx): ");
                    String outputPath = scanner.nextLine().trim();
                    if (!outputPath.isEmpty()) {
                        exportToExcel(outputPath);
                    } else {
                        System.out.println("Path tidak boleh kosong.");
                    }
                    break;
                case 7:
                    System.out.println("Keluar...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (pilihan != 7);
        scanner.close();
    }

    private static void bacaExcel(String path) {
        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) rowIterator.next(); // lewati header jika ada
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int id = (int) row.getCell(0).getNumericCellValue();
                String nama = row.getCell(1).getStringCellValue();
                tambahData(id, nama);
            }
            System.out.println("Data awal dari file '" + path + "' berhasil dimuat.");
        } catch (Exception e) {
            System.out.println("Error membaca file Excel: " + e.getMessage());
            System.out.println("Pastikan file Excel memiliki kolom ID (angka) dan Nama (teks).");
        }
    }

    private static void exportToExcel(String path) {
        if (dataMap.isEmpty()) {
            System.out.println("Tidak ada data untuk diekspor.");
            return;
        }
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            // Header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nama");
            // Data
            List<Data> dataList = new ArrayList<>(dataMap.values());
            dataList.sort(Comparator.comparingInt(d -> d.id)); // urutkan berdasarkan ID
            int rowNum = 1;
            for (Data data : dataList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(data.id);
                row.createCell(1).setCellValue(data.nama);
            }
            // Auto-size kolom
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            // Tulis ke file
            try (FileOutputStream fos = new FileOutputStream(path)) {
                workbook.write(fos);
            }
            System.out.println("Data berhasil diekspor ke '" + path + "'");
        } catch (Exception e) {
            System.out.println("Gagal mengekspor ke Excel: " + e.getMessage());
        }
    }

    private static void tambahData(int id, String nama) {
        if (dataMap.containsKey(id)) {
            System.out.println("ID sudah ada, tidak ditambahkan.");
            return;
        }
        Data data = new Data(id, nama);
        minHeap.offer(data);
        maxHeap.offer(data);
        dataMap.put(id, data);
        System.out.println("Data berhasil ditambahkan.");
    }

    private static void tampilAscending() {
        if (minHeap.isEmpty()) {
            System.out.println("Min-Heap kosong.");
            return;
        }
        PriorityQueue<Data> temp = new PriorityQueue<>(minHeap);
        System.out.println("Data urut ascending (berdasarkan ID):");
        while (!temp.isEmpty()) {
            System.out.println(temp.poll());
        }
    }

    private static void tampilDescending() {
        if (maxHeap.isEmpty()) {
            System.out.println("Max-Heap kosong.");
            return;
        }
        PriorityQueue<Data> temp = new PriorityQueue<>(maxHeap);
        System.out.println("Data urut descending (berdasarkan ID):");
        while (!temp.isEmpty()) {
            System.out.println(temp.poll());
        }
    }

    private static void hapusData(int id) {
        Data data = dataMap.get(id);
        if (data == null) {
            System.out.println("Data dengan ID " + id + " tidak ditemukan.");
            return;
        }
        boolean removedMin = minHeap.remove(data);
        boolean removedMax = maxHeap.remove(data);
        if (removedMin || removedMax) {
            dataMap.remove(id);
            System.out.println("Data berhasil dihapus dari kedua heap.");
        } else {
            System.out.println("Gagal menghapus data.");
        }
    }
}