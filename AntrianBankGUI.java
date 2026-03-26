


import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Antrian {
    int nomor;
    String nama;
    String status;
    
    public Antrian(int nomor, String nama, String status) {
        this.nomor = nomor;
        this.nama = nama;
        this.status = status;
    }
}

public class AntrianBankGUI extends JFrame {
    private Queue<Antrian> antrianQueue;
    private DefaultTableModel tableModel;
    private JTextField txtNama;
    private JLabel lblStatus;
    private int nomorTerakhir = 0;
    
    public AntrianBankGUI() {
        antrianQueue = new LinkedList<>();
        
        // Setting frame
        setTitle("🏦 SISTEM ANTRIAN BANK");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }
        
        initializeUI();
    }
    
    private void initializeUI() {
        // Main panel dengan background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(44, 62, 80));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("🏦 SISTEM ANTRIAN BANK", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(236, 240, 241));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(44, 62, 80));
        
        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(44, 62, 80));
        
        JLabel lblNama = new JLabel("Nama:");
        lblNama.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNama.setForeground(new Color(236, 240, 241));
        
        txtNama = new JTextField(20);
        txtNama.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JButton btnAmbil = createButton("📋 Ambil Antrian", new Color(39, 174, 96));
        btnAmbil.addActionListener(e -> ambilAntrian());
        
        inputPanel.add(lblNama);
        inputPanel.add(txtNama);
        inputPanel.add(btnAmbil);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(44, 62, 80));
        
        JButton btnTampil = createButton("👥 Tampilkan Antrian", new Color(52, 152, 219));
        btnTampil.addActionListener(e -> tampilkanAntrian());
        
        JButton btnPanggil = createButton("📢 Panggil Antrian", new Color(230, 126, 34));
        btnPanggil.addActionListener(e -> panggilAntrian());
        
        JButton btnReset = createButton("🔄 Reset", new Color(231, 76, 60));
        btnReset.addActionListener(e -> resetAntrian());
        
        buttonPanel.add(btnTampil);
        buttonPanel.add(btnPanggil);
        buttonPanel.add(btnReset);
        
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(52, 73, 94), 2));
        
        // Table model
        String[] columns = {"Nomor Antrian", "Nama", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        centerPanel.add(tablePanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBackground(new Color(44, 62, 80));
        
        lblStatus = new JLabel("Total Antrian: 0");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        lblStatus.setForeground(new Color(241, 196, 15));
        
        statusPanel.add(lblStatus);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
    
    private void ambilAntrian() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Masukkan nama terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        nomorTerakhir++;
        Antrian antrian = new Antrian(nomorTerakhir, nama, "Menunggu");
        antrianQueue.add(antrian);
        
        txtNama.setText("");
        
        JOptionPane.showMessageDialog(this,
            String.format("Antrian berhasil diambil!\nNomor: %d\nNama: %s", 
                nomorTerakhir, nama),
            "Sukses",
            JOptionPane.INFORMATION_MESSAGE);
        
        tampilkanAntrian();
    }
    
    private void tampilkanAntrian() {
        tableModel.setRowCount(0);
        
        for (Antrian a : antrianQueue) {
            tableModel.addRow(new Object[]{a.nomor, a.nama, a.status});
        }
        
        lblStatus.setText("Total Antrian: " + antrianQueue.size());
    }
    
    private void panggilAntrian() {
        if (antrianQueue.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Tidak ada antrian untuk dipanggil!",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Antrian antrianDipanggil = antrianQueue.poll();
        antrianDipanggil.status = "Dipanggil";
        
        String pesan = String.format("📢 MEMANGGIL ANTRIAN\nNomor: %d\nNama: %s", 
            antrianDipanggil.nomor, antrianDipanggil.nama);
        
        JOptionPane.showMessageDialog(this, pesan, "Panggilan Antrian", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Panggil suara
        playSound(antrianDipanggil.nomor, antrianDipanggil.nama);
        
        tampilkanAntrian();
    }
    
    private void playSound(int nomor, String nama) {
        // Menggunakan beep sebagai alternatif
        Toolkit.getDefaultToolkit().beep();
        
        // Catatan: Untuk text-to-speech di Java, Anda bisa menggunakan library seperti FreeTTS
        // atau mengintegrasikan dengan sistem operasi
        System.out.println("Memanggil: Nomor " + nomor + ", Nama: " + nama);
    }
    
    private void resetAntrian() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin mereset semua antrian?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            antrianQueue.clear();
            nomorTerakhir = 0;
            tampilkanAntrian();
            lblStatus.setText("Antrian telah direset");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AntrianBankGUI().setVisible(true);
        });
    }
}