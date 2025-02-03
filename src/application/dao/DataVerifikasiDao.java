/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package application.dao;

import application.models.DataVerifikasiModel;
import java.util.List;

/**
 *
 * @author mhdja
 */
public interface DataVerifikasiDao {
    public List<DataVerifikasiModel> findAll();
    public List<DataVerifikasiModel> findByIdListData(int idListData);
    public List<DataVerifikasiModel> findByFilters(Integer idListData, String kelurahan, String kecamatan, String noKk);
    public List<DataVerifikasiModel> findKelurahan();
    public List<DataVerifikasiModel> findKecamatan();
    public void update(DataVerifikasiModel dataVerifikasi);
    public int createBulk(List<DataVerifikasiModel> dataVerifikasi, int id);
    public void updateBulk(List<DataVerifikasiModel> dataVerifikasi);
}
