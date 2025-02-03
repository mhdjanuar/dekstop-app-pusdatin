/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.daoimpl;

import application.dao.RejectionNoteDao;
import application.models.RejectionNoteModel;
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
public class RejectionNoteDaoImpl implements RejectionNoteDao {
    private Connection dbConnection = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;
    private String query;
    
    public RejectionNoteDaoImpl() {
        dbConnection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<RejectionNoteModel> findAll() {
        List<RejectionNoteModel> listRejection = new ArrayList<>();
        
        try {
            query = "SELECT * FROM rejection_note";
            pstmt = dbConnection.prepareStatement(query);
            resultSet = pstmt.executeQuery();
            
            while (resultSet.next()) {
                RejectionNoteModel listData = new RejectionNoteModel();
                listData.setId(resultSet.getInt("id"));
                listData.setDescription(resultSet.getString("description"));
                // Set other attributes accordingly
                listRejection.add(listData);
            }
	} catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement();
        }
        
        return listRejection;
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
