import time
import os
import threading
import sys
import msvcrt  # Untuk Windows, untuk deteksi keyboard
import select  # Untuk Linux/Mac

class Node:
    def __init__(self, berita):
        self.berita = berita
        self.next = None
        self.prev = None
        self.is_current = False  # Menandai berita yang sedang aktif

class CircularDoublyLinkedList:
    def __init__(self):
        self.head = None
        self.tail = None
        self.current = None  # Node yang sedang aktif
        self.size = 0
        self.running = True  # Untuk kontrol looping
    
    # Method untuk menambah berita di akhir
    def tambah_berita(self, berita):
        new_node = Node(berita)
        
        print("\n🔧 PROSES INSERT BERTA:")
        print(f"   Membuat node baru dengan data: \"{berita}\"")
        
        if self.head is None:
            print("   Linked List masih kosong")
            print("   Node baru menjadi HEAD dan TAIL")
            self.head = new_node
            self.tail = new_node
            new_node.next = self.head
            new_node.prev = self.head
            print("   next → menunjuk ke dirinya sendiri (circular)")
            print("   prev → menunjuk ke dirinya sendiri (circular)")
            # Berita pertama otomatis menjadi current
            new_node.is_current = True
            self.current = new_node
            print("   Node ini menjadi CURRENT (berita pertama)")
        else:
            print(f"   Menambahkan node di akhir (sebelum TAIL)")
            print(f"   TAIL saat ini: \"{self.tail.berita}\"")
            
            self.tail.next = new_node
            new_node.prev = self.tail
            print("   prev node baru ← menunjuk ke TAIL lama")
            
            new_node.next = self.head
            self.head.prev = new_node
            print("   next node baru → menunjuk ke HEAD (circular)")
            
            self.tail = new_node
            print("   Node baru menjadi TAIL yang baru")
            print("   HEAD.prev sekarang menunjuk ke TAIL yang baru")
        
        self.size += 1
        print("✅ Berita berhasil ditambahkan!")
        self.tampil_visual_linked_list()
        self.tampil_status_current()
    
    # Method untuk menampilkan visualisasi Circular Doubly Linked List
    def tampil_visual_linked_list(self):
        if self.head is None:
            print("\n📊 VISUALISASI CIRCULAR DOUBLY LINKED LIST:")
            print("   [ KOSONG ]")
            return
        
        print("\n📊 VISUALISASI CIRCULAR DOUBLY LINKED LIST:")
        print("   (prev ← [node] → next)")
        print()
        
        # Tampilkan HEAD
        print(f"   HEAD → {self.head.berita}")
        
        # Tampilkan hubungan prev dan next
        temp = self.head
        nomor = 1
        
        while True:
            print("\n   ┌─────────────────────────────────┐")
            print(f"   │         NODE {nomor}                  │")
            if temp.is_current:
                print("   │         [CURRENT] ✅             │")
            print("   ├─────────────────────────────────┤")
            print(f"   │ prev: \"{temp.prev.berita}\"")
            print(f"   │ data: \"{temp.berita}\"")
            print(f"   │ next: \"{temp.next.berita}\"")
            print("   └─────────────────────────────────┘")
            
            temp = temp.next
            if temp != self.head:
                print("              ↓ next")
                print("              ↑ prev")
            else:
                break
            nomor += 1
        
        # Tampilkan hubungan circular
        print("\n   🔄 HUBUNGAN CIRCULAR:")
        print(f"   TAIL.next → HEAD: \"{self.tail.next.berita}\"")
        print(f"   HEAD.prev → TAIL: \"{self.head.prev.berita}\"")
        print()
        
        # Tampilkan representasi sederhana
        print("   REPRESENTASI SEDERHANA:")
        print("   ", end="")
        temp = self.head
        nomor = 1
        while True:
            if temp.is_current:
                print(f"▶[{nomor}:{temp.berita}]◀", end="")
            else:
                print(f" [{nomor}:{temp.berita}] ", end="")
            print(" ⇄ ", end="")
            temp = temp.next
            nomor += 1
            if temp == self.head:
                break
        print(" (kembali ke awal)")
        print()
    
    # Method untuk mendapatkan node berdasarkan nomor urut
    def get_node_by_nomor(self, nomor):
        current = self.head
        for i in range(1, nomor):
            current = current.next
        return current
    
    # Method untuk menampilkan status current
    def tampil_status_current(self):
        if self.current:
            print(f"📌 Berita CURRENT saat ini: {self.current.berita}")
        else:
            print("📌 Tidak ada berita CURRENT")
    
    # Method untuk menampilkan detail berita yang akan dihapus
    def tampil_detail_berita_untuk_hapus(self, nomor):
        if self.head is None:
            print("❌ Tidak ada berita untuk dihapus!")
            return False
        
        if nomor < 1 or nomor > self.size:
            print("❌ Nomor urut tidak valid!")
            return False
        
        node_to_delete = self.get_node_by_nomor(nomor)
        
        print("\n🔧 PROSES HAPUS BERTA:")
        print(f"   Mencari node nomor {nomor}...")
        print(f"   Ditemukan node dengan data: \"{node_to_delete.berita}\"")
        
        print("\n╔════════════════════════════════════╗")
        print("║       DETAIL BERITA YANG AKAN      ║")
        print("║           DIHAPUS                   ║")
        print("╠════════════════════════════════════╣")
        print(f"║ Nomor urut : {nomor}                       ║")
        if node_to_delete.is_current:
            print("║ Status     : CURRENT ✅             ║")
        else:
            print("║ Status     : -                      ║")
        print("╠════════════════════════════════════╣")
        print("║ Isi Berita:                         ║")
        print(f"║ {node_to_delete.berita}")
        print("╠════════════════════════════════════╣")
        print(f"║ prev: \"{node_to_delete.prev.berita}\"")
        print(f"║ next: \"{node_to_delete.next.berita}\"")
        print("╚════════════════════════════════════╝")
        
        return True
    
    # Method untuk menghapus berita berdasarkan nomor urut
    def hapus_berita(self, nomor):
        if self.head is None:
            print("❌ Tidak ada berita untuk dihapus!")
            return False
        
        if nomor < 1 or nomor > self.size:
            print("❌ Nomor urut tidak valid!")
            return False
        
        print("\n🔧 PROSES EKSEKUSI HAPUS:")
        node_to_delete = self.get_node_by_nomor(nomor)
        berita_yang_dihapus = node_to_delete.berita
        is_current_deleted = node_to_delete.is_current
        
        print(f"   Menghapus node dengan data: \"{berita_yang_dihapus}\"")
        print("   Node ini memiliki:")
        print(f"      - prev: \"{node_to_delete.prev.berita}\"")
        print(f"      - next: \"{node_to_delete.next.berita}\"")
        
        # Jika hanya satu node
        if self.size == 1:
            print("   Ini adalah satu-satunya node dalam list")
            print("   Menghapus node dan mengosongkan list")
            self.head = None
            self.tail = None
            self.current = None
        else:
            print("   Memperbarui pointer:")
            print(f"      {node_to_delete.prev.berita}.next → {node_to_delete.next.berita}")
            print(f"      {node_to_delete.next.berita}.prev → {node_to_delete.prev.berita}")
            
            # Update next dan prev pointers
            node_to_delete.prev.next = node_to_delete.next
            node_to_delete.next.prev = node_to_delete.prev
            
            if node_to_delete == self.head:
                print(f"   Node yang dihapus adalah HEAD")
                print(f"   HEAD baru → {node_to_delete.next.berita}")
                self.head = node_to_delete.next
            if node_to_delete == self.tail:
                print(f"   Node yang dihapus adalah TAIL")
                print(f"   TAIL baru → {node_to_delete.prev.berita}")
                self.tail = node_to_delete.prev
            
            # Jika node yang dihapus adalah current, pindahkan current ke node berikutnya
            if is_current_deleted:
                print("   Node yang dihapus adalah CURRENT")
                self.current = node_to_delete.next
                if self.current:
                    self.current.is_current = True
                    print(f"   CURRENT berpindah ke: \"{self.current.berita}\"")
        
        self.size -= 1
        print("\n✅ Berita nomor", nomor, "BERHASIL DIHAPUS!")
        print(f"📝 Berita yang dihapus: {berita_yang_dihapus}")
        
        if self.size > 0:
            self.tampil_visual_linked_list()
            self.tampil_status_current()
        else:
            print("\n📊 VISUALISASI CIRCULAR DOUBLY LINKED LIST:")
            print("   [ KOSONG ]")
        
        return True
    
    # Fungsi untuk deteksi keyboard (cross-platform)
    def kbhit(self):
        if os.name == 'nt':  # Windows
            return msvcrt.kbhit()
        else:  # Linux/Mac
            dr, dw, de = select.select([sys.stdin], [], [], 0)
            return dr != []
    
    def getch(self):
        if os.name == 'nt':  # Windows
            return msvcrt.getch().decode()
        else:  # Linux/Mac
            return sys.stdin.read(1)
    
    # Method untuk menampilkan berita secara CONTINUOUS FORWARD (tidak berhenti)
    def tampil_forward_continuous(self):
        if self.head is None:
            print("❌ Tidak ada berita untuk ditampilkan!")
            return
        
        print("\n╔════════════════════════════════════╗")
        print("║   MENAMPILKAN BERITA FORWARD      ║")
        print("║        (CONTINUOUS - TEKAN 'Q'     ║")
        print("║         UNTUK BERHENTI)            ║")
        print("╚════════════════════════════════════╝")
        
        print("\n🔄 PROSES TRAVERSAL FORWARD:")
        print("   Bergerak menggunakan pointer next")
        print("   Akan terus berputar (circular)")
        
        self.running = True
        temp = self.head
        nomor = 1
        putaran = 1
        
        print("Mulai menampilkan berita... (Tekan 'Q' untuk berhenti)")
        
        try:
            while self.running:
                print(f"\n--- Putaran ke-{putaran} ---")
                
                while True:
                    if self.kbhit():
                        ch = self.getch().upper()
                        if ch == 'Q':
                            self.running = False
                            break
                    
                    if not self.running:
                        break
                    
                    if temp.is_current:
                        print("▶ ", end="")
                    else:
                        print("  ", end="")
                    print(f"Berita ke-{nomor}: {temp.berita}")
                    print(f"   (next → {temp.next.berita})")
                    
                    time.sleep(3)  # Delay 3 detik
                    
                    temp = temp.next
                    nomor += 1
                    
                    if temp == self.head:
                        print("\n↻ Kembali ke awal (circular)")
                        break
                
                if self.running:
                    nomor = 1
                    putaran += 1
                    print("\n--- Memulai putaran berikutnya ---")
                    
        except KeyboardInterrupt:
            self.running = False
        
        print(f"\n⏹️  Perputaran berita dihentikan oleh user.")
        print(f"   Total {putaran-1} putaran selesai")
        print("══════════════ SELESAI ══════════════\n")
    
    # Method untuk menampilkan berita secara CONTINUOUS BACKWARD (tidak berhenti)
    def tampil_backward_continuous(self):
        if self.head is None:
            print("❌ Tidak ada berita untuk ditampilkan!")
            return
        
        print("\n╔════════════════════════════════════╗")
        print("║   MENAMPILKAN BERITA BACKWARD     ║")
        print("║        (CONTINUOUS - TEKAN 'Q'     ║")
        print("║         UNTUK BERHENTI)            ║")
        print("╚════════════════════════════════════╝")
        
        print("\n🔄 PROSES TRAVERSAL BACKWARD:")
        print("   Bergerak menggunakan pointer prev")
        print("   Akan terus berputar (circular)")
        
        self.running = True
        temp = self.tail
        nomor = self.size
        putaran = 1
        
        print("Mulai menampilkan berita... (Tekan 'Q' untuk berhenti)")
        
        try:
            while self.running:
                print(f"\n--- Putaran ke-{putaran} ---")
                
                while True:
                    if self.kbhit():
                        ch = self.getch().upper()
                        if ch == 'Q':
                            self.running = False
                            break
                    
                    if not self.running:
                        break
                    
                    if temp.is_current:
                        print("◀ ", end="")
                    else:
                        print("  ", end="")
                    print(f"Berita ke-{nomor}: {temp.berita}")
                    print(f"   (prev → {temp.prev.berita})")
                    
                    time.sleep(3)  # Delay 3 detik
                    
                    temp = temp.prev
                    nomor -= 1
                    
                    if temp == self.tail:
                        print("\n↻ Kembali ke akhir (circular)")
                        break
                
                if self.running:
                    nomor = self.size
                    putaran += 1
                    print("\n--- Memulai putaran berikutnya ---")
                    
        except KeyboardInterrupt:
            self.running = False
        
        print(f"\n⏹️  Perputaran berita dihentikan oleh user.")
        print(f"   Total {putaran-1} putaran selesai")
        print("══════════════ SELESAI ══════════════\n")
    
    # Method untuk menampilkan berita tertentu
    def tampil_berita_tertentu(self, nomor):
        if self.head is None:
            print("❌ Tidak ada berita!")
            return
        
        if nomor < 1 or nomor > self.size:
            print("❌ Nomor urut tidak valid!")
            return
        
        print("\n🔍 PROSES PENCARIAN:")
        print(f"   Mencari node nomor {nomor}...")
        
        node_to_show = self.get_node_by_nomor(nomor)
        
        print(f"   Ditemukan!")
        print(f"   Data: \"{node_to_show.berita}\"")
        print(f"   prev: \"{node_to_show.prev.berita}\"")
        print(f"   next: \"{node_to_show.next.berita}\"")
        
        print("\n╔════════════════════════════════════╗")
        print(f"║       BERITA KE-{nomor}                    ║")
        print("╠════════════════════════════════════╣")
        if node_to_show.is_current:
            print("║ [CURRENT]                           ║")
        print(f"║ {node_to_show.berita}")
        print("╚════════════════════════════════════╝\n")
    
    # Method untuk mengubah current ke nomor tertentu
    def set_current(self, nomor):
        if self.head is None:
            print("❌ Tidak ada berita!")
            return
        
        if nomor < 1 or nomor > self.size:
            print("❌ Nomor urut tidak valid!")
            return
        
        print("\n🔄 PROSES UPDATE CURRENT:")
        print(f"   Current lama: \"{self.current.berita if self.current else 'tidak ada'}\"")
        
        # Hapus tanda current dari node sebelumnya
        if self.current:
            self.current.is_current = False
            print(f"   Menghapus status CURRENT dari node \"{self.current.berita}\"")
        
        # Set current ke node baru
        self.current = self.get_node_by_nomor(nomor)
        self.current.is_current = True
        
        print(f"   Memberikan status CURRENT ke node \"{self.current.berita}\"")
        print(f"✅ Current berpindah ke berita nomor {nomor}")
        
        self.tampil_visual_linked_list()
        self.tampil_status_current()
    
    # Method untuk menampilkan semua berita
    def tampil_semua_berita(self):
        if self.head is None:
            print("❌ Tidak ada berita!")
            return
        
        self.tampil_visual_linked_list()
    
    # Method untuk menampilkan penjelasan Circular Doubly Linked List
    def tampil_penjelasan_cdll(self):
        print("\n📚 PENJELASAN CIRCULAR DOUBLY LINKED LIST:")
        print("==========================================")
        print("Circular Doubly Linked List adalah struktur data linear dimana:")
        print()
        print("1️⃣  DOUBLY LINKED:")
        print("   • Setiap node memiliki 2 pointer: prev dan next")
        print("   • prev → menunjuk ke node sebelumnya")
        print("   • next → menunjuk ke node setelahnya")
        print("   • Memungkinkan traversal 2 arah (maju & mundur)")
        print()
        print("2️⃣  CIRCULAR:")
        print("   • Node terakhir (TAIL) menunjuk ke node pertama (HEAD)")
        print("   • Node pertama (HEAD) juga menunjuk ke node terakhir (TAIL)")
        print("   • Tidak ada node yang menunjuk ke NULL")
        print("   • Membentuk lingkaran/cycle")
        print()
        print("3️⃣  KEUNGGULAN:")
        print("   • Traversal maju & mundur mudah")
        print("   • Insert & delete di awal/akhir cepat (O(1))")
        print("   • Cocok untuk aplikasi yang memerlukan perputaran (seperti teks berjalan)")
        print()
        print("4️⃣  IMPLEMENTASI DI PROGRAM INI:")
        print("   • Setiap node menyimpan teks berita")
        print("   • Ada pointer CURRENT untuk berita yang sedang 'on air'")
        print("   • Traversal forward menggunakan pointer next")
        print("   • Traversal backward menggunakan pointer prev")
        print("==========================================\n")
    
    def is_empty(self):
        return self.head is None
    
    def get_size(self):
        return self.size

