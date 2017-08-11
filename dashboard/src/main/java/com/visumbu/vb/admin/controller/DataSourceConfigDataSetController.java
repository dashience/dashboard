/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.DataSourceConfigDataSetService;
import com.visumbu.vb.bean.DataSourceConfigBean;
import com.visumbu.vb.model.DataSourceConfigDataset;
import com.visumbu.vb.model.DataSourceConfigDatasetFrequency;
import com.visumbu.vb.model.DataSourceConfigDatasetLevel;
import com.visumbu.vb.model.DataSourceConfigDatasetSegment;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author duc-dev-03
 */
@Controller
@RequestMapping("dataSourceConfigDataSet")
public class DataSourceConfigDataSetController {

    @Autowired
    private DataSourceConfigDataSetService dataSourceConfigDataSetService;

    @RequestMapping(value = "dataSourceDataSet/{dataSourceId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<DataSourceConfigDataset> getDataSourceConfigDataSet(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dataSourceId) {
        return dataSourceConfigDataSetService.getDataSourceConfigDataSet(dataSourceId);
    }

    @RequestMapping(value = "dataSourceConfigLevel/{dataSetConfigId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<DataSourceConfigDatasetLevel> getDataSourceConfigLevel(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dataSetConfigId) {
        return dataSourceConfigDataSetService.getDataSourceConfigLevel(dataSetConfigId);
    }

    @RequestMapping(value = "dataSourceConfigSegment/{dataSetConfigId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<DataSourceConfigDatasetSegment> getDataSourceConfigSegment(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dataSetConfigId) {
        return dataSourceConfigDataSetService.getDataSourceConfigSegment(dataSetConfigId);
    }

    @RequestMapping(value = "dataSourceConfigFrequency/{dataSetConfigId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<DataSourceConfigDatasetFrequency> getDataSourceConfigFrequency(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dataSetConfigId) {
        return dataSourceConfigDataSetService.getDataSourceConfigFrequency(dataSetConfigId);
    }
    
    
    //Levels
    
    @RequestMapping(value = "dataSourceConfigLevel", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSourceConfigDatasetLevel createDataSourceConfigLevel(HttpServletRequest request, HttpServletResponse response,@RequestBody DataSourceConfigBean dataSourceConfig) {
        return dataSourceConfigDataSetService.saveDataSourceConfigLevel(dataSourceConfig);
    }
    
    @RequestMapping(value = "dataSourceConfigLevel", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSourceConfigDatasetLevel updateDataSourceConfigLevel(HttpServletRequest request, HttpServletResponse response,@RequestBody DataSourceConfigBean dataSourceConfig) {
        return dataSourceConfigDataSetService.saveDataSourceConfigLevel(dataSourceConfig);
    }
    
    
    @RequestMapping(value = "dataSourceConfigLevel/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSourceConfigDatasetLevel deleteDataSourceConfigDataSetLevel(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return dataSourceConfigDataSetService.deleteDataSourceConfigDataSetLevel(id);
    }
    
    
    //Segments
    
    @RequestMapping(value = "dataSourceConfigSegment", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSourceConfigBean createDataSourceConfigSegment(HttpServletRequest request, HttpServletResponse response,@RequestBody DataSourceConfigBean dataSourceConfig) {
        return dataSourceConfigDataSetService.saveDataSourceConfigSegment(dataSourceConfig);
    }
    
    @RequestMapping(value = "dataSourceConfigSegment", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSourceConfigBean updateDataSourceConfigSegment(HttpServletRequest request, HttpServletResponse response,@RequestBody DataSourceConfigBean dataSourceConfig) {
        return dataSourceConfigDataSetService.saveDataSourceConfigSegment(dataSourceConfig);
    }
    
    @RequestMapping(value = "dataSourceConfigSegment/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSourceConfigDatasetSegment deleteDataSourceConfigDataSetSegment(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return dataSourceConfigDataSetService.deleteDataSourceConfigDataSetSegment(id);
    }
    
    //Frequencys
    
    @RequestMapping(value = "dataSourceConfigFrequency", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSourceConfigBean createDataSourceConfigFrequency(HttpServletRequest request, HttpServletResponse response,@RequestBody DataSourceConfigBean dataSourceConfig) {
        return dataSourceConfigDataSetService.saveDataSourceConfigFrequency(dataSourceConfig);
    }
    
    @RequestMapping(value = "dataSourceConfigFrequency", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSourceConfigBean updateDataSourceConfigFrequency(HttpServletRequest request, HttpServletResponse response,@RequestBody DataSourceConfigBean dataSourceConfig) {
        return dataSourceConfigDataSetService.saveDataSourceConfigFrequency(dataSourceConfig);
    }
    
    
    @RequestMapping(value = "dataSourceConfigFrequency/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSourceConfigDatasetFrequency deleteDataSourceConfigDataSetFrequency(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return dataSourceConfigDataSetService.deleteDataSourceConfigDataSetFrequency(id);
    }
    
    
    //DataSet
    
    @RequestMapping(value = "dataSourceDataSet",method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSourceConfigDataset createDataSourceConfigDataSet(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSourceConfigDataset dataSourceConfigDataset) {
        return dataSourceConfigDataSetService.createDataSourceConfigDataSet(dataSourceConfigDataset);
    }
    
    
     @RequestMapping(value = "dataSourceDataSet",method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSourceConfigDataset updateDataSourceConfigDataSet(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSourceConfigDataset dataSourceConfigDataset) {
        return dataSourceConfigDataSetService.updateDataSourceConfigDataSet(dataSourceConfigDataset);
    }
    
    @RequestMapping(value = "dataSourceDataSet/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSourceConfigDataset deleteDataSourceConfigDataSet(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return dataSourceConfigDataSetService.deleteDataSourceConfigDataSet(id);
    }
    
    
}
