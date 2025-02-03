/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.dao.ListDataDao;
import application.models.ListDataModel;
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
public class ListDataDaoImpl implements ListDataDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
    public ListDataDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<ListDataModel> findAll() {
        List<ListDataModel> listDataAll = new ArrayList<>();
        
        try {
            query = "SELECT * FROM list_data";
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();
            
            while (resultSet.next()) {
                ListDataModel listData = new ListDataModel();
                listData.setId(resultSet.getInt("id"));
                listData.setName(resultSet.getString("name"));
                // Set other attributes accordingly
                listDataAll.add(listData);
            }
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
        
        return listDataAll;
    }
    
    @Override
    public int create(ListDataModel listData) {
        int generatedId = -1; // Default jika gagal
        
        try {
            query = "INSERT INTO list_data(name) " +
                    "VALUES(?)";
            
            pstmt = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, listData.getName());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1); // Ambil ID yang baru dibuat
                        listData.setId(generatedId); // Set ke objek
                    }
                }
            }
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
        
         return generatedId; // Kembalikan ID baru
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
}
