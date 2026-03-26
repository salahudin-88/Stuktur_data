package evaluator_ekspresi;
import java.util.Scanner;

class Node<T> {
    T data;
    Node<T> next;
    
    public Node(T data) {
        this.data = data;
        this.next = null;
    }
}

// Stack menggunakan Singly Linked List
class LinkedListStack<T> {
    private Node<T> top;
    private int size;
    
    public LinkedListStack() {
        top = null;
        size = 0;
    }
    
    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
        displayStack("Push " + data);
    }
    
    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack kosong!");
        }
        T data = top.data;
        top = top.next;
        size--;
        displayStack("Pop " + data);
        return data;
    }
    
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack kosong!");
        }
        return top.data;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public int getSize() {
        return size;
    }
    
    public void clear() {
        top = null;
        size = 0;
        System.out.println("Stack telah dikosongkan");
    }
    
    public void displayStack(String action) {
        System.out.println("\n" + action);
        System.out.print("Stack (TOP -> BOTTOM): ");
        if (isEmpty()) {
            System.out.println("[KOSONG]");
            return;
        }
        
        Node<T> current = top;
        System.out.print("[");
        while (current != null) {
            System.out.print(current.data);
            if (current.next != null) {
                System.out.print(" -> ");
            }
            current = current.next;
        }
        System.out.println("]");
    }
    
    public void displayStack() {
        displayStack("Kondisi Stack Saat Ini");
    }
}

class EvaluatorEkspresi {
    private final LinkedListStack<Character> operatorStack;
    private final LinkedListStack<Integer> evaluasiStack;
    private String lastResult;
    
    public EvaluatorEkspresi() {
        operatorStack = new LinkedListStack<>();
        evaluasiStack = new LinkedListStack<>();
        lastResult = "0";
    }
    
    private int getPriority(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }
    
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
    
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    
    public String infixToPostfix(String infix) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║    PROSES KONVERSI INFIX KE POSTFIX  ║");
        System.out.println("╚══════════════════════════════════════╝");
        
        operatorStack.clear();
        StringBuilder postfix = new StringBuilder();
        
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            
            if (c == ' ') continue;
            
