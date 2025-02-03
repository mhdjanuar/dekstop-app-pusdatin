/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package application.views;

import application.dao.ListDataDao;
import application.daoimpl.ListDataDaoImpl;
import application.models.ListDataModel;
import application.utils.DatabaseUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
public class ListDataView extends javax.swing.JPanel {
    public final ListDataDao listDataDao;
    private JPanel Pane; // Referensi ke Pane
    private JPanel PaneHeader;

    /**
     * Creates new form ListDataView
     */
    public ListDataView(JPanel Pane) {
        this.Pane = Pane;
        this.listDataDao = new ListDataDaoImpl();
        
        List<ListDataModel> listDataAll = listDataDao.findAll();
        
        initComponents();
        
        addLabelsToPanel(listDataAll);
    }
    
    private void addLabelsToPanel(List<ListDataModel> listDataAll) {
        PaneList.setLayout(new BoxLayout(PaneList, BoxLayout.Y_AXIS));

        for (ListDataModel data : listDataAll) {
            // Panel untuk setiap baris (Label + Button)
            JPanel rowPanel = new JPanel(new GridBagLayout());
            rowPanel.setBackground(Color.WHITE);
            rowPanel.setPreferredSize(new Dimension(400, 50)); // Atur Lebar & Tinggi
            rowPanel.setMinimumSize(new Dimension(400, 50));
            rowPanel.setMaximumSize(new Dimension(400, 50));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weighty = 1; // Supaya tinggi tetap
            gbc.insets = new Insets(5, 5, 5, 5); // Jarak padding antar komponen

            // JLabel (Di kiri)
            JLabel label = new JLabel(data.getName());
            gbc.gridx = 0; // Kolom pertama (Paling kiri)
            gbc.weightx = 1.0; // Supaya label bisa mengisi ruang tersisa
            gbc.anchor = GridBagConstraints.WEST;
            rowPanel.add(label, gbc);

            // Tombol Edit (Di kanan)
            JButton btnLihat = new JButton("Lihat");
            gbc.gridx = 1; // Kolom kedua
            gbc.weightx = 0; // Tidak mengambil banyak ruang
            gbc.anchor = GridBagConstraints.EAST;
            rowPanel.add(btnLihat, gbc);

            // Tombol Delete (Di kanan setelah Edit)
            JButton btnEdit = new JButton("Edit");
            gbc.gridx = 2; // Kolom ketiga
            rowPanel.add(btnEdit, gbc);
            
            // Tambahkan event listener secara terpisah
            addButtonActions(btnLihat, btnEdit, data);

            // Tambahkan rowPanel ke PaneList
            PaneList.add(rowPanel);
            PaneList.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer antar row
        }

        // Refresh tampilan
        PaneList.revalidate();
        PaneList.repaint();
    }
    
    private void addButtonActions(JButton btnLihat, JButton btnEdit, ListDataModel data) {
        btnLihat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tambahkan logika edit di sini
                Pane.removeAll();
                Pane.repaint();
                Pane.revalidate();

                // add Panel, add panel
                Pane.add(new ListView(Pane, data, "Lihat"));
                Pane.repaint();
                Pane.revalidate();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tambahkan logika edit di sini
                Pane.removeAll();
                Pane.repaint();
                Pane.revalidate();

                // add Panel, add panel
                Pane.add(new ListView(Pane, data, "Edit"));
                Pane.repaint();
                Pane.revalidate();
            }
        });
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
        PaneList = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(700, 700));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 145, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PaneListLayout = new javax.swing.GroupLayout(PaneList);
        PaneList.setLayout(PaneListLayout);
        PaneListLayout.setHorizontalGroup(
            PaneListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );
        PaneListLayout.setVerticalGroup(
            PaneListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
        );

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Unduh Data");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(PaneList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(156, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PaneList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
               // TODO add your handling code here:
               String templateName = "ListDataReport.jrxml";
               InputStream reportStream = ReportView.class.getResourceAsStream("/resources/reports/" + templateName);
               JasperDesign jd = JRXmlLoader.load(reportStream);

               Connection dbConnection = DatabaseUtil.getInstance().getConnection();

               JasperReport jr = JasperCompileManager.compileReport(jd);

               HashMap parameter = new HashMap();
               parameter.put("PATH","src/resources/images/");

               JasperPrint jp = JasperFillManager.fillReport(jr,parameter, dbConnection);
               JasperViewer.viewReport(jp, false);
           } catch (JRException ex) {
               Logger.getLogger(ReportView.class.getName()).log(Level.SEVERE, null, ex);
           }  
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PaneList;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
