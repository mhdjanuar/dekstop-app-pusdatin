/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.dao.PerhitunganDao;
import application.models.PresentaseModel;
import application.models.RangkingModel;
import application.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mhdja
 */
public class PerhitunganDaoImpl implements PerhitunganDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
    public PerhitunganDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<RangkingModel> findRangkingKelurahan() {
        List<RangkingModel> list = new ArrayList<>();
        query = "SELECT KELURAHAN, COUNT(*) AS jumlah_layak " +
                "FROM data_verifikasi " +
                "WHERE HASIL_MUSKEL_KELAYAKAN = 'LAYAK' " +
                "GROUP BY KELURAHAN " +
                "ORDER BY jumlah_layak DESC";

        try {
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                RangkingModel model = new RangkingModel();
                model.setNama(resultSet.getString("KELURAHAN"));
                model.setNilai(resultSet.getInt("jumlah_layak"));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return list;
    }
    
    private void closeStatement() {
        try {
            if(pstmt != null){
                pstmt.close();
                pstmt = null;
            }
            if(resultSet != null){
                resultSet.close();
                resultSet = null;
            }   
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RangkingModel> findRangkingKecamatan() {
        List<RangkingModel> list = new ArrayList<>();
        query = "SELECT KECAMATAN, COUNT(*) AS jumlah_layak " +
                "FROM data_verifikasi " +
                "WHERE HASIL_MUSKEL_KELAYAKAN = 'LAYAK' " +
                "GROUP BY KECAMATAN " +
                "ORDER BY jumlah_layak DESC";

        try {
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                RangkingModel model = new RangkingModel();
                model.setNama(resultSet.getString("KECAMATAN"));
                model.setNilai(resultSet.getInt("jumlah_layak"));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return list;
    }

    @Override
    public List<PresentaseModel> findPresentaseKelayakan() {
        List<PresentaseModel> list = new ArrayList<>();
        query = "SELECT hasil_muskel_kelayakan, " +
                "COUNT(*) AS jumlah, " +
                "(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM data_verifikasi WHERE COALESCE(hasil_muskel_kelayakan, '') <> '')) AS persen " +
                "FROM data_verifikasi " +
                "WHERE COALESCE(hasil_muskel_kelayakan, '') <> '' " +
                "GROUP BY hasil_muskel_kelayakan";

        try {
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                PresentaseModel model = new PresentaseModel();
                model.setName(resultSet.getString("hasil_muskel_kelayakan"));
                model.setJumlah(resultSet.getInt("jumlah"));
                model.setPersen(resultSet.getDouble("persen"));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return list;
    }


    @Override
    public List<PresentaseModel> findPresentaseKetTidakLayak() {
        List<PresentaseModel> list = new ArrayList<>();
        query = "SELECT KETERANGAN_MUSKEL, " +
                "COUNT(*) AS jumlah, " +
                "(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM data_verifikasi WHERE HASIL_MUSKEL_KELAYAKAN = 'TIDAK LAYAK' AND COALESCE(KETERANGAN_MUSKEL, '') <> '')) AS persen " +
                "FROM data_verifikasi " +
                "WHERE HASIL_MUSKEL_KELAYAKAN = 'TIDAK LAYAK' " +
                "AND COALESCE(KETERANGAN_MUSKEL, '') <> '' " +
                "GROUP BY KETERANGAN_MUSKEL";

        try {
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                PresentaseModel model = new PresentaseModel();
                model.setName(resultSet.getString("KETERANGAN_MUSKEL"));
                model.setJumlah(resultSet.getInt("jumlah"));
                model.setPersen(resultSet.getDouble("persen"));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return list;
    }


    @Override
    public List<PresentaseModel> findPresentaseKelayakanKelurahan(String kelurahan, String kecamatan, int idListData) {
        List<PresentaseModel> list = new ArrayList<>();
        query = "SELECT hasil_muskel_kelayakan, " +
                "COUNT(*) AS jumlah, " +
                "(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM data_verifikasi)) AS persen " +
                "FROM data_verifikasi " +  // Perhatikan spasi di akhir
                "WHERE kelurahan = ? AND kecamatan = ? AND id_list_data = ? " +
                "GROUP BY hasil_muskel_kelayakan";

        try {
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, kelurahan);
            pstmt.setString(2, kecamatan);
            pstmt.setInt(3, idListData);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                PresentaseModel model = new PresentaseModel();
                model.setName(resultSet.getString("hasil_muskel_kelayakan"));
                model.setJumlah(resultSet.getInt("jumlah"));
                model.setPersen(resultSet.getDouble("persen"));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return list;
    }

    @Override
    public List<PresentaseModel> findPresentaseKelayakanKecamatan(String kecamatan, String kelurahan, int idListData) {
        List<PresentaseModel> list = new ArrayList<>();
        query = "SELECT hasil_muskel_kelayakan, " +
                "COUNT(*) AS jumlah, " +
                "(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM data_verifikasi)) AS persen " +
                "FROM data_verifikasi " +  // Perhatikan spasi di akhir
                "WHERE kecamatan = ? AND kelurahan = ? AND id_list_data = ? " +
                "GROUP BY hasil_muskel_kelayakan";

        try {
            System.out.println("Executing Query: " + query);
            System.out.println("Parameters: Kecamatan=" + kecamatan + ", Kelurahan=" + kelurahan + ", ID List Data=" + idListData);
            
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, kecamatan);
            pstmt.setString(2, kelurahan);
            pstmt.setInt(3, idListData);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                PresentaseModel model = new PresentaseModel();
                model.setName(resultSet.getString("hasil_muskel_kelayakan"));
                model.setJumlah(resultSet.getInt("jumlah"));
                model.setPersen(resultSet.getDouble("persen"));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return list;
    }

    @Override
    public List<PresentaseModel> findPresentaseKelayakanListData(int idListData) {
        List<PresentaseModel> list = new ArrayList<>();
        query = "SELECT hasil_muskel_kelayakan, " +
                "COUNT(*) AS jumlah, " +
                "(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM data_verifikasi WHERE id_list_data = ? AND hasil_muskel_kelayakan IS NOT NULL AND hasil_muskel_kelayakan <> '')) AS persen " +
                "FROM data_verifikasi " +
                "WHERE id_list_data = ? AND hasil_muskel_kelayakan IS NOT NULL AND hasil_muskel_kelayakan <> '' " +
                "GROUP BY hasil_muskel_kelayakan";

        try {
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, idListData);
            pstmt.setInt(2, idListData); // Parameter kedua untuk id_list_data di subquery
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                PresentaseModel model = new PresentaseModel();
                model.setName(resultSet.getString("hasil_muskel_kelayakan"));
                model.setJumlah(resultSet.getInt("jumlah"));
                model.setPersen(resultSet.getDouble("persen"));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return list;
    }
    
}
