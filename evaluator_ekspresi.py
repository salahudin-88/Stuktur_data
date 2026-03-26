class Node:
    def __init__(self, data):
        self.data = data
        self.next = None


class LinkedListStack:
    def __init__(self):
        self.top = None
        self.size = 0
    
    def push(self, data):
        new_node = Node(data)
        new_node.next = self.top
        self.top = new_node
        self.size += 1
        self.display_stack(f"Push {data}")
    
    def pop(self):
        if self.is_empty():
            raise Exception("Stack kosong!")
        
        data = self.top.data
        self.top = self.top.next
        self.size -= 1
        self.display_stack(f"Pop {data}")
        return data
    
    def peek(self):
        if self.is_empty():
            raise Exception("Stack kosong!")
        return self.top.data
    
    def is_empty(self):
        return self.top is None
    
    def get_size(self):
        return self.size
    
    def clear(self):
        self.top = None
        self.size = 0
        print("\n✅ Stack telah dikosongkan")
    
    def display_stack(self, action=""):
        if action:
            print(f"\n{action}")
        
        print("Stack (TOP -> BOTTOM): ", end="")
        if self.is_empty():
            print("[KOSONG]")
            return
        
        current = self.top
        stack_list = []
        while current:
            stack_list.append(str(current.data))
            current = current.next
        
        print("[" + " -> ".join(stack_list) + "]")


