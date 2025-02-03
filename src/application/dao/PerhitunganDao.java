/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package application.dao;

import application.models.PresentaseModel;
import application.models.RangkingModel;
import java.util.List;

/**
 *
 * @author mhdja
 */
public interface PerhitunganDao {
    public List<RangkingModel> findRangkingKelurahan();
    public List<RangkingModel> findRangkingKecamatan();
    public List<PresentaseModel> findPresentaseKelayakan();
    public List<PresentaseModel> findPresentaseKetTidakLayak();
    public List<PresentaseModel> findPresentaseKelayakanKelurahan(String kelurahan);
    public List<PresentaseModel> findPresentaseKelayakanKecamatan(String kecamatan);
    public List<PresentaseModel> findPresentaseKelayakanListData(int idListData);
}
