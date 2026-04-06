import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// ================= NODE =================
class Node {
    int nim;
    String nama;
    Node left, right;

    public Node(int nim, String nama) {
        this.nim = nim;
        this.nama = nama;
        left = right = null;
    }
}

// ================= BST =================
class BinarySearchTree {
    private Node root;

    // INSERT
    public void insert(int nim, String nama) {
        root = insertRec(root, nim, nama);
    }

    private Node insertRec(Node root, int nim, String nama) {
        if (root == null) return new Node(nim, nama);

        if (nim < root.nim)
            root.left = insertRec(root.left, nim, nama);
        else if (nim > root.nim)
            root.right = insertRec(root.right, nim, nama);
        else
            root.nama = nama;

        return root;
    }

    // SEARCH
    public String search(int nim) {
        Node result = searchRec(root, nim);
        return (result != null) ? result.nama : null;
    }

    private Node searchRec(Node root, int nim) {
        if (root == null || root.nim == nim) return root;
        return nim < root.nim ? searchRec(root.left, nim) : searchRec(root.right, nim);
    }

    // DELETE
    public boolean delete(int nim) {
        if (search(nim) == null) return false;
        root = deleteRec(root, nim);
        return true;
    }

    private Node deleteRec(Node root, int nim) {
        if (root == null) return null;

        if (nim < root.nim)
            root.left = deleteRec(root.left, nim);
        else if (nim > root.nim)
            root.right = deleteRec(root.right, nim);
        else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            Node successor = minValueNode(root.right);
            root.nim = successor.nim;
            root.nama = successor.nama;
            root.right = deleteRec(root.right, successor.nim);
        }
        return root;
    }

    private Node minValueNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // TRAVERSAL
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print("[" + root.nim + ": " + root.nama + "] ");
            inorderRec(root.right);
        }
    }

    public void preorder() {
        preorderRec(root);
        System.out.println();
    }

    private void preorderRec(Node root) {
        if (root != null) {
            System.out.print("[" + root.nim + ": " + root.nama + "] ");
            preorderRec(root.left);
            preorderRec(root.right);
        }
    }

    public void postorder() {
        postorderRec(root);
        System.out.println();
    }

    private void postorderRec(Node root) {
        if (root != null) {
            postorderRec(root.left);
            postorderRec(root.right);
            System.out.print("[" + root.nim + ": " + root.nama + "] ");
        }
    }

    // DISPLAY ALL
    public void displayAll() {
        if (root == null) {
            System.out.println("Tree kosong.");
            return;
        }
        System.out.println("Data Mahasiswa:");
        inorder();
    }

    // INFO TREE
    public void infoTree() {
        if (root == null) {
            System.out.println("Tree kosong.");
            return;
        }
        System.out.println("Tinggi Tree: " + getHeight(root));
        System.out.println("Jumlah Node: " + getNodeCount(root));
    }

    private int getHeight(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private int getNodeCount(Node node) {
        if (node == null) return 0;
        return 1 + getNodeCount(node.left) + getNodeCount(node.right);
    }

    // ================= CSV =================
        public void insertFromCSV(String filePath) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                int count = 0;

                while ((line = br.readLine()) != null) {

                    // Deteksi separator otomatis
                    String[] parts;
                    if (line.contains(";")) {
                        parts = line.split(";");
                    } else {
                        parts = line.split(",");
                    }

                    if (parts.length < 2) continue;

                    try {
                        int nim = Integer.parseInt(parts[0].trim());
                        String nama = parts[1].trim();

                        insert(nim, nama);
                        count++;

                    } catch (NumberFormatException e) {
                        System.out.println("Lewati baris: " + line);
                    }
                }

                System.out.println("Berhasil import " + count + " data dari CSV.");

            } catch (Exception e) {
                System.out.println("Error CSV: " + e.getMessage());
            }
        }

    // BALANCE TREE
    public void balanceTree() {
        List<Node> nodes = new ArrayList<>();
        storeInorder(root, nodes);
        root = buildBalanced(nodes, 0, nodes.size() - 1);
        System.out.println("Tree berhasil di-balance.");
    }

    private void storeInorder(Node root, List<Node> nodes) {
        if (root == null) return;
        storeInorder(root.left, nodes);
        nodes.add(new Node(root.nim, root.nama));
        storeInorder(root.right, nodes);
    }

    private Node buildBalanced(List<Node> nodes, int start, int end) {
        if (start > end) return null;

        int mid = (start + end) / 2;
        Node node = nodes.get(mid);

        node.left = buildBalanced(nodes, start, mid - 1);
        node.right = buildBalanced(nodes, mid + 1, end);

        return node;
    }
}