class EvaluatorEkspresi:
    def __init__(self):
        self.operator_stack = LinkedListStack()
        self.evaluasi_stack = LinkedListStack()
        self.last_result = "0"
    
    def get_priority(self, op):
        priorities = {'+': 1, '-': 1, '*': 2, '/': 2, '^': 3}
        return priorities.get(op, 0)
    
    def is_operator(self, c):
        return c in ['+', '-', '*', '/', '^']
    
    def is_digit(self, c):
        return c.isdigit()
    
    def infix_to_postfix(self, infix):
        print("\n╔══════════════════════════════════════╗")
        print("║    PROSES KONVERSI INFIX KE POSTFIX  ║")
        print("╚══════════════════════════════════════╝")
        
        self.operator_stack.clear()
        postfix = []
        
        for c in infix:
            if c == ' ':
                continue
            
            if self.is_digit(c):
                postfix.append(c)
                print(f"\n📌 Operand ditemukan: {c}")
                print(f"   Postfix sementara: {''.join(postfix)}")
            
            elif c == '(':
                self.operator_stack.push(c)
            
            elif c == ')':
                print("\n📌 Menemukan ')', pop sampai '('")
                while (not self.operator_stack.is_empty() and 
                       self.operator_stack.peek() != '('):
                    postfix.append(self.operator_stack.pop())
                
                if (not self.operator_stack.is_empty() and 
                    self.operator_stack.peek() == '('):
                    self.operator_stack.pop()
                
                print(f"   Postfix setelah ) : {''.join(postfix)}")
            
            elif self.is_operator(c):
                print(f"\n📌 Operator ditemukan: {c}")
                while (not self.operator_stack.is_empty() and 
                       self.operator_stack.peek() != '(' and 
                       self.get_priority(self.operator_stack.peek()) >= self.get_priority(c)):
                    postfix.append(self.operator_stack.pop())
                
                self.operator_stack.push(c)
                print(f"   Postfix sementara: {''.join(postfix)}")
        
        print("\n📌 Mengeluarkan sisa operator dalam stack")
        while not self.operator_stack.is_empty():
            postfix.append(self.operator_stack.pop())
        
        result = ''.join(postfix)
        print(f"\n✅ HASIL POSTFIX: {result}")
        return result
    
    def evaluate_postfix(self, postfix):
        print("\n╔══════════════════════════════════════╗")
        print("║      PROSES EVALUASI POSTFIX        ║")
        print("╚══════════════════════════════════════╝")
        
        self.evaluasi_stack.clear()
        
        for c in postfix:
            if self.is_digit(c):
                num = int(c)
                self.evaluasi_stack.push(num)
            
            elif self.is_operator(c):
                print(f"\n📌 Menemukan operator: {c}")
                operand2 = self.evaluasi_stack.pop()
                operand1 = self.evaluasi_stack.pop()
                
                if c == '+':
                    result = operand1 + operand2
                    print(f"   🔢 {operand1} + {operand2} = {result}")
                elif c == '-':
                    result = operand1 - operand2
                    print(f"   🔢 {operand1} - {operand2} = {result}")
                elif c == '*':
                    result = operand1 * operand2
                    print(f"   🔢 {operand1} * {operand2} = {result}")
                elif c == '/':
                    if operand2 == 0:
                        raise Exception("Pembagian dengan nol!")
                    result = operand1 // operand2
                    print(f"   🔢 {operand1} / {operand2} = {result}")
                elif c == '^':
                    result = operand1 ** operand2
                    print(f"   🔢 {operand1} ^ {operand2} = {result}")
                
                self.evaluasi_stack.push(result)
        
        final_result = self.evaluasi_stack.pop()
        print(f"\n✅ HASIL AKHIR: {final_result}")
        return final_result
    
    def evaluasi_ekspresi(self, infix):
        print(f"\n📝 Ekspresi Infix: {infix}")
        
        try:
            postfix = self.infix_to_postfix(infix)
            print(f"\n📊 Ekspresi Postfix: {postfix}")
            
            hasil = self.evaluate_postfix(postfix)
            self.last_result = str(hasil)
            
            print("\n╔══════════════════════════════════════╗")
            print("║            HASIL FINAL              ║")
            print("╠══════════════════════════════════════╣")
            print(f"║  {infix} = {hasil:<20} ║")
            print("╚══════════════════════════════════════╝")
            
        except Exception as e:
            print(f"\n❌ ERROR: {e}")
    
    def get_last_result(self):
        return self.last_result
    
    def set_last_result(self, result):
        self.last_result = result
    
    def display_menu(self):
        print("\n╔════════════════════════════════════════════╗")
        print("║     PROGRAM EVALUASI EKSPRESI ARITMATIKA  ║")
        print("║         (SINGLY LINKED LIST STACK)        ║")
        print("╠════════════════════════════════════════════╣")
        print("║  1. Evaluasi Ekspresi Baru                ║")
        print("║  2. Gunakan Hasil Sebelumnya              ║")
        print("║  3. Contoh Ekspresi                       ║")
        print("║  4. Informasi Stack                       ║")
        print("║  5. Reset/clear semua                     ║")
        print("║  0. Keluar                                 ║")
        print("╚════════════════════════════════════════════╝")
    
    def show_info(self):
        print("\n📌 INFORMASI STACK:")
        print("   • Menggunakan Singly Linked List")
        print("   • Setiap node memiliki data dan pointer ke node berikutnya")
        print("   • Operasi Push: Tambah node di awal (TOP)")
        print("   • Operasi Pop: Hapus node dari awal (TOP)")
        print("   • Kompleksitas waktu: O(1) untuk push dan pop")
        print("   • Representasi Stack: TOP -> node1 -> node2 -> ...")
    
    def show_examples(self):
        print("\n📚 CONTOH EKSPRESI:")
        print("   1. 3+5*2      =", 3+5*2)
        print("   2. (3+5)*2    =", (3+5)*2)
        print("   3. 10-4/2     =", 10-4//2)
        print("   4. (8-3)*4+6  =", (8-3)*4+6)
        print("   5. 2^3+4*2    =", 2**3 + 4*2)
        print("\n   Catatan: Gunakan angka 0-9 saja (single digit)")


def main():
    evaluator = EvaluatorEkspresi()
    
    print("\n╔════════════════════════════════════════════╗")
    print("║     EVALUATOR EKSPRESI ARITMATIKA         ║")
    print("║        DENGAN SINGLY LINKED LIST          ║")
    print("╚════════════════════════════════════════════╝")
    
    while True:
        evaluator.display_menu()
        
        try:
            pilihan = input("➤ Pilih menu (0-5): ").strip()
            
            if not pilihan.isdigit():
                print("\n❌ Input harus angka!")
                continue
            
            pilihan = int(pilihan)
            
            if pilihan == 1:
                infix = input("\n📝 Masukkan ekspresi infix: ")
                evaluator.evaluasi_ekspresi(infix)
            
            elif pilihan == 2:
                print(f"\n📌 Hasil sebelumnya: {evaluator.get_last_result()}")
                expr = input("📝 Masukkan ekspresi (gunakan 'x' untuk hasil sebelumnya): ")
                
                # Replace 'x' dengan hasil sebelumnya
                expr = expr.replace('x', evaluator.get_last_result())
                print(f"   Ekspresi setelah substitusi: {expr}")
                evaluator.evaluasi_ekspresi(expr)
            
            elif pilihan == 3:
                evaluator.show_examples()
            
            elif pilihan == 4:
                evaluator.show_info()
            
            elif pilihan == 5:
                evaluator.set_last_result("0")
                print("\n✅ Semua data telah direset!")
            
            elif pilihan == 0:
                print("\n👋 Terima kasih telah menggunakan program ini!")
                break
            
            else:
                print("\n❌ Pilihan tidak valid!")
        
        except Exception as e:
            print(f"\n❌ Error: {e}")


if __name__ == "__main__":
    main()