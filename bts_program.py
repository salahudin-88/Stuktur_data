import openpyxl  # pastikan sudah install: pip install openpyxl

class Node:
    def __init__(self, id, name):
        self.id = id
        self.name = name
        self.left = None
        self.right = None

class BST:
    def __init__(self):
        self.root = None

    def insert(self, id, name):
        self.root = self._insert_rec(self.root, id, name)

    def _insert_rec(self, node, id, name):
        if node is None:
            return Node(id, name)
        if id < node.id:
            node.left = self._insert_rec(node.left, id, name)
        elif id > node.id:
            node.right = self._insert_rec(node.right, id, name)
        else:
            print(f"ID {id} sudah ada. Data tidak ditambahkan.")
        return node

    def search(self, id):
        return self._search_rec(self.root, id)

    def _search_rec(self, node, id):
        if node is None or node.id == id:
            return node
        if id < node.id:
            return self._search_rec(node.left, id)
        return self._search_rec(node.right, id)

    def delete(self, id):
        self.root = self._delete_rec(self.root, id)

    def _delete_rec(self, node, id):
        if node is None:
            print("ID tidak ditemukan.")
            return None
        if id < node.id:
            node.left = self._delete_rec(node.left, id)
        elif id > node.id:
            node.right = self._delete_rec(node.right, id)
        else:
            if node.left is None:
                return node.right
            if node.right is None:
                return node.left
            successor = self._find_min(node.right)
            node.id = successor.id
            node.name = successor.name
            node.right = self._delete_rec(node.right, successor.id)
        return node

    def _find_min(self, node):
        while node.left:
            node = node.left
        return node

    def inorder(self):
        self._inorder_rec(self.root)
        print()

    def _inorder_rec(self, node):
        if node:
            self._inorder_rec(node.left)
            print(f"({node.id}, {node.name})", end=" ")
            self._inorder_rec(node.right)

    def preorder(self):
        self._preorder_rec(self.root)
        print()

    def _preorder_rec(self, node):
        if node:
            print(f"({node.id}, {node.name})", end=" ")
            self._preorder_rec(node.left)
            self._preorder_rec(node.right)

    def postorder(self):
        self._postorder_rec(self.root)
        print()

    def _postorder_rec(self, node):
        if node:
            self._postorder_rec(node.left)
            self._postorder_rec(node.right)
            print(f"({node.id}, {node.name})", end=" ")

def import_excel(tree, file_path):
    """Membaca file Excel dan memasukkan data ke BST"""
    try:
        wb = openpyxl.load_workbook(file_path)
        sheet = wb.active
        count = 0
        for i, row in enumerate(sheet.iter_rows(values_only=True)):
            if i == 0:  # Lewati baris pertama (header)
                continue
            id_val, name_val = row[0], row[1]
            if id_val is not None and name_val is not None:
                tree.insert(int(id_val), str(name_val))
                count += 1
        print(f"Berhasil mengimpor {count} data dari {file_path}")
    except FileNotFoundError:
        print(f"File '{file_path}' tidak ditemukan.")
    except ValueError as e:
        print(f"Format data salah (pastikan kolom A berisi angka, kolom B teks): {e}")
    except Exception as e:
        print(f"Error membaca Excel: {e}")

def main():
    tree = BST()
    while True:
        print("\n=== MENU BST ===")
        print("1. Tambah Data")
        print("2. Cari Data")
        print("3. Hapus Data")
        print("4. Traversal")
        print("5. Impor dari File Excel")
        print("6. Keluar")
        choice = input("Pilihan: ")

        if choice == "1":
            try:
                id_add = int(input("ID: "))
                name_add = input("Nama: ")
                tree.insert(id_add, name_add)
            except ValueError:
                print("ID harus angka.")
        elif choice == "2":
            try:
                id_search = int(input("ID yang dicari: "))
                node = tree.search(id_search)
                if node:
                    print(f"Ditemukan: ID={node.id}, Nama={node.name}")
                else:
                    print("Tidak ditemukan.")
            except ValueError:
                print("ID harus angka.")
        elif choice == "3":
            try:
                id_del = int(input("ID yang dihapus: "))
                tree.delete(id_del)
            except ValueError:
                print("ID harus angka.")
        elif choice == "4":
            print("Pilih Traversal: a.Inorder b.Preorder c.Postorder")
            trav = input().lower()
            if trav == "a":
                print("Inorder: ", end="")
                tree.inorder()
            elif trav == "b":
                print("Preorder: ", end="")
                tree.preorder()
            elif trav == "c":
                print("Postorder: ", end="")
                tree.postorder()
            else:
                print("Pilihan salah.")
        elif choice == "5":
            path = input("Masukkan path file Excel (contoh: data.xlsx atau C:/data.xlsx): ")
            import_excel(tree, path)
        elif choice == "6":
            print("Keluar.")
            break
        else:
            print("Pilihan tidak valid.")

if __name__ == "__main__":
    main()