// ================= MAIN =================
public class BSTMahasiswa {
    private static Scanner scanner = new Scanner(System.in);
    private static BinarySearchTree bst = new BinarySearchTree();

    public static void main(String[] args) {
        int pilihan;

        do {
            tampilkanMenu();
            pilihan = getIntInput("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    tambahData();
                    break;
                case 2:
                    cariData();
                    break;
                case 3:
                    hapusData();
                    break;
                case 4:
                    System.out.print("Inorder: ");
                    bst.inorder();
                    break;
                case 5:
                    System.out.print("Preorder: ");
                    bst.preorder();
                    break;
                case 6:
                    System.out.print("Postorder: ");
                    bst.postorder();
                    break;
                case 7:
                    bst.displayAll();
                    break;
                case 8:
                    bst.infoTree();
                    break;
                case 9:
                    tambahDariCSV();
                    break;
                case 10:
                    bst.balanceTree();
                    break;
                case 11:
                    tambahDariExcel();
                    break;
                case 0:
                    System.out.println("Terima kasih.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }

        } while (pilihan != 0);
    }

    private static void tampilkanMenu() {
        System.out.println("\n======== MENU BINARY SEARCH TREE ========\n");
        System.out.println("1. Tambah Data");
        System.out.println("2. Cari Data");
        System.out.println("3. Hapus Data");
        System.out.println("4. Traversal (Inorder)");
        System.out.println("5. Traversal (Preorder)");
        System.out.println("6. Traversal (Postorder)");
        System.out.println("7. Tampilkan Semua Data");
        System.out.println("8. Informasi Tree");
        System.out.println("9. Tambah Data dari CSV");
        System.out.println("10. Balance/Rebalance Tree");
        System.out.println("11. Tambah Data dari Excel");
        System.out.println("0. Keluar");
        System.out.println("\n===================================================");
    }

    private static int getIntInput(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextInt()) {
            System.out.print("Harus angka! ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }

    private static void tambahData() {
        int nim = getIntInput("Masukkan NIM: ");
        System.out.print("Masukkan Nama: ");
        String nama = scanner.nextLine();

        bst.insert(nim, nama);
    }

    private static void cariData() {
        int nim = getIntInput("Masukkan NIM: ");
        String nama = bst.search(nim);

        if (nama != null)
            System.out.println("Ditemukan: " + nama);
        else
            System.out.println("Tidak ditemukan");
    }

    private static void hapusData() {
        int nim = getIntInput("Masukkan NIM: ");
        if (bst.delete(nim))
            System.out.println("Berhasil dihapus");
        else
            System.out.println("Tidak ditemukan");
    }

    private static void tambahDariCSV() {
        System.out.print("Path CSV: ");
        String path = scanner.nextLine();
        bst.insertFromCSV(path);
    }

    // ===== EXCEL TANPA APACHE POI =====
    private static void tambahDariExcel() {
        System.out.print("Masukkan path Excel (.xlsx): ");
        String excelPath = scanner.nextLine();

        String csvPath = convertExcelToCSV(excelPath);

        if (csvPath != null) {
            bst.insertFromCSV(csvPath);

            // optional: hapus CSV setelah dipakai
            new File(csvPath).delete();
        }
    }

    // CONVERTER
    @SuppressWarnings("UseSpecificCatch")
    private static String convertExcelToCSV(String excelPath) {
        String csvPath = excelPath.replace(".xlsx", ".csv");

        try {
            String command = "powershell -Command \""
                    + "$excel = New-Object -ComObject Excel.Application; "
                    + "$excel.Visible = $false; "
                    + "$wb = $excel.Workbooks.Open('" + excelPath + "'); "
                    + "$wb.SaveAs('" + csvPath + "', 6); "
                    + "$wb.Close($false); "
                    + "$excel.Quit();\"";

            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();

            System.out.println("Excel berhasil dikonversi ke CSV.");
            return csvPath;

        } catch (Exception e) {
            System.out.println("Error konversi: " + e.getMessage());
            return null;
        }
    }
}