            if (isDigit(c)) {
                postfix.append(c);
                System.out.println("\n Operand ditemukan: " + c);
                System.out.println("   Postfix sementara: " + postfix);
            }
            else if (c == '(') {
                operatorStack.push(c);
            }
            else if (c == ')') {
                System.out.println("\n Menemukan ')', pop sampai '('");
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    postfix.append(operatorStack.pop());
                }
                if (!operatorStack.isEmpty() && operatorStack.peek() == '(') {
                    operatorStack.pop(); // Pop '('
                }
                System.out.println("   Postfix setelah ) : " + postfix);
            }
            else if (isOperator(c)) {
                System.out.println("\n Operator ditemukan: " + c);
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(' && 
                       getPriority(operatorStack.peek()) >= getPriority(c)) {
                    postfix.append(operatorStack.pop());
                }
                operatorStack.push(c);
                System.out.println("   Postfix sementara: " + postfix);
            }
        }
        
        System.out.println("\n Mengeluarkan sisa operator dalam stack");
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop());
        }
        
        String result = postfix.toString();
        System.out.println("\n HASIL POSTFIX: " + result);
        return result;
    }
    
    public int evaluatePostfix(String postfix) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║      PROSES EVALUASI POSTFIX        ║");
        System.out.println("╚══════════════════════════════════════╝");
        
        evaluasiStack.clear();
        
        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);
            
            if (isDigit(c)) {
                int num = c - '0';
                evaluasiStack.push(num);
            }
            else if (isOperator(c)) {
                System.out.println("\n Menemukan operator: " + c);
                int operand2 = evaluasiStack.pop();
                int operand1 = evaluasiStack.pop();
                int result = 0;
                
                switch (c) {
                    case '+' -> {
                        result = operand1 + operand2;
                        System.out.println("   " + operand1 + " + " + operand2 + " = " + result);
                    }
                    case '-' -> {
                        result = operand1 - operand2;
                        System.out.println("   " + operand1 + " - " + operand2 + " = " + result);
                    }
                    case '*' -> {
                        result = operand1 * operand2;
                        System.out.println("   " + operand1 + " * " + operand2 + " = " + result);
                    }
                    case '/' -> {
                        if (operand2 == 0) {
                            throw new ArithmeticException("Pembagian dengan nol!");
                        }
                        result = operand1 / operand2;
                        System.out.println("   " + operand1 + " / " + operand2 + " = " + result);
                    }
                    case '^' -> {
                        result = (int) Math.pow(operand1, operand2);
                        System.out.println("   " + operand1 + " ^ " + operand2 + " = " + result);
                    }
                }
                
                evaluasiStack.push(result);
            }
        }
        
        int finalResult = evaluasiStack.pop();
        System.out.println("\n HASIL AKHIR: " + finalResult);
        return finalResult;
    }
    
    public void evaluasiEkspresi(String infix) {
        System.out.println("\n Ekspresi Infix: " + infix);
        
        try {
            String postfix = infixToPostfix(infix);
            System.out.println("\n Ekspresi Postfix: " + postfix);
            
            int hasil = evaluatePostfix(postfix);
            lastResult = String.valueOf(hasil);
            
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║            HASIL FINAL              ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.printf("║  %s = %-20d ║\n", infix, hasil);
            System.out.println("╚══════════════════════════════════════╝");
            
        } catch (Exception e) {
            System.out.println("\n ERROR: " + e.getMessage());
        }
    }
    
    public String getLastResult() {
        return lastResult;
    }
    
    public void setLastResult(String result) {
        this.lastResult = result;
    }
    
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║     PROGRAM EVALUASI EKSPRESI ARITMATIKA  ║");
        System.out.println("║         (SINGLY LINKED LIST STACK)        ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║  1. Evaluasi Ekspresi Baru                ║");
        System.out.println("║  2. Gunakan Hasil Sebelumnya              ║");
        System.out.println("║  3. Contoh Ekspresi                       ║");
        System.out.println("║  4. Informasi Stack                       ║");
        System.out.println("║  5. Reset/clear semua                     ║");
        System.out.println("║  0. Keluar                                 ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }
    
    public void showInfo() {
        System.out.println("\n INFORMASI STACK:");
        System.out.println("   • Menggunakan Singly Linked List");
        System.out.println("   • Setiap node memiliki data dan pointer ke node berikutnya");
        System.out.println("   • Operasi Push: Tambah node di awal (TOP)");
        System.out.println("   • Operasi Pop: Hapus node dari awal (TOP)");
        System.out.println("   • Kompleksitas waktu: O(1) untuk push dan pop");
    }
    
    public void showExamples() {
        System.out.println("\n CONTOH EKSPRESI:");
        System.out.println("   1. 3+5*2      = " + (3+5*2));
        System.out.println("   2. (3+5)*2    = " + ((3+5)*2));
        System.out.println("   3. 10-4/2     = " + (10-4/2));
        System.out.println("   4. (8-3)*4+6  = " + ((8-3)*4+6));
        System.out.println("   5. 2^3+4*2    = " + ((int)Math.pow(2,3) + 4*2));
        System.out.println("\n   Catatan: Gunakan angka 0-9 saja (single digit)");
    }
}

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            EvaluatorEkspresi evaluator = new EvaluatorEkspresi();
            int pilihan;
            
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║     EVALUATOR EKSPRESI ARITMATIKA         ║");
            System.out.println("║        DENGAN SINGLY LINKED LIST          ║");
            System.out.println("╚════════════════════════════════════════════╝");
            
            do {
                evaluator.displayMenu();
                System.out.print(" Pilih menu (0-5): ");
                
                try {
                    pilihan = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    
                    switch (pilihan) {
                        case 1 -> {
                            System.out.print("\n Masukkan ekspresi infix: ");
                            String infix = scanner.nextLine();
                            evaluator.evaluasiEkspresi(infix);
                        }
                        
                        case 2 -> {
                            System.out.println("\n Hasil sebelumnya: " + evaluator.getLastResult());
                            System.out.print(" Masukkan ekspresi (gunakan 'x' untuk hasil sebelumnya): ");
                            String expr = scanner.nextLine();
                            
                            // Replace 'x' dengan hasil sebelumnya
                            expr = expr.replace("x", evaluator.getLastResult());
                            System.out.println("   Ekspresi setelah substitusi: " + expr);
                            evaluator.evaluasiEkspresi(expr);
                        }
                        
                        case 3 -> evaluator.showExamples();
                        
                        case 4 -> evaluator.showInfo();
                        
                        case 5 -> {
                            evaluator.setLastResult("0");
                            System.out.println("\n Semua data telah direset!");
                        }
                        
                        case 0 -> System.out.println("\n Terima kasih telah menggunakan program ini!");
                        
                        default -> System.out.println("\n Pilihan tidak valid!");
                    }
                    
                } catch (Exception e) {
                    System.out.println("\n Input tidak valid: " + e.getMessage());
                    scanner.nextLine(); // clear buffer
                    pilihan = -1;
                }
                
            } while (pilihan != 0);
        }
    }
}
