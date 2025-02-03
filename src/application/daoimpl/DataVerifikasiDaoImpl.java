/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.dao.DataVerifikasiDao;
import application.models.DataVerifikasiModel;
import application.utils.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mhdja
 */
public class DataVerifikasiDaoImpl implements DataVerifikasiDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
    public DataVerifikasiDaoImpl() {
       dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<DataVerifikasiModel> findAll() {
        List<DataVerifikasiModel> listDataVerifikasi = new ArrayList<>();
        
        try {
            query = "SELECT * FROM data_verifikasi";
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();
            
            while (resultSet.next()) {
                DataVerifikasiModel dataVerifikasi = new DataVerifikasiModel();
                dataVerifikasi.setId(resultSet.getInt("id"));
                dataVerifikasi.setName(resultSet.getString("name"));
                dataVerifikasi.setNoKk(resultSet.getString("no_kk"));
                dataVerifikasi.setNik(resultSet.getString("nik"));
                dataVerifikasi.setAddress(resultSet.getString("address"));
                dataVerifikasi.setKelurahan(resultSet.getString("kelurahan"));
                dataVerifikasi.setKecamatan(resultSet.getString("kecamatan"));
                dataVerifikasi.setHasilMuskelKelayakan(resultSet.getString("hasil_muskel_kelayakan"));
                dataVerifikasi.setKeteranganMuskel(resultSet.getString("keterangan_muskel"));
                dataVerifikasi.setIdListData(resultSet.getInt("id_list_data"));
                dataVerifikasi.setStatus(resultSet.getString("status"));
                // Set other attributes accordingly
                listDataVerifikasi.add(dataVerifikasi);
            }
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
        
        return listDataVerifikasi;
    }
    
    @Override
    public List<DataVerifikasiModel> findByIdListData(int idListData) {
        List<DataVerifikasiModel> listDataVerifikasi = new ArrayList<>();
        String query = "SELECT * FROM data_verifikasi WHERE id_list_data = ?";

        try {
            pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, idListData);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                DataVerifikasiModel dataVerifikasi = new DataVerifikasiModel();
                dataVerifikasi.setId(resultSet.getInt("id"));
                dataVerifikasi.setName(resultSet.getString("name"));
                dataVerifikasi.setNoKk(resultSet.getString("no_kk"));
                dataVerifikasi.setNik(resultSet.getString("nik"));
                dataVerifikasi.setAddress(resultSet.getString("address"));
                dataVerifikasi.setKelurahan(resultSet.getString("kelurahan"));
                dataVerifikasi.setKecamatan(resultSet.getString("kecamatan"));
                dataVerifikasi.setHasilMuskelKelayakan(resultSet.getString("hasil_muskel_kelayakan"));
                dataVerifikasi.setKeteranganMuskel(resultSet.getString("keterangan_muskel"));
                dataVerifikasi.setIdListData(resultSet.getInt("id_list_data"));
                dataVerifikasi.setStatus(resultSet.getString("status"));

                listDataVerifikasi.add(dataVerifikasi);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return listDataVerifikasi;
    }
    
    @Override
    public List<DataVerifikasiModel> findByFilters(Integer idListData, String kelurahan, String kecamatan, String noKk) {
        List<DataVerifikasiModel> listDataVerifikasi = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM data_verifikasi WHERE 1=1");

        if (idListData != null) {
            query.append(" AND id_list_data = ?");
        }
        if (kelurahan != null && !kelurahan.isEmpty()) {
            query.append(" AND kelurahan LIKE ?");
        }
        if (kecamatan != null && !kecamatan.isEmpty()) {
            query.append(" AND kecamatan LIKE ?");
        }
        if (noKk != null && !noKk.isEmpty()) {
            query.append(" AND no_kk = ?");
        }

        try {
            pstmt = dbConnection.prepareStatement(query.toString());

            int paramIndex = 1;
            if (idListData != null) {
                pstmt.setInt(paramIndex++, idListData);
            }
            if (kelurahan != null && !kelurahan.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + kelurahan + "%"); // LIKE untuk pencarian fleksibel
            }
            if (kecamatan != null && !kecamatan.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + kecamatan + "%");
            }
            if (noKk != null && !noKk.isEmpty()) {
                pstmt.setString(paramIndex++, noKk);
            }

            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                DataVerifikasiModel dataVerifikasi = new DataVerifikasiModel();
                dataVerifikasi.setId(resultSet.getInt("id"));
                dataVerifikasi.setName(resultSet.getString("name"));
                dataVerifikasi.setNoKk(resultSet.getString("no_kk"));
                dataVerifikasi.setNik(resultSet.getString("nik"));
                dataVerifikasi.setAddress(resultSet.getString("address"));
                dataVerifikasi.setKelurahan(resultSet.getString("kelurahan"));
                dataVerifikasi.setKecamatan(resultSet.getString("kecamatan"));
                dataVerifikasi.setHasilMuskelKelayakan(resultSet.getString("hasil_muskel_kelayakan"));
                dataVerifikasi.setKeteranganMuskel(resultSet.getString("keterangan_muskel"));
                dataVerifikasi.setIdListData(resultSet.getInt("id_list_data"));
                dataVerifikasi.setStatus(resultSet.getString("status"));

                listDataVerifikasi.add(dataVerifikasi);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }

        return listDataVerifikasi;
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
    public List<DataVerifikasiModel> findKelurahan() {
        List<DataVerifikasiModel> listKelurahan = new ArrayList<>();
        String query = "SELECT DISTINCT kelurahan FROM data_verifikasi";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                DataVerifikasiModel data = new DataVerifikasiModel();
                data.setKelurahan(resultSet.getString("kelurahan"));
                listKelurahan.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listKelurahan;
    }

    @Override
    public List<DataVerifikasiModel> findKecamatan() {
        List<DataVerifikasiModel> listKecamatan = new ArrayList<>();
        String query = "SELECT DISTINCT kecamatan FROM data_verifikasi";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                DataVerifikasiModel data = new DataVerifikasiModel();
                data.setKecamatan(resultSet.getString("kecamatan"));
                listKecamatan.add(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listKecamatan;
    }

    @Override
    public int createBulk(List<DataVerifikasiModel> dataVerifikasi, int listDataId) {
        int result = 0;
        try {
            query = "INSERT INTO data_verifikasi (name, no_kk, nik, address, kelurahan, kecamatan, hasil_muskel_kelayakan, keterangan_muskel, id_list_data, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = dbConnection.prepareStatement(query);

            for (DataVerifikasiModel data : dataVerifikasi) {
                pstmt.setString(1, data.getName());
                pstmt.setString(2, data.getNoKk());
                pstmt.setString(3, data.getNik());
                pstmt.setString(4, data.getAddress());
                pstmt.setString(5, data.getKelurahan());
                pstmt.setString(6, data.getKecamatan());
                pstmt.setString(7, data.getHasilMuskelKelayakan());
                pstmt.setString(8, data.getKeteranganMuskel());
                pstmt.setInt(9, listDataId);
                pstmt.setString(10, data.getStatus());
                pstmt.addBatch();
            }

            int[] batchResult = pstmt.executeBatch();

            // Jika eksekusi batch berhasil, set result = 1
            if (batchResult.length > 0) {
                result = 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting batch data: " + e.getMessage(), e);
        } finally {
            closeStatement();
        }

        return result;
    }

    @Override
    public void update(DataVerifikasiModel data) {
        try {
            query = "UPDATE data_verifikasi SET hasil_muskel_kelayakan = ?, keterangan_muskel = ? WHERE id = ?";

            pstmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, data.getHasilMuskelKelayakan());
            pstmt.setString(2, data.getKeteranganMuskel());
            pstmt.setInt(3, data.getId());

            // Debug: Cetak query dan parameter
            System.out.println("Executing Query: " + query);
            System.out.println("Params: hasil_muskel_kelayakan=" + data.getHasilMuskelKelayakan() +
                               ", keterangan_muskel=" + data.getKeteranganMuskel() +
                               ", id=" + data.getId());

            int affectedRows = pstmt.executeUpdate(); // Eksekusi query

            if (affectedRows == 0) {
                throw new RuntimeException("Gagal memperbarui data, tidak ada baris yang terpengaruh.");
            }

            System.out.println("Update berhasil! " + affectedRows + " row(s) affected.");

        } catch (SQLException e) {
            e.printStackTrace(); // Cetak stack trace untuk melihat error
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
    }


    @Override
    public void updateBulk(List<DataVerifikasiModel> dataVerifikasi) {
        try {
            query = "UPDATE data_verifikasi SET hasil_muskel_kelayakan = ? WHERE id = ?";
            
            pstmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
             for (DataVerifikasiModel data : dataVerifikasi) {
                pstmt.setString(1, data.getHasilMuskelKelayakan());
                pstmt.setInt(2, data.getId());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
    }

   

}
