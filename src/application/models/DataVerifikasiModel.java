/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.models;

/**
 *
 * @author mhdja
 */
public class DataVerifikasiModel {
    private int id;
    private String name;
    private String noKk;
    private String nik;
    private String address;
    private String kelurahan;
    private String kecamatan;
    private String hasilMuskelKelayakan;
    private String keteranganMuskel;
    private int idListData;
    private String status;
    
    public DataVerifikasiModel() {
        // Konstruktor default
    }

    public DataVerifikasiModel(String name, String noKk, String nik, String address, String kelurahan, String kecamatan, String hasilMuskelKelayakan, String keteranganMuskel, String status) {
        this.name = name;
        this.noKk = noKk;
        this.nik = nik;
        this.address = address;
        this.kelurahan = kelurahan;
        this.kecamatan = kecamatan;
        this.hasilMuskelKelayakan = hasilMuskelKelayakan;
        this.keteranganMuskel = keteranganMuskel;
        this.status = status;
    }
    
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the noKk
     */
    public String getNoKk() {
        return noKk;
    }

    /**
     * @param noKk the noKk to set
     */
    public void setNoKk(String noKk) {
        this.noKk = noKk;
    }

    /**
     * @return the nik
     */
    public String getNik() {
        return nik;
    }

    /**
     * @param nik the nik to set
     */
    public void setNik(String nik) {
        this.nik = nik;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the kelurahan
     */
    public String getKelurahan() {
        return kelurahan;
    }

    /**
     * @param kelurahan the kelurahan to set
     */
    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    /**
     * @return the kecamatan
     */
    public String getKecamatan() {
        return kecamatan;
    }

    /**
     * @param kecamatan the kecamatan to set
     */
    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    /**
     * @return the hasilMuskelKelayakan
     */
    public String getHasilMuskelKelayakan() {
        return hasilMuskelKelayakan;
    }

    /**
     * @param hasilMuskelKelayakan the hasilMuskelKelayakan to set
     */
    public void setHasilMuskelKelayakan(String hasilMuskelKelayakan) {
        this.hasilMuskelKelayakan = hasilMuskelKelayakan;
    }

    /**
     * @return the keteranganMuskel
     */
    public String getKeteranganMuskel() {
        return keteranganMuskel;
    }

    /**
     * @param keteranganMuskel the keteranganMuskel to set
     */
    public void setKeteranganMuskel(String keteranganMuskel) {
        this.keteranganMuskel = keteranganMuskel;
    }

    /**
     * @return the idListData
     */
    public int getIdListData() {
        return idListData;
    }

    /**
     * @param idListData the idListData to set
     */
    public void setIdListData(int idListData) {
        this.idListData = idListData;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
