# Konstanta untuk kapasitas array
KAPASITAS = 10

# Array untuk menyimpan data siswa (2 kolom: NIM dan Nama)
data_siswa = [[None, None] for _ in range(KAPASITAS)]

# Variabel untuk menghitung jumlah data yang tersimpan
count = 0

def tampilkan_menu():
    """Menampilkan menu pilihan"""
    print("\n" + "="*35)
    print("MENU MANAJEMEN SISWA")
    print("="*35)
    print("1. Sisipkan di awal")
    print("2. Sisipkan pada posisi tertentu")
    print("3. Sisipkan di akhir")
    print("4. Hapus dari awal")
    print("5. Hapus posisi tertentu")
    print("6. Hapus dari akhir")
    print("7. Hapus kejadian pertama")
    print("8. Tampilkan data")
    print("9. Exit")
    print("="*35)

def sisipkan_di_awal():
    """Menyisipkan data di awal array dengan cara ditimpa"""
    global count, data_siswa
    
    # Cek apakah array sudah penuh
    if count >= KAPASITAS:
        print("ERROR: Array sudah penuh! Tidak bisa menambah data.")
        return
    
    # Input data dari user
    nim = input("Masukkan NIM  : ")
    nama = input("Masukkan Nama : ")
    
    # Geser semua data ke kanan untuk membuat ruang di awal
    # Data ditimpa: data di index i ditimpa dengan data dari index i-1
    for i in range(count, 0, -1):
        data_siswa[i][0] = data_siswa[i-1][0]  # Timpa NIM
        data_siswa[i][1] = data_siswa[i-1][1]  # Timpa Nama
    
    # Simpan data baru di awal array
    data_siswa[0][0] = nim   # Simpan NIM
    data_siswa[0][1] = nama  # Simpan Nama
    
    # Tambah count karena data bertambah
    count += 1
    
    print("SUKSES: Data berhasil ditambahkan di awal array!")

def sisipkan_di_posisi():
    """Menyisipkan data di posisi tertentu dengan cara ditimpa"""
    global count, data_siswa
    
    # Cek apakah array sudah penuh
    if count >= KAPASITAS:
        print("ERROR: Array sudah penuh! Tidak bisa menambah data.")
        return
    
    # Input posisi dari user
    try:
        posisi = int(input(f"Masukkan posisi untuk sisipkan data (1-{count + 1}): "))
    except ValueError:
        print("ERROR: Input harus angka!")
        return
    
    # Validasi posisi
    if posisi < 1 or posisi > count + 1:
        print("ERROR: Posisi tidak valid!")
        return
    
    # Input data dari user
    nim = input("Masukkan NIM  : ")
    nama = input("Masukkan Nama : ")
    
    # Geser data dari posisi yang dipilih ke kanan
    # Data ditimpa: data di index i ditimpa dengan data dari index i-1
    for i in range(count, posisi - 1, -1):
        data_siswa[i][0] = data_siswa[i-1][0]  # Timpa NIM
        data_siswa[i][1] = data_siswa[i-1][1]  # Timpa Nama
    
    # Simpan data baru di posisi yang dipilih
    data_siswa[posisi-1][0] = nim   # Simpan NIM
    data_siswa[posisi-1][1] = nama  # Simpan Nama
    
    # Tambah count karena data bertambah
    count += 1
    
    print(f"SUKSES: Data berhasil ditambahkan di posisi {posisi}!")

def sisipkan_di_akhir():
    """Menyisipkan data di akhir array"""
    global count, data_siswa
    
    # Cek apakah array sudah penuh
    if count >= KAPASITAS:
        print("ERROR: Array sudah penuh! Tidak bisa menambah data.")
        return
    
    # Input data dari user
    nim = input("Masukkan NIM  : ")
    nama = input("Masukkan Nama : ")
    
    # Simpan data di akhir array (posisi count)
    data_siswa[count][0] = nim   # Simpan NIM
    data_siswa[count][1] = nama  # Simpan Nama
    
    # Tambah count karena data bertambah
    count += 1
    
    print("SUKSES: Data berhasil ditambahkan di akhir array!")

def hapus_dari_awal():
    """Menghapus data dari awal array dengan cara ditimpa"""
    global count, data_siswa
    
    # Cek apakah array kosong
    if count == 0:
        print("ERROR: Array kosong! Tidak ada data untuk dihapus.")
        return
    
    # Tampilkan data yang akan dihapus
    print(f"Data yang dihapus: {data_siswa[0][0]} - {data_siswa[0][1]}")
    
    # Geser semua data ke kiri untuk menutupi data yang dihapus
    # Data ditimpa: data di index i ditimpa dengan data dari index i+1
    for i in range(count - 1):
        data_siswa[i][0] = data_siswa[i+1][0]  # Timpa NIM
        data_siswa[i][1] = data_siswa[i+1][1]  # Timpa Nama
    
    # Kosongkan data terakhir
    data_siswa[count-1][0] = None  # Set NIM menjadi None
    data_siswa[count-1][1] = None  # Set Nama menjadi None
    
    # Kurangi count karena data berkurang
    count -= 1
    
    print("SUKSES: Data berhasil dihapus dari awal array!")