def clear_screen():
    # Untuk Windows
    if os.name == 'nt':
        os.system('cls')
    # Untuk Linux/Mac
    else:
        os.system('clear')

def main():
    daftar_berita = CircularDoublyLinkedList()
    
    print("╔════════════════════════════════════╗")
    print("║    SIMULASI TEKS BERJALAN TV      ║")
    print("║    (Circular Doubly Linked List)  ║")
    print("╚════════════════════════════════════╝")
    print("Selamat datang di program teks berjalan!")
    print("▶ Menandakan berita CURRENT (sedang ditampilkan)\n")
    
    # Tampilkan penjelasan CDLL di awal
    daftar_berita.tampil_penjelasan_cdll()
    
    while True:
        print("\n╔════════════════════════════════════╗")
        print("║            MENU UTAMA              ║")
        print("╠════════════════════════════════════╣")
        print("║ 1. Insert berita (di akhir)        ║")
        print("║ 2. Hapus berita                    ║")
        print("║ 3. Tampil FORWARD CONTINUOUS       ║")
        print("║    (tekan 'Q' untuk stop)          ║")
        print("║ 4. Tampil BACKWARD CONTINUOUS      ║")
        print("║    (tekan 'Q' untuk stop)          ║")
        print("║ 5. Tampil berita tertentu          ║")
        print("║ 6. Tampilkan semua berita          ║")
        print("║ 7. Set CURRENT (nomor urut)        ║")
        print("║ 8. Lihat Penjelasan CDLL           ║")
        print("║ 9. Exit                            ║")
        print("╚════════════════════════════════════╝")
        
        try:
            pilihan = int(input("Pilihan Anda: "))
            
            if pilihan == 1:
                berita_baru = input("Masukkan teks berita: ")
                daftar_berita.tambah_berita(berita_baru)
                
            elif pilihan == 2:
                if not daftar_berita.is_empty():
                    daftar_berita.tampil_semua_berita()
                    nomor_hapus = int(input("\nMasukkan nomor berita yang akan dihapus: "))
                    
                    # Tampilkan detail dan minta konfirmasi
                    if daftar_berita.tampil_detail_berita_untuk_hapus(nomor_hapus):
                        konfirmasi = input("\nApakah Anda yakin ingin menghapus berita ini? (y/n): ")
                        
                        if konfirmasi.lower() == 'y':
                            daftar_berita.hapus_berita(nomor_hapus)
                        else:
                            print("❌ Penghapusan dibatalkan.")
                else:
                    print("❌ Tidak ada berita untuk dihapus!")
                    
            elif pilihan == 3:
                print("\n⚠️  PERHATIAN: Akan menampilkan berita secara continuous.")
                print("   Tekan 'Q' kapan saja untuk menghentikan.")
                input("   Tekan ENTER untuk memulai...")
                daftar_berita.tampil_forward_continuous()
                
            elif pilihan == 4:
                print("\n⚠️  PERHATIAN: Akan menampilkan berita secara continuous.")
                print("   Tekan 'Q' kapan saja untuk menghentikan.")
                input("   Tekan ENTER untuk memulai...")
                daftar_berita.tampil_backward_continuous()
                
            elif pilihan == 5:
                if not daftar_berita.is_empty():
                    daftar_berita.tampil_semua_berita()
                    nomor_tampil = int(input("\nMasukkan nomor berita yang ingin ditampilkan: "))
                    daftar_berita.tampil_berita_tertentu(nomor_tampil)
                else:
                    print("❌ Tidak ada berita untuk ditampilkan!")
                    
            elif pilihan == 6:
                daftar_berita.tampil_semua_berita()
                
            elif pilihan == 7:
                if not daftar_berita.is_empty():
                    daftar_berita.tampil_semua_berita()
                    nomor_current = int(input("\nMasukkan nomor berita untuk dijadikan CURRENT: "))
                    daftar_berita.set_current(nomor_current)
                else:
                    print("❌ Tidak ada berita!")
                    
            elif pilihan == 8:
                daftar_berita.tampil_penjelasan_cdll()
                
            elif pilihan == 9:
                print("\n✨ Terima kasih telah menggunakan program ini!")
                print("✨ Program teks berjalan selesai.")
                break
                
            else:
                print("❌ Pilihan tidak valid! Silakan pilih 1-9.")
                
        except ValueError:
            print("❌ Input tidak valid! Masukkan angka.")
        except KeyboardInterrupt:
            print("\n\n⚠️ Program dihentikan oleh user.")
            break
        except Exception as e:
            print(f"❌ Terjadi kesalahan: {e}")

if __name__ == "__main__":
    main()