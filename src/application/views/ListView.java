/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package application.views;

import application.dao.DataVerifikasiDao;
import application.dao.PerhitunganDao;
import application.daoimpl.DataVerifikasiDaoImpl;
import application.daoimpl.PerhitunganDaoImpl;
import application.models.DataVerifikasiModel;
import application.models.ListDataModel;
import application.models.PresentaseModel;
import application.models.UserModel;
import application.utils.DatabaseUtil;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author mhdja
 */
public class ListView extends javax.swing.JPanel {
    public final DataVerifikasiDao dataVerifikasiDao;
    public final ListDataModel listDetail;
    public final PerhitunganDao perhitunganDao;
    private JPanel Pane; // Referensi ke Pane
    private Map<Integer, Boolean> selectedRows = new HashMap<>();
    private String pageType;
    private UserModel userAuth;

    
    private void populateTable(List<DataVerifikasiModel> dataList) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Hanya checkbox yang bisa diedit
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // Kolom pertama adalah checkbox
                }
                return super.getColumnClass(columnIndex);
            }
        };

        model.setColumnIdentifiers(new Object[]{
            "Pilih", "ID", "Nama", "No KK", "NIK", "Alamat", "Kelurahan", "Kecamatan", "Status", "Aksi"
        });

        for (DataVerifikasiModel data : dataList) {
            // Ambil status ceklis sebelumnya jika ada, jika tidak ada default false
            boolean isSelected = selectedRows.getOrDefault(data.getId(), false);
            String status = data.getHasilMuskelKelayakan();
            
            // Cek hasil Muskel Kelayakan
            if ("Layak".equalsIgnoreCase(data.getHasilMuskelKelayakan())) {
                switch (userAuth.getRoleId()) {
                    case 3:
                        status = "Verifikasi Kecamatan";
                        break;
                    case 2:
                        status = "Verifikasi Kelurahan";
                        break;
                    default:
                        status = "Verifikasi Admin";
                        break;
                }
            }
            
            model.addRow(new Object[]{
                isSelected, // Load status checkbox
                data.getId(), data.getName(), data.getNoKk(), data.getNik(),
                data.getAddress(), data.getKelurahan(), data.getKecamatan(),
                status, "Edit"
            });
        }

        jTable1.setModel(model);
        
        if (!"Edit".equals(pageType)) {
            // Sembunyikan kolom "Pilih" (Index 0)
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(0).setWidth(0);

            // Sembunyikan kolom "Aksi" (Index 9)
            jTable1.getColumnModel().getColumn(9).setMinWidth(0);
            jTable1.getColumnModel().getColumn(9).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(9).setWidth(0);
        }

        // Tambahkan listener untuk menyimpan status ceklis saat diubah
        jTable1.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 0) { // Hanya update kalau kolom checkbox berubah
                Boolean checked = (Boolean) jTable1.getValueAt(row, 0);
                Integer id = (Integer) jTable1.getValueAt(row, 1); // Ambil ID data
                selectedRows.put(id, checked); // Simpan ke HashMap
            }
        });
        
        // Tambahkan event listener untuk menangani klik tombol edit
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = jTable1.getColumnModel().getColumnIndex("Aksi");
                int row = jTable1.rowAtPoint(evt.getPoint());

                if (row >= 0 && column == jTable1.columnAtPoint(evt.getPoint())) {
                    int id = (int) jTable1.getValueAt(row, 1); // Ambil ID data
                    
                    Pane.removeAll();
                    Pane.repaint();
                    Pane.revalidate();

                    // add Panel, add panel
                    Pane.add(new RejectView(id, Pane, listDetail));
                    Pane.repaint();
                    Pane.revalidate();
                }
            }
        });
    }


    public void getAllData(int idListData) {
        List<DataVerifikasiModel> dataList = dataVerifikasiDao.findByIdListData(idListData);
        populateTable(dataList);
    }
    
    public void loadKelurahanToComboBox() {
        List<DataVerifikasiModel> kelurahanList = dataVerifikasiDao.findKelurahan();

        jComboBoxKelurahan.addItem("Pilih Kelurahan"); // Opsi default
        for (DataVerifikasiModel data : kelurahanList) {
            jComboBoxKelurahan.addItem(data.getKelurahan());
        }
    }
    
    public void loadKecamatanToComboBox() {
        List<DataVerifikasiModel> kecamatanList = dataVerifikasiDao.findKecamatan();

        jComboBoxKecamatan.addItem("Pilih Kecamatan"); // Opsi default
        for (DataVerifikasiModel data : kecamatanList) {
            jComboBoxKecamatan.addItem(data.getKecamatan());
        }
    }

    
    /**
     * Creates new form ListView
     */
    public ListView(JPanel Pane, ListDataModel listdataDetail, String pageType, UserModel userAuth) {
        initComponents();
        
        this.Pane = Pane;
        this.dataVerifikasiDao = new DataVerifikasiDaoImpl();
        this.perhitunganDao = new PerhitunganDaoImpl();
        this.listDetail = listdataDetail;
        this.pageType = pageType;
        this.userAuth = userAuth;
        
        labelHeader.setText(listdataDetail.getName()); // Ubah teks label
        getAllData(listdataDetail.getId());
        loadKelurahanToComboBox();
        loadKecamatanToComboBox();
        
        // Tambahkan Listener setelah JComboBox diisi
        jComboBoxKelurahan.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                String selectedKelurahan = null;
                
                if(!"Pilih Kelurahan".equals((String) jComboBoxKelurahan.getSelectedItem())) {
                    selectedKelurahan = (String) jComboBoxKelurahan.getSelectedItem();
                }
               
                filterData(selectedKelurahan, (String) jComboBoxKecamatan.getSelectedItem(), noKK.getText(), listdataDetail.getId());
            }
        });
        
        jComboBoxKecamatan.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                String selectedKecamatan = null;

                if(!"Pilih Kecamatan".equals((String) jComboBoxKecamatan.getSelectedItem())) {
                    selectedKecamatan = (String) jComboBoxKecamatan.getSelectedItem();
                }

                filterData((String) jComboBoxKelurahan.getSelectedItem(), selectedKecamatan, noKK.getText(), listdataDetail.getId());
            }
        });
        
        if(!"Edit".equals(pageType)) {
            jButton1.hide();
            jButton3.hide();
        }
        
        if(!"Lihat".equals(pageType)) {
            jButton2.hide();
            jButton4.hide();
            jButton5.hide();
        }
    }
    
    public void filterData(String kelurahan, String kecamatan, String noKK, int idListData) {
        String filterKelurahan = null;
        String filterKecamatan = null;
        String filterNoKK = null;
        
        if(!"Pilih Kelurahan".equals(kelurahan)) {
            filterKelurahan = kelurahan;
        }
        
        if(!"Pilih Kecamatan".equals(kecamatan)) {
            filterKecamatan = kecamatan;
        }
        
        if(!"Cari NO KK ...".equals(noKK)) {
            filterNoKK = noKK;
        }
        
        // Debug: Cek nilai parameter sebelum query
        System.out.println("DEBUG: filterData called with parameters:");
        System.out.println("Kelurahan: " + filterKelurahan);
        System.out.println("Kecamatan: " + filterKecamatan);
        System.out.println("No KK: " + filterNoKK);
        System.out.println("ID List Data: " + idListData);

        // Debug: Cek apakah listDetail memiliki ID yang benar
        System.out.println("listDetail.getId(): " + listDetail.getId());

        List<DataVerifikasiModel> dataList = dataVerifikasiDao.findByFilters(listDetail.getId(), filterKelurahan, filterKecamatan, filterNoKK);

        // Debug: Cek hasil dari query
        if (dataList.isEmpty()) {
            System.out.println("DEBUG: No data found for the given filters.");
        } else {
            System.out.println("DEBUG: Data retrieved: " + dataList.size() + " records.");
            for (DataVerifikasiModel data : dataList) {
                System.out.println(data);
            }
        }

        populateTable(dataList);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        labelHeader = new javax.swing.JLabel();
        jComboBoxKecamatan = new javax.swing.JComboBox<>();
        noKK = new javax.swing.JTextField();
        searchNoKK = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jComboBoxKelurahan = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(700, 700));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        labelHeader.setFont(new java.awt.Font("72 Black", 1, 24)); // NOI18N
        labelHeader.setText("LIST DATA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(272, 272, 272)
                .addComponent(labelHeader)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(labelHeader)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jComboBoxKecamatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxKecamatanActionPerformed(evt);
            }
        });

        searchNoKK.setBackground(new java.awt.Color(0, 0, 0));
        searchNoKK.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        searchNoKK.setForeground(new java.awt.Color(255, 255, 255));
        searchNoKK.setText("Cari No KK");
        searchNoKK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchNoKKMouseClicked(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Ceklis Semua");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("VERIFIKASI");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Unduh Kelurahan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Unduh Kecamatan");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 0, 0));
        jButton5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Unduh Semua");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBoxKecamatan, javax.swing.GroupLayout.Alignment.LEADING, 0, 226, Short.MAX_VALUE)
                            .addComponent(jComboBoxKelurahan, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(noKK))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(searchNoKK, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(16, 16, 16))
            .addGroup(layout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxKelurahan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxKecamatan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchNoKK, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(noKK, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchNoKKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchNoKKMouseClicked
        // TODO add your handling code here:   
        String kelurahan = (String) jComboBoxKelurahan.getSelectedItem();
        String kecamatan = (String) jComboBoxKecamatan.getSelectedItem();
        String numberKK = noKK.getText();
        
        filterData(kelurahan, kecamatan, numberKK, listDetail.getId());
    }//GEN-LAST:event_searchNoKKMouseClicked

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(true, i, 0); // Set semua checkbox sesuai dengan ceklis semua
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        List<DataVerifikasiModel> selectedData = getSelectedData();

        if (selectedData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada data yang dipilih.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        } else {
            // Tampilkan pop-up konfirmasi
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin mengupdate " + selectedData.size() + " data?", 
                "Konfirmasi Update", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);

            // Jika user memilih "YES", lakukan update
            if (confirm == JOptionPane.YES_OPTION) {
                String kelurahan = (String) jComboBoxKelurahan.getSelectedItem();
                String kecamatan = (String) jComboBoxKecamatan.getSelectedItem();
                String numberKK = noKK.getText();
        
                dataVerifikasiDao.updateBulk(selectedData);
                
                selectedRows.clear();
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                filterData(kelurahan, kecamatan, numberKK, listDetail.getId());
                
            }
        }
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            String namaKelurahan = (String) jComboBoxKelurahan.getSelectedItem();
            
            List<PresentaseModel> presentaseList = perhitunganDao.findPresentaseKelayakanKelurahan(namaKelurahan);
            
            int allData = 0;

            // Pastikan list memiliki minimal 2 elemen sebelum mengakses indeksnya
            if (presentaseList.size() >= 2) {
                allData = presentaseList.get(0).getJumlah() + presentaseList.get(1).getJumlah();
            }

            String countData = String.valueOf(allData);
            
            String templateName = "ReportDataKelurahan.jrxml";
            InputStream reportStream = ReportView.class.getResourceAsStream("/resources/reports/" + templateName);
            JasperDesign jd = JRXmlLoader.load(reportStream);

            Connection dbConnection = DatabaseUtil.getInstance().getConnection();

            JasperReport jr = JasperCompileManager.compileReport(jd);

            HashMap parameter = new HashMap();
            parameter.put("PATH","src/resources/images/");
            parameter.put("NAMA_JUDUL", "KELURAHAN");
            parameter.put("NAMA_KELURAHAN", namaKelurahan);
            parameter.put("COUNT_ALL_DATA", countData);
            parameter.put("COUNT_LAYAK", String.valueOf(presentaseList.get(0).getJumlah()));
            parameter.put("COUNT_TIDAK_LAYAK", String.valueOf(presentaseList.get(1).getJumlah()));

            JasperPrint jp = JasperFillManager.fillReport(jr,parameter, dbConnection);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(ReportView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            String namaKecamatan = (String) jComboBoxKecamatan.getSelectedItem();
            
            List<PresentaseModel> presentaseList = perhitunganDao.findPresentaseKelayakanKecamatan(namaKecamatan);
            
            int allData = 0;

            // Pastikan list memiliki minimal 2 elemen sebelum mengakses indeksnya
            if (presentaseList.size() >= 2) {
                allData = presentaseList.get(0).getJumlah() + presentaseList.get(1).getJumlah();
            }

            String countData = String.valueOf(allData);
            
            String templateName = "ReportDataKelurahan.jrxml";
            InputStream reportStream = ReportView.class.getResourceAsStream("/resources/reports/" + templateName);
            JasperDesign jd = JRXmlLoader.load(reportStream);

            Connection dbConnection = DatabaseUtil.getInstance().getConnection();

            JasperReport jr = JasperCompileManager.compileReport(jd);

            HashMap parameter = new HashMap();
            parameter.put("PATH","src/resources/images/");
            parameter.put("NAMA_JUDUL", "KECAMATAN");
            parameter.put("NAMA_KELURAHAN", namaKecamatan);
            parameter.put("COUNT_ALL_DATA", countData);
            parameter.put("COUNT_LAYAK", String.valueOf(presentaseList.get(0).getJumlah()));
            parameter.put("COUNT_TIDAK_LAYAK", String.valueOf(presentaseList.get(1).getJumlah()));

            JasperPrint jp = JasperFillManager.fillReport(jr,parameter, dbConnection);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(ReportView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {            
            List<PresentaseModel> presentaseList = perhitunganDao.findPresentaseKelayakanListData(listDetail.getId());
            
            int allData = 0;

            // Pastikan list memiliki minimal 2 elemen sebelum mengakses indeksnya
            if (presentaseList.size() >= 2) {
                allData = presentaseList.get(0).getJumlah() + presentaseList.get(1).getJumlah();
            }

            String countData = String.valueOf(allData);
            
            String templateName = "ReportDataKelurahan.jrxml";
            InputStream reportStream = ReportView.class.getResourceAsStream("/resources/reports/" + templateName);
            JasperDesign jd = JRXmlLoader.load(reportStream);

            Connection dbConnection = DatabaseUtil.getInstance().getConnection();

            JasperReport jr = JasperCompileManager.compileReport(jd);

            HashMap parameter = new HashMap();
            parameter.put("PATH","src/resources/images/");
            parameter.put("NAMA_JUDUL", "REKAPAN");
            parameter.put("NAMA_KELURAHAN", listDetail.getName());
            parameter.put("COUNT_ALL_DATA", countData);
            parameter.put("COUNT_LAYAK", String.valueOf(presentaseList.get(0).getJumlah()));
            parameter.put("COUNT_TIDAK_LAYAK", String.valueOf(presentaseList.get(1).getJumlah()));

            JasperPrint jp = JasperFillManager.fillReport(jr,parameter, dbConnection);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(ReportView.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_jButton5ActionPerformed

    
    private void jComboBoxKecamatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxKecamatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxKecamatanActionPerformed
    
    public List<DataVerifikasiModel> getSelectedData() {
        List<DataVerifikasiModel> selectedData = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean isChecked = (boolean) model.getValueAt(i, 0);
            if (isChecked) {
                DataVerifikasiModel data = new DataVerifikasiModel();
                data.setId((int) model.getValueAt(i, 1));
                data.setName((String) model.getValueAt(i, 2));
                data.setNoKk((String) model.getValueAt(i, 3));
                data.setNik((String) model.getValueAt(i, 4));
                data.setAddress((String) model.getValueAt(i, 5));
                data.setKelurahan((String) model.getValueAt(i, 6));
                data.setKecamatan((String) model.getValueAt(i, 7));
                data.setStatus((String) model.getValueAt(i, 8));
                data.setHasilMuskelKelayakan("LAYAK");
                selectedData.add(data);
            }
        }

        return selectedData;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBoxKecamatan;
    private javax.swing.JComboBox<String> jComboBoxKelurahan;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labelHeader;
    private javax.swing.JTextField noKK;
    private javax.swing.JButton searchNoKK;
    // End of variables declaration//GEN-END:variables
}