def hapus_posisi_tertentu():
    """Menghapus data dari posisi tertentu dengan cara ditimpa"""
    global count, data_siswa
    
    # Cek apakah array kosong
    if count == 0:
        print("ERROR: Array kosong! Tidak ada data untuk dihapus.")
        return
    
    # Input posisi dari user
    try:
        posisi = int(input(f"Masukkan posisi data yang akan dihapus (1-{count}): "))
    except ValueError:
        print("ERROR: Input harus angka!")
        return
    
    # Validasi posisi
    if posisi < 1 or posisi > count:
        print("ERROR: Posisi tidak valid!")
        return
    
    # Tampilkan data yang akan dihapus
    print(f"Data yang dihapus: {data_siswa[posisi-1][0]} - {data_siswa[posisi-1][1]}")
    
    # Geser data dari posisi yang dihapus ke kiri
    # Data ditimpa: data di index i ditimpa dengan data dari index i+1
    for i in range(posisi - 1, count - 1):
        data_siswa[i][0] = data_siswa[i+1][0]  # Timpa NIM
        data_siswa[i][1] = data_siswa[i+1][1]  # Timpa Nama
    
    # Kosongkan data terakhir
    data_siswa[count-1][0] = None  # Set NIM menjadi None
    data_siswa[count-1][1] = None  # Set Nama menjadi None
    
    # Kurangi count karena data berkurang
    count -= 1
    
    print(f"SUKSES: Data berhasil dihapus dari posisi {posisi}!")

def hapus_dari_akhir():
    """Menghapus data dari akhir array"""
    global count, data_siswa
    
    # Cek apakah array kosong
    if count == 0:
        print("ERROR: Array kosong! Tidak ada data untuk dihapus.")
        return
    
    # Tampilkan data yang akan dihapus
    print(f"Data yang dihapus: {data_siswa[count-1][0]} - {data_siswa[count-1][1]}")
    
    # Kosongkan data terakhir
    data_siswa[count-1][0] = None  # Set NIM menjadi None
    data_siswa[count-1][1] = None  # Set Nama menjadi None
    
    # Kurangi count karena data berkurang
    count -= 1
    
    print("SUKSES: Data berhasil dihapus dari akhir array!")

def hapus_kejadian_pertama():
    """Menghapus kejadian pertama berdasarkan NIM dengan cara ditimpa"""
    global count, data_siswa
    
    # Cek apakah array kosong
    if count == 0:
        print("ERROR: Array kosong! Tidak ada data untuk dihapus.")
        return
    
    # Input NIM yang akan dihapus
    nim_cari = input("Masukkan NIM data yang akan dihapus: ")
    
    # Cari posisi data dengan NIM yang dicari
    posisi = -1
    for i in range(count):
        if data_siswa[i][0] == nim_cari:
            posisi = i
            break
    
    # Cek apakah data ditemukan
    if posisi == -1:
        print(f"ERROR: Data dengan NIM {nim_cari} tidak ditemukan!")
        return
    
    # Tampilkan data yang akan dihapus
    print(f"Data yang dihapus: {data_siswa[posisi][0]} - {data_siswa[posisi][1]}")
    
    # Geser data dari posisi yang ditemukan ke kiri
    # Data ditimpa: data di index i ditimpa dengan data dari index i+1
    for i in range(posisi, count - 1):
        data_siswa[i][0] = data_siswa[i+1][0]  # Timpa NIM
        data_siswa[i][1] = data_siswa[i+1][1]  # Timpa Nama
    
    # Kosongkan data terakhir
    data_siswa[count-1][0] = None  # Set NIM menjadi None
    data_siswa[count-1][1] = None  # Set Nama menjadi None
    
    # Kurangi count karena data berkurang
    count -= 1
    
    print("SUKSES: Data berhasil dihapus!")

def tampilkan_data():
    """Menampilkan semua data dalam array"""
    global count, data_siswa
    
    # Cek apakah array kosong
    if count == 0:
        print("INFO: Array kosong. Tidak ada data untuk ditampilkan.")
        return
    
    # Tampilkan header
    print("\n" + "="*45)
    print("DATA SISWA YANG TERSIMPAN")
    print("="*45)
    print(f"{'No':<3} {'NIM':<15} {'Nama':<20}")
    print("-" * 45)
    
    # Tampilkan semua data
    for i in range(count):
        print(f"{i+1:<3} {data_siswa[i][0]:<15} {data_siswa[i][1]:<20}")
    
    # Tampilkan footer
    print("-" * 45)
    print(f"Total data: {count} dari {KAPASITAS} kapasitas")
    print("="*45 + "\n")

def main():
    """Fungsi utama program"""
    global count
    
    while True:
        # Tampilkan menu
        tampilkan_menu()
        
        # Input pilihan menu
        try:
            pilihan = int(input("Pilih menu (1-9): "))
        except ValueError:
            print("ERROR: Input harus angka 1-9!")
            continue
        
        # Proses pilihan menu
        if pilihan == 1:
            sisipkan_di_awal()
        elif pilihan == 2:
            sisipkan_di_posisi()
        elif pilihan == 3:
            sisipkan_di_akhir()
        elif pilihan == 4:
            hapus_dari_awal()
        elif pilihan == 5:
            hapus_posisi_tertentu()
        elif pilihan == 6:
            hapus_dari_akhir()
        elif pilihan == 7:
            hapus_kejadian_pertama()
        elif pilihan == 8:
            tampilkan_data()
        elif pilihan == 9:
            print("Program selesai. Terima kasih!")
            break
        else:
            print("ERROR: Pilihan tidak valid! Harap pilih 1-9.")

# Jalankan program utama
if __name__ == "__main__":
    main()