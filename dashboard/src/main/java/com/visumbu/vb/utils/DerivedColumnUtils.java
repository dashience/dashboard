/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.admin.service.GaService;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.model.DatasetColumns;
import static com.visumbu.vb.utils.ShuntingYard.postfix;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author deeta1
 */
public class DerivedColumnUtils {

    @Autowired
    private GaService gaService;
    
    @Autowired
    private UiDao uiDao;

    
}
