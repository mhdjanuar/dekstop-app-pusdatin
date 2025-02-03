/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package application.dao;

import application.models.ListDataModel;
import java.util.List;

/**
 *
 * @author mhdja
 */
public interface ListDataDao {
    public List<ListDataModel> findAll();
    public int create(ListDataModel listData);
}
