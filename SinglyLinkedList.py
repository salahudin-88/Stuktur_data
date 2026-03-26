class Node:
    def __init__(self, nim, nama):
        self.nim = nim
        self.nama = nama
        self.next = None

class SinglyLinkedList:
    def __init__(self):
        self.head = None
        self.count = 0
    
    # 1. Sisipkan di awal
    def insert_first(self, nim, nama):
        new_node = Node(nim, nama)
        if self.head is None:
            self.head = new_node
        else:
            new_node.next = self.head
            self.head = new_node
        self.count += 1
        print("Data berhasil ditambahkan di awal!")
    
    # 2. Sisipkan pada posisi tertentu
    def insert_at_position(self, nim, nama, position):
        if position < 1 or position > self.count + 1:
            print(f"Posisi tidak valid! Posisi harus antara 1 - {self.count + 1}")
            return
        
        new_node = Node(nim, nama)
        
        if position == 1:
            new_node.next = self.head
            self.head = new_node
        else:
            current = self.head
            for i in range(1, position - 1):
                current = current.next
            new_node.next = current.next
            current.next = new_node
        
        self.count += 1
        print(f"Data berhasil ditambahkan di posisi {position}!")
    
    # 3. Sisipkan di akhir
    def insert_last(self, nim, nama):
        new_node = Node(nim, nama)
        
        if self.head is None:
            self.head = new_node
        else:
            current = self.head
            while current.next is not None:
                current = current.next
            current.next = new_node
        
        self.count += 1
        print("Data berhasil ditambahkan di akhir!")
    
    # 4. Hapus dari awal
    def delete_first(self):
        if self.head is None:
            print("LinkedList kosong!")
            return
        
        print(f"Data yang dihapus: {self.head.nim} - {self.head.nama}")
        self.head = self.head.next
        self.count -= 1
        print("Data berhasil dihapus dari awal!")
    
    # 5. Hapus posisi tertentu
    def delete_at_position(self, position):
        if self.head is None:
            print("LinkedList kosong!")
            return
        
        if position < 1 or position > self.count:
            print(f"Posisi tidak valid! Posisi harus antara 1 - {self.count}")
            return
        
        if position == 1:
            temp = self.head
            self.head = self.head.next
        else:
            current = self.head
            for i in range(1, position - 1):
                current = current.next
            temp = current.next
            current.next = temp.next
        
        print(f"Data yang dihapus: {temp.nim} - {temp.nama}")
        self.count -= 1
        print(f"Data berhasil dihapus dari posisi {position}!")
    
    # 6. Hapus dari akhir
    def delete_last(self):
        if self.head is None:
            print("LinkedList kosong!")
            return
        
        if self.head.next is None:
            temp = self.head
            self.head = None
        else:
            current = self.head
            while current.next.next is not None:
                current = current.next
            temp = current.next
            current.next = None
        
        print(f"Data yang dihapus: {temp.nim} - {temp.nama}")
        self.count -= 1
        print("Data berhasil dihapus dari akhir!")
    
    # 7. Hapus kejadian pertama berdasarkan NIM
    def delete_by_nim(self, nim):
        if self.head is None:
            print("LinkedList kosong!")
            return
        
        if self.head.nim == nim:
            print(f"Data yang dihapus: {self.head.nim} - {self.head.nama}")
            self.head = self.head.next
            self.count -= 1
            print("Data berhasil dihapus!")
            return
        
        current = self.head
        previous = None
        
        while current is not None and current.nim != nim:
            previous = current
            current = current.next
        
        if current is None:
            print(f"Data dengan NIM {nim} tidak ditemukan!")
            return
        
        print(f"Data yang dihapus: {current.nim} - {current.nama}")
        previous.next = current.next
        self.count -= 1
        print("Data berhasil dihapus!")
    
    # 8. Show data
    def display(self):
        if self.head is None:
            print("LinkedList kosong!")
            return
        
        print("\n=== Data Mahasiswa ===")
        print(f"Jumlah data: {self.count}")
        print("-" * 22)
        
        current = self.head
        position = 1
        while current is not None:
            print(f"{position}. {current.nim} - {current.nama}")
            current = current.next
            position += 1
        print("=" * 22 + "\n")
    
    def get_count(self):
        return self.count

def main():
    list = SinglyLinkedList()
    
    while True:
        print("\n=== MENU SINGLY LINKED LIST MAHASISWA ===")
        print("1. Sisipkan di awal")
        print("2. Sisipkan pada posisi tertentu")
        print("3. Sisipkan di akhir")
        print("4. Hapus dari awal")
        print("5. Hapus posisi tertentu")
        print("6. Hapus dari akhir")
        print("7. Hapus kejadian pertama (berdasarkan NIM)")
        print("8. Show data")
        print("9. Exit")
        
        try:
            choice = int(input("Pilih menu (1-9): "))
            
            if choice == 1:
                print("\n--- Sisipkan di Awal ---")
                nim = input("Masukkan NIM: ")
                nama = input("Masukkan Nama: ")
                list.insert_first(nim, nama)
            
            elif choice == 2:
                print("\n--- Sisipkan pada Posisi Tertentu ---")
                nim = input("Masukkan NIM: ")
                nama = input("Masukkan Nama: ")
                position = int(input(f"Masukkan posisi (1 - {list.get_count() + 1}): "))
                list.insert_at_position(nim, nama, position)
            
            elif choice == 3:
                print("\n--- Sisipkan di Akhir ---")
                nim = input("Masukkan NIM: ")
                nama = input("Masukkan Nama: ")
                list.insert_last(nim, nama)
            
            elif choice == 4:
                print("\n--- Hapus dari Awal ---")
                list.delete_first()
            
            elif choice == 5:
                print("\n--- Hapus Posisi Tertentu ---")
                if list.get_count() > 0:
                    position = int(input(f"Masukkan posisi yang akan dihapus (1 - {list.get_count()}): "))
                    list.delete_at_position(position)
                else:
                    print("LinkedList kosong!")
            
            elif choice == 6:
                print("\n--- Hapus dari Akhir ---")
                list.delete_last()
            
            elif choice == 7:
                print("\n--- Hapus Berdasarkan NIM ---")
                if list.get_count() > 0:
                    nim = input("Masukkan NIM yang akan dihapus: ")
                    list.delete_by_nim(nim)
                else:
                    print("LinkedList kosong!")
            
            elif choice == 8:
                list.display()
            
            elif choice == 9:
                print("Terima kasih! Program selesai.")
                break
            
            else:
                print("Pilihan tidak valid! Silakan pilih 1-9.")
        
        except ValueError:
            print("Input tidak valid! Harap masukkan angka.")

if __name__ == "__main__":
    main()