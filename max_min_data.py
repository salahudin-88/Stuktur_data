import heapq
import pandas as pd

class HeapProgram:
    def __init__(self):
        self.min_heap = []          # elemen (id, nama)
        self.max_heap = []          # elemen (-id, nama)
        self.data_map = {}          # id -> (id, nama)

    def baca_excel(self, path):
        """Membaca data awal dari file Excel dengan path yang diberikan"""
        try:
            df = pd.read_excel(path)
            # asumsi kolom: ID (angka), Nama (string)
            for _, row in df.iterrows():
                id_data = int(row.iloc[0])
                nama = str(row.iloc[1])
                self.tambah_data(id_data, nama)
            print(f"Data awal dari file '{path}' berhasil dimuat.")
        except Exception as e:
            print(f"Error membaca file Excel: {e}")
            print("Pastikan file Excel memiliki kolom ID (angka) dan Nama (teks).")

    def export_to_excel(self, path):
        """Menyimpan seluruh data (dari data_map) ke file Excel"""
        if not self.data_map:
            print("Tidak ada data untuk diekspor.")
            return
        # Buat list of dictionaries atau list of list
        data_list = [{"ID": id, "Nama": nama} for id, (_, nama) in self.data_map.items()]
        df = pd.DataFrame(data_list)
        # Urutkan berdasarkan ID agar rapi
        df = df.sort_values(by="ID")
        try:
            df.to_excel(path, index=False)
            print(f"Data berhasil diekspor ke '{path}'")
        except Exception as e:
            print(f"Gagal mengekspor ke Excel: {e}")

    def tambah_data(self, id_data, nama):
        if id_data in self.data_map:
            print("ID sudah ada, tidak ditambahkan.")
            return
        data = (id_data, nama)
        heapq.heappush(self.min_heap, data)
        heapq.heappush(self.max_heap, (-id_data, nama))
        self.data_map[id_data] = data
        print("Data berhasil ditambahkan.")

    def tampil_ascending(self):
        if not self.min_heap:
            print("Min-Heap kosong.")
            return
        temp = self.min_heap.copy()
        print("Data urut ascending (berdasarkan ID):")
        while temp:
            id_data, nama = heapq.heappop(temp)
            print(f"{id_data} - {nama}")

    def tampil_descending(self):
        if not self.max_heap:
            print("Max-Heap kosong.")
            return
        temp = self.max_heap.copy()
        print("Data urut descending (berdasarkan ID):")
        while temp:
            neg_id, nama = heapq.heappop(temp)
            print(f"{-neg_id} - {nama}")

    def hapus_dari_min_heap(self, id_data):
        if id_data not in self.data_map:
            print(f"Data dengan ID {id_data} tidak ditemukan.")
            return
        # Filter ulang min_heap
        self.min_heap = [d for d in self.min_heap if d[0] != id_data]
        heapq.heapify(self.min_heap)
        self._hapus_total(id_data)
        print("Data berhasil dihapus dari Min-Heap (dan otomatis dari Max-Heap).")

    def hapus_dari_max_heap(self, id_data):
        if id_data not in self.data_map:
            print(f"Data dengan ID {id_data} tidak ditemukan.")
            return
        self._hapus_total(id_data)
        print("Data berhasil dihapus dari Max-Heap (dan otomatis dari Min-Heap).")

    def _hapus_total(self, id_data):
        """Menghapus data dari kedua heap dan map"""
        # Hapus dari min_heap
        self.min_heap = [d for d in self.min_heap if d[0] != id_data]
        heapq.heapify(self.min_heap)
        # Hapus dari max_heap
        self.max_heap = [d for d in self.max_heap if d[0] != -id_data]
        heapq.heapify(self.max_heap)
        # Hapus dari map
        del self.data_map[id_data]

def main():
    prog = HeapProgram()
    
    # Input path file Excel awal
    file_path = input("Masukkan path file Excel untuk membaca data awal (kosongkan jika tidak ada): ").strip()
    if file_path:
        prog.baca_excel(file_path)
    else:
        print("Memulai dengan heap kosong.")
    
    while True:
        print("\n=== MENU ===")
        print("1. Tambah data (id, nama)")
        print("2. Tampilkan ascending (Min-Heap)")
        print("3. Tampilkan descending (Max-Heap)")
        print("4. Hapus data dari Min-Heap (dan Max-Heap)")
        print("5. Hapus data dari Max-Heap (dan Min-Heap)")
        print("6. Ekspor data ke file Excel")
        print("7. Keluar")
        pilihan = input("Pilihan: ").strip()
        
        if pilihan == "1":
            try:
                id_data = int(input("ID: "))
                nama = input("Nama: ")
                prog.tambah_data(id_data, nama)
            except ValueError:
                print("ID harus angka!")
        elif pilihan == "2":
            prog.tampil_ascending()
        elif pilihan == "3":
            prog.tampil_descending()
        elif pilihan in ("4", "5"):
            try:
                id_data = int(input("ID data yang akan dihapus: "))
                if pilihan == "4":
                    prog.hapus_dari_min_heap(id_data)
                else:
                    prog.hapus_dari_max_heap(id_data)
            except ValueError:
                print("ID harus angka!")
        elif pilihan == "6":
            output_path = input("Masukkan path file Excel tujuan (contoh: output.xlsx): ").strip()
            if output_path:
                prog.export_to_excel(output_path)
            else:
                print("Path tidak boleh kosong.")
        elif pilihan == "7":
            print("Keluar...")
            break
        else:
            print("Pilihan tidak valid!")

if __name__ == "__main__":
    main()