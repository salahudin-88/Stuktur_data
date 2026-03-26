package teksBerjalan;
import java.util.Scanner;

class Node {
    String berita;
    Node next;
    Node prev;
    boolean isCurrent; // Menandai berita yang sedang aktif
    
    public Node(String berita) {
        this.berita = berita;
        this.next = null;
        this.prev = null;
        this.isCurrent = false;
    }
}

class CircularDoublyLinkedList {
    private Node head;
    private Node tail;
    private Node current; // Node yang sedang aktif
    private int size;
    private volatile boolean running; // Untuk kontrol looping
    
    public CircularDoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.current = null;
        this.size = 0;
        this.running = true;
    }
    
    // Method untuk menambah berita di akhir
    public void tambahBerita(String berita) {
        Node newNode = new Node(berita);
        
        System.out.println("\n🔧 PROSES INSERT BERTA:");
        System.out.println("   Membuat node baru dengan data: \"" + berita + "\"");
        
        if (head == null) {
            System.out.println("   Linked List masih kosong");
            System.out.println("   Node baru menjadi HEAD dan TAIL");
            head = newNode;
            tail = newNode;
            newNode.next = head;
            newNode.prev = head;
            System.out.println("   next → menunjuk ke dirinya sendiri (circular)");
            System.out.println("   prev → menunjuk ke dirinya sendiri (circular)");
            // Berita pertama otomatis menjadi current
            newNode.isCurrent = true;
            current = newNode;
            System.out.println("   Node ini menjadi CURRENT (berita pertama)");
        } else {
            System.out.println("   Menambahkan node di akhir (sebelum TAIL)");
            System.out.println("   TAIL saat ini: \"" + tail.berita + "\"");
            
            tail.next = newNode;
            newNode.prev = tail;
            System.out.println("   prev node baru ← menunjuk ke TAIL lama");
            
            newNode.next = head;
            head.prev = newNode;
            System.out.println("   next node baru → menunjuk ke HEAD (circular)");
            
            tail = newNode;
            System.out.println("   Node baru menjadi TAIL yang baru");
            System.out.println("   HEAD.prev sekarang menunjuk ke TAIL yang baru");
        }
        size++;
        
        System.out.println(" Berita berhasil ditambahkan!");
        tampilVisualLinkedList();
        tampilStatusCurrent();
    }
    
    // Method untuk menampilkan visualisasi Circular Doubly Linked List
    public void tampilVisualLinkedList() {
        if (head == null) {
            System.out.println("\n VISUALISASI CIRCULAR DOUBLY LINKED LIST:");
            System.out.println("   [ KOSONG ]");
            return;
        }
        
        System.out.println("\n VISUALISASI CIRCULAR DOUBLY LINKED LIST:");
        System.out.println("   (prev ← [node] → next)");
        System.out.println();
        
        // Tampilkan HEAD
        System.out.println("   HEAD → " + head.berita);
        
        // Tampilkan hubungan prev dan next
        Node temp = head;
        int nomor = 1;
        
        do {
            System.out.println("\n   ┌─────────────────────────────────┐");
            System.out.println("   │         NODE " + nomor + "                  │");
            if (temp.isCurrent) {
                System.out.println("   │         [CURRENT]             │");
            }
            System.out.println("   ├─────────────────────────────────┤");
            System.out.println("   │ prev: \"" + temp.prev.berita + "\"");
            System.out.println("   │ data: \"" + temp.berita + "\"");
            System.out.println("   │ next: \"" + temp.next.berita + "\"");
            System.out.println("   └─────────────────────────────────┘");
            
            if (temp.next != head) {
                System.out.println("              ↓ next");
                System.out.println("              ↑ prev");
            }
            
            temp = temp.next;
            nomor++;
        } while (temp != head);
        
        // Tampilkan hubungan circular
        System.out.println("\n    HUBUNGAN CIRCULAR:");
        System.out.println("   TAIL.next → HEAD: \"" + tail.next.berita + "\"");
        System.out.println("   HEAD.prev → TAIL: \"" + head.prev.berita + "\"");
        System.out.println();
        
        // Tampilkan representasi sederhana
        System.out.println("   REPRESENTASI SEDERHANA:");
        System.out.print("   ");
        temp = head;
        nomor = 1;
        do {
            if (temp.isCurrent) {
                System.out.print("[" + nomor + ":" + temp.berita + "]");
            } else {
                System.out.print(" [" + nomor + ":" + temp.berita + "] ");
            }
            System.out.print(" ⇄ ");
            temp = temp.next;
            nomor++;
        } while (temp != head);
        System.out.println(" (kembali ke awal)");
        System.out.println();
    }
    
    // Method untuk mendapatkan node berdasarkan nomor urut
    private Node getNodeByNomor(int nomor) {
        Node temp = head;
        for (int i = 1; i < nomor; i++) {
            temp = temp.next;
        }
        return temp;
    }
    
    // Method untuk menampilkan status current
    private void tampilStatusCurrent() {
        if (current != null) {
            System.out.println(" Berita CURRENT saat ini: " + current.berita);
        } else {
            System.out.println(" Tidak ada berita CURRENT");
        }
    }
    
    // Method untuk menampilkan detail berita yang akan dihapus
    public void tampilDetailBeritaUntukHapus(int nomor) {
        if (head == null) {
            System.out.println(" Tidak ada berita untuk dihapus!");
            return;
        }
        
        if (nomor < 1 || nomor > size) {
            System.out.println(" Nomor urut tidak valid!");
            return;
        }
        
        Node nodeToDelete = getNodeByNomor(nomor);
        
        System.out.println("\n🔧 PROSES HAPUS BERTA:");
        System.out.println("   Mencari node nomor " + nomor + "...");
        System.out.println("   Ditemukan node dengan data: \"" + nodeToDelete.berita + "\"");
        
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║       DETAIL BERITA YANG AKAN      ║");
        System.out.println("║           DIHAPUS                   ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Nomor urut : " + nomor + "                       ║");
        if (nodeToDelete.isCurrent) {
            System.out.println("║ Status     : CURRENT              ║");
        } else {
            System.out.println("║ Status     : -                      ║");
        }
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Isi Berita:                         ║");
        System.out.println("║ " + nodeToDelete.berita);
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ prev: \"" + nodeToDelete.prev.berita + "\"");
        System.out.println("║ next: \"" + nodeToDelete.next.berita + "\"");
        System.out.println("╚════════════════════════════════════╝");
    }
    
    // Method untuk menghapus berita berdasarkan nomor urut
    public boolean hapusBerita(int nomor, boolean konfirmasi) {
        if (head == null) {
            System.out.println(" Tidak ada berita untuk dihapus!");
            return false;
        }
        
        if (nomor < 1 || nomor > size) {
            System.out.println(" Nomor urut tidak valid!");
            return false;
        }
        
        // Jika belum dikonfirmasi, tampilkan detail dan minta konfirmasi
        if (!konfirmasi) {
            tampilDetailBeritaUntukHapus(nomor);
            return false; // Kembalikan false karena belum dihapus
        }
        
        System.out.println("\n🔧 PROSES EKSEKUSI HAPUS:");
        Node nodeToDelete = getNodeByNomor(nomor);
        String beritaYangDihapus = nodeToDelete.berita;
        boolean isCurrentDeleted = nodeToDelete.isCurrent;
        
        System.out.println("   Menghapus node dengan data: \"" + beritaYangDihapus + "\"");
        System.out.println("   Node ini memiliki:");
        System.out.println("      - prev: \"" + nodeToDelete.prev.berita + "\"");
        System.out.println("      - next: \"" + nodeToDelete.next.berita + "\"");
        
        // Jika hanya satu node
        if (size == 1) {
            System.out.println("   Ini adalah satu-satunya node dalam list");
            System.out.println("   Menghapus node dan mengosongkan list");
            head = null;
            tail = null;
            current = null;
        } else {
            System.out.println("   Memperbarui pointer:");
            System.out.println("      " + nodeToDelete.prev.berita + ".next → " + nodeToDelete.next.berita);
            System.out.println("      " + nodeToDelete.next.berita + ".prev → " + nodeToDelete.prev.berita);
            
            // Update next dan prev pointers
            nodeToDelete.prev.next = nodeToDelete.next;
            nodeToDelete.next.prev = nodeToDelete.prev;
            
            if (nodeToDelete == head) {
                System.out.println("   Node yang dihapus adalah HEAD");
                System.out.println("   HEAD baru → " + nodeToDelete.next.berita);
                head = nodeToDelete.next;
            }
            if (nodeToDelete == tail) {
                System.out.println("   Node yang dihapus adalah TAIL");
                System.out.println("   TAIL baru → " + nodeToDelete.prev.berita);
                tail = nodeToDelete.prev;
            }
            
            // Jika node yang dihapus adalah current, pindahkan current ke node berikutnya
            if (isCurrentDeleted) {
                System.out.println("   Node yang dihapus adalah CURRENT");
                current = nodeToDelete.next;
                if (current != null) {
                    current.isCurrent = true;
                    System.out.println("   CURRENT berpindah ke: \"" + current.berita + "\"");
                }
            }
        }
        
        size--;
        System.out.println("\n Berita nomor " + nomor + " BERHASIL DIHAPUS!");
        System.out.println(" Berita yang dihapus: " + beritaYangDihapus);
        
        if (size > 0) {
            tampilVisualLinkedList();
            tampilStatusCurrent();
        } else {
            System.out.println("\n VISUALISASI CIRCULAR DOUBLY LINKED LIST:");
            System.out.println("   [ KOSONG ]");
        }
        
        return true;
    }
    
    // Method untuk menampilkan berita secara CONTINUOUS FORWARD (tidak berhenti)
    public void tampilForwardContinuous() throws InterruptedException {
        if (head == null) {
            System.out.println("Tidak ada berita untuk ditampilkan!");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║   MENAMPILKAN BERITA FORWARD      ║");
        System.out.println("║        (CONTINUOUS - TEKAN ENTER   ║");
        System.out.println("║         UNTUK BERHENTI)            ║");
        System.out.println("╚════════════════════════════════════╝");
        
        System.out.println("\n PROSES TRAVERSAL FORWARD:");
        System.out.println("   Bergerak menggunakan pointer next");
        System.out.println("   Akan terus berputar (circular)");
        
        running = true;
        Node temp = head;
        int nomor = 1;
        int putaran = 1;
        
        // Thread untuk mendeteksi input user
        @SuppressWarnings({"UseSpecificCatch", "CallToPrintStackTrace"})
        Thread inputThread = new Thread(() -> {
            try {
                System.in.read();
                running = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        inputThread.start();
        
        // Loop continuous
        while (running) {
            System.out.println("\n--- Putaran ke-" + putaran + " ---");
            
            do {
                if (temp.isCurrent) {
                    System.out.print("? "); // Menandai current
                } else {
                    System.out.print("  ");
                }
                System.out.println("Berita ke-" + nomor + ": " + temp.berita);
                System.out.println("   (next → " + temp.next.berita + ")");
                Thread.sleep(3000); // Delay 3 detik
                
                temp = temp.next;
                nomor++;
                
                if (temp == head) {
                    System.out.println("\n Kembali ke awal (circular)");
                }
            } while (temp != head && running);
            
            if (running) {
                nomor = 1;
                putaran++;
                System.out.println("\n--- Memulai putaran berikutnya ---");
            }
        }
        
        System.out.println("\n  Perputaran berita dihentikan oleh user.");
        System.out.println("   Total " + (putaran-1) + " putaran selesai");
        System.out.println("══════════════ SELESAI ══════════════\n");
    }
    
    // Method untuk menampilkan berita secara CONTINUOUS BACKWARD (tidak berhenti)
    public void tampilBackwardContinuous() throws InterruptedException {
        if (head == null) {
            System.out.println(" Tidak ada berita untuk ditampilkan!");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║   MENAMPILKAN BERITA BACKWARD     ║");
        System.out.println("║        (CONTINUOUS - TEKAN ENTER   ║");
        System.out.println("║         UNTUK BERHENTI)            ║");
        System.out.println("╚════════════════════════════════════╝");
        
        System.out.println("\n PROSES TRAVERSAL BACKWARD:");
        System.out.println("   Bergerak menggunakan pointer prev");
        System.out.println("   Akan terus berputar (circular)");
        
        running = true;
        Node temp = tail;
        int nomor = size;
        int putaran = 1;
        
        // Thread untuk mendeteksi input user
        @SuppressWarnings({"UseSpecificCatch", "CallToPrintStackTrace"})
        Thread inputThread = new Thread(() -> {
            try {
                System.in.read();
                running = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        inputThread.start();
        
        // Loop continuous
        while (running) {
            System.out.println("\n--- Putaran ke-" + putaran + " ---");
            
            do {
                if (temp.isCurrent) {
                    System.out.print("? "); // Menandai current
                } else {
                    System.out.print("  ");
                }
                System.out.println("Berita ke-" + nomor + ": " + temp.berita);
                System.out.println("   (prev → " + temp.prev.berita + ")");
                Thread.sleep(3000); // Delay 3 detik
                
                temp = temp.prev;
                nomor--;
                
                if (temp == tail) {
                    System.out.println("\n↻ Kembali ke akhir (circular)");
                }
            } while (temp != tail && running);
            
            if (running) {
                nomor = size;
                putaran++;
                System.out.println("\n--- Memulai putaran berikutnya ---");
            }
        }
        
        System.out.println("\n  Perputaran berita dihentikan oleh user.");
        System.out.println("   Total " + (putaran-1) + " putaran selesai");
        System.out.println("══════════════ SELESAI ══════════════\n");
    }
    
    // Method untuk menampilkan berita tertentu
    public void tampilBeritaTertentu(int nomor) {
        if (head == null) {
            System.out.println(" Tidak ada berita!");
            return;
        }
        
        if (nomor < 1 || nomor > size) {
            System.out.println(" Nomor urut tidak valid!");
            return;
        }
        
        System.out.println("\n PROSES PENCARIAN:");
        System.out.println("   Mencari node nomor " + nomor + "...");
        
        Node nodeToShow = getNodeByNomor(nomor);
        
        System.out.println("   Ditemukan!");
        System.out.println("   Data: \"" + nodeToShow.berita + "\"");
        System.out.println("   prev: \"" + nodeToShow.prev.berita + "\"");
        System.out.println("   next: \"" + nodeToShow.next.berita + "\"");
        
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║       BERITA KE-" + nomor + "                    ║");
        System.out.println("╠════════════════════════════════════╣");
        if (nodeToShow.isCurrent) {
            System.out.println("║ [CURRENT]                           ║");
        }
        System.out.println("║ " + nodeToShow.berita);
        System.out.println("╚════════════════════════════════════╝\n");
    }
    
    // Method untuk mengubah current ke nomor tertentu
    public void setCurrent(int nomor) {
        if (head == null) {
            System.out.println(" Tidak ada berita!");
            return;
        }
        
        if (nomor < 1 || nomor > size) {
            System.out.println(" Nomor urut tidak valid!");
            return;
        }
        
        System.out.println("\n PROSES UPDATE CURRENT:");
        System.out.println("   Current lama: \"" + (current != null ? current.berita : "tidak ada") + "\"");
        
        // Hapus tanda current dari node sebelumnya
        if (current != null) {
            current.isCurrent = false;
            System.out.println("   Menghapus status CURRENT dari node \"" + current.berita + "\"");
        }
        
        // Set current ke node baru
        current = getNodeByNomor(nomor);
        current.isCurrent = true;
        
        System.out.println("   Memberikan status CURRENT ke node \"" + current.berita + "\"");
        System.out.println(" Current berpindah ke berita nomor " + nomor);
        
        tampilVisualLinkedList();
        tampilStatusCurrent();
    }
    
    // Method untuk menampilkan semua berita
    public void tampilSemuaBerita() {
        if (head == null) {
            System.out.println(" Tidak ada berita!");
            return;
        }
        
        tampilVisualLinkedList();
    }
    
    // Method untuk menampilkan penjelasan Circular Doubly Linked List
    public void tampilPenjelasanCDLL() {
        System.out.println("\n PENJELASAN CIRCULAR DOUBLY LINKED LIST:");
        System.out.println("==========================================");
        System.out.println("Circular Doubly Linked List adalah struktur data linear dimana:");
        System.out.println();
        System.out.println("1️  DOUBLY LINKED:");
        System.out.println("   • Setiap node memiliki 2 pointer: prev dan next");
        System.out.println("   • prev → menunjuk ke node sebelumnya");
        System.out.println("   • next → menunjuk ke node setelahnya");
        System.out.println("   • Memungkinkan traversali 2 arah (maju & mundur)");
        System.out.println();
        System.out.println("2️  CIRCULAR:");
        System.out.println("   • Node terakhir (TAIL) menunjuk ke node pertama (HEAD)");
        System.out.println("   • Node pertama (HEAD) juga menunjuk ke node terakhir (TAIL)");
        System.out.println("   • Tidak ada node yang menunjuk ke NULL");
        System.out.println("   • Membentuk lingkaran/cycle");
        System.out.println();
        System.out.println("3️  KEUNGGULAN:");
        System.out.println("   • Traversal maju & mundur mudah");
        System.out.println("   • Insert & delete di awal/akhir cepat (O(1))");
        System.out.println("   • Cocok untuk aplikasi yang memerlukan perputaran (seperti teks berjalan)");
        System.out.println();
        System.out.println("4️  IMPLEMENTASI DI PROGRAM INI:");
        System.out.println("   • Setiap node menyimpan teks berita");
        System.out.println("   • Ada pointer CURRENT untuk berita yang sedang 'on air'");
        System.out.println("   • Traversal forward menggunakan pointer next");
        System.out.println("   • Traversal backward menggunakan pointer prev");
        System.out.println("==========================================\n");
    }
    
    public boolean isEmpty() {
        return head == null;
    }
    
    public int getSize() {
        return size;
    }
}

public class TeksBerjalan {
    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CircularDoublyLinkedList daftarBerita = new CircularDoublyLinkedList();
        int pilihan;
        
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║    SIMULASI TEKS BERJALAN TV      ║");
        System.out.println("║    (Circular Doubly Linked List)  ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("Selamat datang di program teks berjalan!");
        System.out.println("Menandakan berita CURRENT (sedang ditampilkan)\n");
        
        // Tampilkan penjelasan CDLL di awal
        daftarBerita.tampilPenjelasanCDLL();
        
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║            MENU UTAMA              ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Insert berita (di akhir)        ║");
            System.out.println("║ 2. Hapus berita                    ║");
            System.out.println("║ 3. Tampil FORWARD CONTINUOUS       ║");
            System.out.println("║    (tekan ENTER untuk stop)        ║");
            System.out.println("║ 4. Tampil BACKWARD CONTINUOUS      ║");
            System.out.println("║    (tekan ENTER untuk stop)        ║");
            System.out.println("║ 5. Tampil berita tertentu          ║");
            System.out.println("║ 6. Tampilkan semua berita          ║");
            System.out.println("║ 7. Set CURRENT (nomor urut)        ║");
            System.out.println("║ 8. Lihat Penjelasan CDLL           ║");
            System.out.println("║ 9. Exit                            ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Pilihan Anda: ");
            
            pilihan = scanner.nextInt();
            scanner.nextLine(); // Membersihkan buffer
            
            try {
                switch (pilihan) {
                    case 1 -> {
                        System.out.print("Masukkan teks berita: ");
                        String beritaBaru = scanner.nextLine();
                        daftarBerita.tambahBerita(beritaBaru);
                    }
                        
                    case 2 -> {
                        if (!daftarBerita.isEmpty()) {
                            daftarBerita.tampilSemuaBerita();
                            System.out.print("\nMasukkan nomor berita yang akan dihapus: ");
                            int nomorHapus = scanner.nextInt();
                            
                            // Tampilkan detail dan minta konfirmasi
                            daftarBerita.tampilDetailBeritaUntukHapus(nomorHapus);
                            
                            System.out.print("\nApakah Anda yakin ingin menghapus berita ini? (y/n): ");
                            scanner.nextLine(); // Clear buffer
                            String konfirmasi = scanner.nextLine();
                            
                            if (konfirmasi.equalsIgnoreCase("y")) {
                                daftarBerita.hapusBerita(nomorHapus, true);
                            } else {
                                System.out.println(" Penghapusan dibatalkan.");
                            }
                        } else {
                            System.out.println(" Tidak ada berita untuk dihapus!");
                        }
                    }
                        
                    case 3 -> {
                        System.out.println("\n  PERHATIAN: Akan menampilkan berita secara continuous.");
                        System.out.println("   Tekan ENTER kapan saja untuk menghentikan.");
                        System.out.print("   Tekan ENTER untuk memulai...");
                        scanner.nextLine();
                        daftarBerita.tampilForwardContinuous();
                    }
                        
                    case 4 -> {
                        System.out.println("\n  PERHATIAN: Akan menampilkan berita secara continuous.");
                        System.out.println("   Tekan ENTER kapan saja untuk menghentikan.");
                        System.out.print("   Tekan ENTER untuk memulai...");
                        scanner.nextLine();
                        daftarBerita.tampilBackwardContinuous();
                    }
                        
                    case 5 -> {
                        if (!daftarBerita.isEmpty()) {
                            daftarBerita.tampilSemuaBerita();
                            System.out.print("\nMasukkan nomor berita yang ingin ditampilkan: ");
                            int nomorTampil = scanner.nextInt();
                            daftarBerita.tampilBeritaTertentu(nomorTampil);
                        } else {
                            System.out.println(" Tidak ada berita untuk ditampilkan!");
                        }
                    }
                        
                    case 6 -> daftarBerita.tampilSemuaBerita();
                        
                    case 7 -> {
                        if (!daftarBerita.isEmpty()) {
                            daftarBerita.tampilSemuaBerita();
                            System.out.print("\nMasukkan nomor berita untuk dijadikan CURRENT: ");
                            int nomorCurrent = scanner.nextInt();
                            daftarBerita.setCurrent(nomorCurrent);
                        } else {
                            System.out.println(" Tidak ada berita!");
                        }
                    }
                        
                    case 8 -> daftarBerita.tampilPenjelasanCDLL();
                        
                    case 9 -> {
                        System.out.println("\n Terima kasih telah menggunakan program ini!");
                        System.out.println(" Program teks berjalan selesai.");
                    }
                        
                    default -> System.out.println(" Pilihan tidak valid! Silakan pilih 1-9.");
                }
            } catch (InterruptedException e) {
                System.out.println(" Terjadi gangguan pada sistem!");
            }
            
        } while (pilihan != 9);
        
        scanner.close();
    }
}
