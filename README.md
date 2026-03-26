# Sistem-antirian-bank
import tkinter as tk
from tkinter import messagebox, ttk
import pygame
import threading
import time
import pyttsx3
import queue
import os

class AntrianBank:
    def __init__(self, root):
        self.root = root
        self.root.title("Sistem Antrian Bank - Dengan Suara")
        self.root.geometry("900x700")
        
        # Inisialisasi text-to-speech
        self.init_tts()
        
        # Data antrian menggunakan queue
        self.antrian = queue.Queue()
        self.daftar_antrian = []  # Untuk tampilan
        self.nomor_terakhir = 0
        self.antrian_dipanggil = []
        self.sedang_memanggil = False
        
        # Warna tema
        self.warna_bg = "#2c3e50"
        self.warna_button = "#3498db"
        self.warna_text = "#ecf0f1"
        
        self.setup_ui()
        
    def init_tts(self):
        """Inisialisasi text-to-speech dengan berbagai suara"""
        try:
            self.engine = pyttsx3.init()
            
            # Dapatkan daftar suara yang tersedia
            self.voices = self.engine.getProperty('voices')
            
            # Set properti suara
            self.engine.setProperty('rate', 150)    # Kecepatan bicara
            self.engine.setProperty('volume', 0.9)  # Volume
            
            print(f"Tersedia {len(self.voices)} suara:")
            for i, voice in enumerate(self.voices):
                print(f"{i}: {voice.name}")
                
        except Exception as e:
            print(f"Error inisialisasi TTS: {e}")
            self.engine = None
    
    def setup_ui(self):
        # Frame utama
        main_frame = tk.Frame(self.root, bg=self.warna_bg)
        main_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)
        
        # Judul dengan animasi
        judul = tk.Label(main_frame, text="🏦 SISTEM ANTRIAN BANK DENGAN SUARA 🏦", 
                        font=("Arial", 20, "bold"), 
                        bg=self.warna_bg, fg=self.warna_text)
        judul.pack(pady=10)
        
        # Frame untuk input
        input_frame = tk.Frame(main_frame, bg=self.warna_bg)
        input_frame.pack(pady=10)
        
        tk.Label(input_frame, text="Nama:", font=("Arial", 12), 
                bg=self.warna_bg, fg=self.warna_text).grid(row=0, column=0, padx=5)
        
        self.entry_nama = tk.Entry(input_frame, font=("Arial", 12), width=30)
        self.entry_nama.grid(row=0, column=1, padx=5)
        self.entry_nama.bind('<Return>', lambda e: self.ambil_antrian())
        
        btn_ambil = tk.Button(input_frame, text="📋 Ambil Antrian", 
                            command=self.ambil_antrian,
                            font=("Arial", 12, "bold"), bg="#27ae60", fg="white",
                            padx=20, pady=10, cursor="hand2")
        btn_ambil.grid(row=0, column=2, padx=10)
        
        # Frame untuk pengaturan suara
        sound_frame = tk.LabelFrame(main_frame, text="🔊 Pengaturan Suara", 
                                   font=("Arial", 10, "bold"),
                                   bg=self.warna_bg, fg=self.warna_text,
                                   padx=10, pady=10)
        sound_frame.pack(fill=tk.X, pady=10)
        
        # Pilihan suara
        tk.Label(sound_frame, text="Pilih Suara:", bg=self.warna_bg, 
                fg=self.warna_text).grid(row=0, column=0, padx=5)
        
        self.suara_var = tk.StringVar(value="default")
        suara_options = ["default", "pria", "wanita", "anak"]
        
        for i, suara in enumerate(suara_options):
            rb = tk.Radiobutton(sound_frame, text=suara.capitalize(), 
                               variable=self.suara_var, value=suara,
                               bg=self.warna_bg, fg=self.warna_text,
                               selectcolor=self.warna_bg,
                               command=self.ubah_suara)
            rb.grid(row=0, column=i+1, padx=5)
        
        # Kecepatan suara
        tk.Label(sound_frame, text="Kecepatan:", bg=self.warna_bg, 
                fg=self.warna_text).grid(row=1, column=0, padx=5, pady=5)
        
        self.kecepatan_var = tk.IntVar(value=150)
        speed_scale = tk.Scale(sound_frame, from_=50, to=300, 
                              orient=tk.HORIZONTAL, length=200,
                              variable=self.kecepatan_var,
                              bg=self.warna_bg, fg=self.warna_text,
                              command=self.ubah_kecepatan)
        speed_scale.grid(row=1, column=1, columnspan=4, padx=5, pady=5)
        
        # Tombol-tombol utama
        button_frame = tk.Frame(main_frame, bg=self.warna_bg)
        button_frame.pack(pady=10)
        
        buttons = [
            ("👥 Tampilkan Antrian", "#3498db", self.tampilkan_antrian),
            ("📢 Panggil Antrian", "#e67e22", self.panggil_antrian),
            ("🔁 Panggil Ulang", "#f39c12", self.panggil_ulang),
            ("🔄 Reset", "#e74c3c", self.reset_antrian)
        ]
        
        for text, color, command in buttons:
            btn = tk.Button(button_frame, text=text, command=command,
                          font=("Arial", 11, "bold"), bg=color, fg="white",
                          padx=15, pady=8, cursor="hand2")
            btn.pack(side=tk.LEFT, padx=5)
        
        # Frame untuk display antrian
        display_frame = tk.Frame(main_frame)
        display_frame.pack(fill=tk.BOTH, expand=True, pady=10)
        
        # Notebook untuk tab
        notebook = ttk.Notebook(display_frame)
        notebook.pack(fill=tk.BOTH, expand=True)
        
        # Tab Antrian Aktif
        active_frame = tk.Frame(notebook, bg="white")
        notebook.add(active_frame, text="📋 Antrian Aktif")
        
        # Treeview untuk antrian aktif
        columns = ('No', 'Nama', 'Waktu Ambil', 'Status')
        self.tree_active = ttk.Treeview(active_frame, columns=columns, 
                                        show='headings', height=8)
        
        self.tree_active.heading('No', text='Nomor')
        self.tree_active.heading('Nama', text='Nama')
        self.tree_active.heading('Waktu Ambil', text='Waktu Ambil')
        self.tree_active.heading('Status', text='Status')
        
        self.tree_active.column('No', width=80, anchor='center')
        self.tree_active.column('Nama', width=200, anchor='center')
        self.tree_active.column('Waktu Ambil', width=150, anchor='center')
        self.tree_active.column('Status', width=100, anchor='center')
        
        scroll_active = ttk.Scrollbar(active_frame, orient=tk.VERTICAL, 
                                     command=self.tree_active.yview)
        self.tree_active.configure(yscrollcommand=scroll_active.set)
        
        self.tree_active.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        scroll_active.pack(side=tk.RIGHT, fill=tk.Y)
        
        # Tab Riwayat Panggilan
        history_frame = tk.Frame(notebook, bg="white")
        notebook.add(history_frame, text="📜 Riwayat Panggilan")
        
        columns_history = ('No', 'Nama', 'Waktu Panggil')
        self.tree_history = ttk.Treeview(history_frame, columns=columns_history,
                                        show='headings', height=8)
        
        self.tree_history.heading('No', text='Nomor')
        self.tree_history.heading('Nama', text='Nama')
        self.tree_history.heading('Waktu Panggil', text='Waktu Panggil')
        
        self.tree_history.column('No', width=100, anchor='center')
        self.tree_history.column('Nama', width=250, anchor='center')
        self.tree_history.column('Waktu Panggil', width=200, anchor='center')
        
        scroll_history = ttk.Scrollbar(history_frame, orient=tk.VERTICAL,
                                      command=self.tree_history.yview)
        self.tree_history.configure(yscrollcommand=scroll_history.set)
        
        self.tree_history.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        scroll_history.pack(side=tk.RIGHT, fill=tk.Y)
        
        # Panel informasi
        info_frame = tk.Frame(main_frame, bg=self.warna_bg)
        info_frame.pack(fill=tk.X, pady=10)
        
        self.status_label = tk.Label(info_frame, text="", 
                                     font=("Arial", 14, "bold"),
                                     bg=self.warna_bg, fg="#f1c40f")
        self.status_label.pack(side=tk.LEFT, padx=10)
        
        self.suara_status = tk.Label(info_frame, text="🔊 Suara: Aktif", 
                                     font=("Arial", 10),
                                     bg=self.warna_bg, fg="#2ecc71")
        self.suara_status.pack(side=tk.RIGHT, padx=10)
        
        # Display panggilan sekarang
        self.now_calling = tk.Label(main_frame, text="⏳ Menunggu panggilan...",
                                   font=("Arial", 16, "bold"),
                                   bg="#34495e", fg="#ecf0f1",
                                   pady=10)
        self.now_calling.pack(fill=tk.X, pady=5)
        
    def ubah_suara(self):
        """Mengubah jenis suara"""
        if not self.engine:
            return
        try:
            suara = self.suara_var.get()
            if suara == "pria" and len(self.voices) > 1:
                self.engine.setProperty('voice', self.voices[0].id)
            elif suara == "wanita" and len(self.voices) > 0:
                # Cari suara wanita
                for voice in self.voices:
                    if "female" in voice.name.lower():
                        self.engine.setProperty('voice', voice.id)
                        break
            elif suara == "anak" and len(self.voices) > 2:
                # Cari suara anak-anak
                for voice in self.voices:
                    if "child" in voice.name.lower() or "kids" in voice.name.lower():
                        self.engine.setProperty('voice', voice.id)
                        break
            
            self.suara_status.config(text=f"🔊 Suara: {suara.capitalize()}")
            
        except Exception as e:
            print(f"Error ganti suara: {e}")
    
    def ubah_kecepatan(self, value):
        """Mengubah kecepatan suara"""
        if self.engine:
            self.engine.setProperty('rate', int(value))
    
    def ambil_antrian(self):
        nama = self.entry_nama.get().strip()
        if not nama:
            messagebox.showwarning("Peringatan", "Masukkan nama terlebih dahulu!")
            return
        
        self.nomor_terakhir += 1
        waktu = time.strftime("%H:%M:%S")
        
        data_antrian = {
            'nomor': self.nomor_terakhir,
            'nama': nama,
            'waktu': waktu,
            'status': 'Menunggu'
        }
        
        self.antrian.put(data_antrian)
        self.daftar_antrian.append(data_antrian)
        self.entry_nama.delete(0, tk.END)
        
        # Suara konfirmasi
        threading.Thread(target=self.suara_ambil_antrian, 
                        args=(self.nomor_terakhir, nama), daemon=True).start()
        
        messagebox.showinfo("Sukses", f"✅ Antrian berhasil diambil!\nNomor: {self.nomor_terakhir}\nNama: {nama}")
        self.tampilkan_antrian()
    
    def suara_ambil_antrian(self, nomor, nama):
        """Suara konfirmasi ambil antrian"""
        if self.engine:
            try:
                teks = f"Terima kasih {nama}. Nomor antrian anda adalah {nomor}"
                self.engine.say(teks)
                self.engine.runAndWait()
            except:
                pass
    
    def panggil_antrian(self):
        if not self.daftar_antrian:
            messagebox.showinfo("Informasi", "Tidak ada antrian untuk dipanggil!")
            return
        
        if self.sedang_memanggil:
            messagebox.showinfo("Informasi", "Sedang memanggil antrian, tunggu sebentar...")
            return
        
        # Ambil antrian pertama
        antrian_dipanggil = self.daftar_antrian.pop(0)
        antrian_dipanggil['status'] = 'Dipanggil'
        antrian_dipanggil['waktu_panggil'] = time.strftime("%H:%M:%S")
        self.antrian_dipanggil.append(antrian_dipanggil)
        
        # Update display
        self.now_calling.config(text=f" SEKARANG DIPANGGIL: Nomor {antrian_dipanggil['nomor']} - {antrian_dipanggil['nama']}")
        
        # Tampilkan pesan
        pesan = f"MEMANGGIL ANTRIAN\nNomor: {antrian_dipanggil['nomor']}\nNama: {antrian_dipanggil['nama']}"
        
        # Panggil suara dengan variasi
        self.sedang_memanggil = True
        threading.Thread(target=self.panggil_dengan_suara, 
                        args=(antrian_dipanggil['nomor'], antrian_dipanggil['nama']), 
                        daemon=True).start()
        
        messagebox.showinfo("Panggilan Antrian", pesan)
        self.tampilkan_antrian()
        self.sedang_memanggil = False
    
    def panggil_dengan_suara(self, nomor, nama):
        """Memanggil antrian dengan suara yang lebih natural"""
        if not self.engine:
            # Fallback ke beep
            for _ in range(3):
                print('\a')
                time.sleep(0.5)
            return
        
        try:
            # Panggilan pertama
            teks1 = f"Nomor antrian {nomor}"
            self.engine.say(teks1)
            self.engine.runAndWait()
            time.sleep(0.5)
            
            # Panggilan kedua dengan nama
            teks2 = f"Atas nama {nama}"
            self.engine.say(teks2)
            self.engine.runAndWait()
            time.sleep(0.5)
            
            # Panggilan ketiga
            teks3 = f"Dipanggil menuju loket pelayanan"
            self.engine.say(teks3)
            self.engine.runAndWait()
            time.sleep(0.5)
            
            # Ulang sekali lagi
            teks4 = f"Nomor {nomor}, {nama}"
            self.engine.say(teks4)
            self.engine.runAndWait()
            
        except Exception as e:
            print(f"Error suara: {e}")
            # Fallback ke beep
            for _ in range(3):
                print('\a')
                time.sleep(0.5)
    
    def panggil_ulang(self):
        """Memanggil ulang antrian terakhir"""
        if not self.antrian_dipanggil:
            messagebox.showinfo("Informasi", "Belum ada antrian yang dipanggil!")
            return
        
        antrian_terakhir = self.antrian_dipanggil[-1]
        threading.Thread(target=self.panggil_dengan_suara,
                        args=(antrian_terakhir['nomor'], antrian_terakhir['nama']),
                        daemon=True).start()
        
        self.now_calling.config(text=f" PANGGILAN ULANG: Nomor {antrian_terakhir['nomor']} - {antrian_terakhir['nama']}")
        
    def tampilkan_antrian(self):
        # Hapus data lama
        for item in self.tree_active.get_children():
            self.tree_active.delete(item)
        
        for item in self.tree_history.get_children():
            self.tree_history.delete(item)
        
        # Tampilkan antrian aktif
        for data in self.daftar_antrian:
            self.tree_active.insert('', tk.END, 
                                   values=(data['nomor'], data['nama'], 
                                          data['waktu'], data['status']))
        
        # Tampilkan riwayat panggilan
        for data in reversed(self.antrian_dipanggil[-10:]):  # 10 terakhir
            self.tree_history.insert('', 0,
                                   values=(data['nomor'], data['nama'],
                                          data.get('waktu_panggil', '-')))
        
        self.status_label.config(text=f"Total Antrian: {len(self.daftar_antrian)} | Terpanggil: {len(self.antrian_dipanggil)}")
    
    def reset_antrian(self):
        if messagebox.askyesno("Konfirmasi", "Apakah Anda yakin ingin mereset semua antrian?"):
            # Kosongkan antrian
            while not self.antrian.empty():
                self.antrian.get()
            
            self.daftar_antrian.clear()
            self.antrian_dipanggil.clear()
            self.nomor_terakhir = 0
            self.now_calling.config(text="⏳ Menunggu panggilan...")
            self.tampilkan_antrian()
            
            # Suara reset
            if self.engine:
                threading.Thread(target=lambda: self.engine.say("Sistem antrian direset"),
                               daemon=True).start()
            
            messagebox.showinfo("Info", " Sistem antrian telah direset")

def main():
    root = tk.Tk()
    zzz = AntrianBank(root)
    root.mainloop()
    
if __name__ == "__main__":
    main()
