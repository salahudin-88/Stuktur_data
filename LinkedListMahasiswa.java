package singly_linked_list;
import java.util.Scanner;

import teks_berjalan.Node;

class Node {
    String nim;
    String nama;
    Node next;
    
    Node(String nim, String nama) {
        this.nim = nim;
        this.nama = nama;
        this.next = null;
    }
}

class SinglyLinkedList {
    private Node head;
    private int count;
    
    public SinglyLinkedList() {
        this.head = null;
        this.count = 0;
    }
    
    // 1. Sisipkan di awal
    public void insertFirst(String nim, String nama) {
        Node newNode = new Node(nim, nama);
        if (head == null) {
            head = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        count++;
        System.out.println("Data berhasil ditambahkan di awal!");
    }
    
    // 2. Sisipkan pada posisi tertentu
    public void insertAtPosition(String nim, String nama, int position) {
        if (position < 1 || position > count + 1) {
            System.out.println("Posisi tidak valid! Posisi harus antara 1 - " + (count + 1));
            return;
        }
        
        Node newNode = new Node(nim, nama);
        
        if (position == 1) {
            newNode.next = head;
            head = newNode;
        } else {
            Node current = head;
            for (int i = 1; i < position - 1; i++) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        count++;
        System.out.println("Data berhasil ditambahkan di posisi " + position + "!");
    }
    
    // 3. Sisipkan di akhir
    public void insertLast(String nim, String nama) {
        Node newNode = new Node(nim, nama);
        
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        count++;
        System.out.println("Data berhasil ditambahkan di akhir!");
    }
    
    // 4. Hapus dari awal
    public void deleteFirst() {
        if (head == null) {
            System.out.println("LinkedList kosong!");
            return;
        }
        
        System.out.println("Data yang dihapus: " + head.nim + " - " + head.nama);
        head = head.next;
        count--;
        System.out.println("Data berhasil dihapus dari awal!");
    }
    
    // 5. Hapus posisi tertentu
    public void deleteAtPosition(int position) {
        if (head == null) {
            System.out.println("LinkedList kosong!");
            return;
        }
        
        if (position < 1 || position > count) {
            System.out.println("Posisi tidak valid! Posisi harus antara 1 - " + count);
            return;
        }
        
        Node temp;
        if (position == 1) {
            temp = head;
            head = head.next;
        } else {
            Node current = head;
            for (int i = 1; i < position - 1; i++) {
                current = current.next;
            }
            temp = current.next;
            current.next = temp.next;
        }
        System.out.println("Data yang dihapus: " + temp.nim + " - " + temp.nama);
        count--;
        System.out.println("Data berhasil dihapus dari posisi " + position + "!");
    }
    
    // 6. Hapus dari akhir
    public void deleteLast() {
        if (head == null) {
            System.out.println("LinkedList kosong!");
            return;
        }
        
        Node temp;
        if (head.next == null) {
            temp = head;
            head = null;
        } else {
            Node current = head;
            while (current.next.next != null) {
                current = current.next;
            }
            temp = current.next;
            current.next = null;
        }
        System.out.println("Data yang dihapus: " + temp.nim + " - " + temp.nama);
        count--;
        System.out.println("Data berhasil dihapus dari akhir!");
    }
    
    // 7. Hapus kejadian pertama berdasarkan NIM
    public void deleteByNIM(String nim) {
        if (head == null) {
            System.out.println("LinkedList kosong!");
            return;
        }
        
        if (head.nim.equals(nim)) {
            System.out.println("Data yang dihapus: " + head.nim + " - " + head.nama);
            head = head.next;
            count--;
            System.out.println("Data berhasil dihapus!");
            return;
        }
        
        Node current = head;
        Node previous = null;
        
        while (current != null && !current.nim.equals(nim)) {
            previous = current;
            current = current.next;
        }
        
        if (current == null) {
            System.out.println("Data dengan NIM " + nim + " tidak ditemukan!");
            return;
        }
        
        System.out.println("Data yang dihapus: " + current.nim + " - " + current.nama);
        if (previous != null) {
            previous.next = current.next;
        }
        count--;
        System.out.println("Data berhasil dihapus!");
    }
    
    // 8. Show data
    public void display() {
        if (head == null) {
            System.out.println("LinkedList kosong!");
            return;
        }
        
        System.out.println("\n=== Data Mahasiswa ===");
        System.out.println("Jumlah data: " + count);
        System.out.println("----------------------");
        
        Node current = head;
        int position = 1;
        while (current != null) {
            System.out.println(position + ". " + current.nim + " - " + current.nama);
            current = current.next;
            position++;
        }
        System.out.println("======================\n");
    }
    
    public int getCount() {
        return count;
    }
}

public class LinkedListMahasiswa {
    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SinglyLinkedList list = new SinglyLinkedList();
        int choice;
        String nim, nama;
        int position;
        
        do {
            System.out.println("\n=== MENU SINGLY LINKED LIST MAHASISWA ===");
            System.out.println("1. Sisipkan di awal");
            System.out.println("2. Sisipkan pada posisi tertentu");
            System.out.println("3. Sisipkan di akhir");
            System.out.println("4. Hapus dari awal");
            System.out.println("5. Hapus posisi tertentu");
            System.out.println("6. Hapus dari akhir");
            System.out.println("7. Hapus kejadian pertama (berdasarkan NIM)");
            System.out.println("8. Show data");
            System.out.println("9. Exit");
            System.out.print("Pilih menu (1-9): ");
            
            choice = scanner.nextInt();
            scanner.nextLine(); // membersihkan buffer
            
            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- Sisipkan di Awal ---");
                    System.out.print("Masukkan NIM: ");
                    nim = scanner.nextLine();
                    System.out.print("Masukkan Nama: ");
                    nama = scanner.nextLine();
                    list.insertFirst(nim, nama);
                }
                    
                case 2 -> {
                    System.out.println("\n--- Sisipkan pada Posisi Tertentu ---");
                    System.out.print("Masukkan NIM: ");
                    nim = scanner.nextLine();
                    System.out.print("Masukkan Nama: ");
                    nama = scanner.nextLine();
                    System.out.print("Masukkan posisi (1 - " + (list.getCount() + 1) + "): ");
                    position = scanner.nextInt();
                    list.insertAtPosition(nim, nama, position);
                }
                    
                case 3 -> {
                    System.out.println("\n--- Sisipkan di Akhir ---");
                    System.out.print("Masukkan NIM: ");
                    nim = scanner.nextLine();
                    System.out.print("Masukkan Nama: ");
                    nama = scanner.nextLine();
                    list.insertLast(nim, nama);
                }
                    
                case 4 -> {
                    System.out.println("\n--- Hapus dari Awal ---");
                    list.deleteFirst();
                }
                    
                case 5 -> {
                    System.out.println("\n--- Hapus Posisi Tertentu ---");
                    if (list.getCount() > 0) {
                        System.out.print("Masukkan posisi yang akan dihapus (1 - " + list.getCount() + "): ");
                        position = scanner.nextInt();
                        list.deleteAtPosition(position);
                    } else {
                        System.out.println("LinkedList kosong!");
                    }
                }
                    
                case 6 -> {
                    System.out.println("\n--- Hapus dari Akhir ---");
                    list.deleteLast();
                }
                    
                case 7 -> {
                    System.out.println("\n--- Hapus Berdasarkan NIM ---");
                    if (list.getCount() > 0) {
                        System.out.print("Masukkan NIM yang akan dihapus: ");
                        nim = scanner.nextLine();
                        list.deleteByNIM(nim);
                    } else {
                        System.out.println("LinkedList kosong!");
                    }
                }
                    
                case 8 -> list.display();
                    
                case 9 -> System.out.println("Terima kasih! Program selesai.");
                    
                default -> System.out.println("Pilihan tidak valid! Silakan pilih 1-9.");
            }
        } while (choice != 9);
        
        scanner.close();
    }
}