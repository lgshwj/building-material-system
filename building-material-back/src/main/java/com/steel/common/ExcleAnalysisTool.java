package com.steel.common;

import com.alibaba.druid.util.StringUtils;
import com.steel.dao.MaterialStockDao;
import com.steel.dao.QuotePriceDao;
import com.steel.dao.SupplierDao;
import com.steel.entity.Factory;
import com.steel.entity.MaterialStock;
import com.steel.entity.QuotePrice;
import com.steel.entity.Supplier;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ProjectName: building-material
 * @Package: com.steel.common
 * @ClassName: ExecleAnalysisTool
 * @Author: Hero
 * @Description: execle解析
 * @Date: 2020/4/23 20:28
 * @Version: 1.0
 */
@Component
public class ExcleAnalysisTool  {

    @Resource
    MaterialStockDao materialStockDao;

    @Resource
    QuotePriceDao quotePriceDao;

    public static ExcleAnalysisTool execleAnalysisTool;

    @PostConstruct
    public void init() {
        execleAnalysisTool = this;
        execleAnalysisTool.materialStockDao = this.materialStockDao;
        execleAnalysisTool.quotePriceDao = this.quotePriceDao;
    }
    /**
     * @Description 读取execle
     */
    public Map<String, Object> readExecleFile(MultipartFile file, List<Supplier> supplierList, List<Factory> factoryList) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String fileName = file.getOriginalFilename();//文件名字
        //获取文件名后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        // 根据后缀创建workbook
        Workbook workbook = null;
        InputStream in = file.getInputStream();
        workbook = getWorkbook(in, suffix);
        if (workbook == null) {
            return null;
        }

        List<QuotePrice> list = new LinkedList<QuotePrice>();
        Sheet sheet = workbook.getSheetAt(0);

        String msg = "";
        Integer state = 0;
        // 遍历行
        for (int i=sheet.getFirstRowNum()+1; i<sheet.getLastRowNum()+1; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            // 遍历一行的单元格
            QuotePrice quotePrice = new QuotePrice();
            String temp = "";



            if (row.getCell(0) != null) {
                quotePrice.setMaterialNumber(row.getCell(0).toString()); // 材料编号
            }
            if (row.getCell(1) != null) {
                quotePrice.setMaterialName(row.getCell(1).toString()); // 名称
            }

            if (row.getCell(2) != null) {
                quotePrice.setNorm(row.getCell(2).toString()); // 规格
            }
            if (row.getCell(3) != null) {
                quotePrice.setMaterialQuality(row.getCell(3).toString()); // 材质
            }
            if (row.getCell(4) != null) {
                quotePrice.setMaterialQuantity( new Double(row.getCell(4).getNumericCellValue()).intValue()); // 起订量
            }
            if (row.getCell(5) != null) {
                quotePrice.setMaterialUnit(row.getCell(5).toString()); // 单位
            }
            if (row.getCell(6) != null) {
                quotePrice.setMaterialPrice(row.getCell(6).getNumericCellValue()); // 单价
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (row.getCell(7) != null) {
                try {
                    quotePrice.setReleaseDate(simpleDateFormat.format(row.getCell(7).getDateCellValue())); // 发布日期
                } catch (Exception e) {
                    temp += "，发布日期格式错误";
                    state = 1;
                }
            } else {
                temp += "，发布日期为空";
                state = 1;
            }

            if (row.getCell(8) == null) {
                temp += "，经销商编号为空";
                state = 1;
            } else {
                quotePrice.setSupplierNumber(row.getCell(8).toString()); // 供应商编号
                for (Supplier supplier : supplierList) {
                    if (supplier.getSupplierNumber().equals(quotePrice.getSupplierNumber())) {
                        quotePrice.setSupplierId(supplier.getId());
                        break;
                    }
                }
                if (quotePrice.getSupplierId() == null) {
                    temp += "，经销商编号错误";
                    state = 1;
                }
            }

            if (row.getCell(9) != null) {
                quotePrice.setSupplierName(row.getCell(9).toString()); // 供应商名称
            }
            //厂家编号
            if (row.getCell(10) == null) {
                temp += "，生产商编号为空";
                state = 1;
            } else {
                quotePrice.setFactoryNumber(row.getCell(10).toString()); // 供应商编号
                for (Factory factory : factoryList) {
                    if (factory.getFactoryNumber().equals(quotePrice.getFactoryNumber())) {
                        quotePrice.setFactoryId(factory.getId());
                        break;
                    }
                }
                if (quotePrice.getFactoryId() == null) {
                    temp += "，生产商编号错误";
                    state = 1;
                }
            }

            if (row.getCell(11) != null) {
                quotePrice.setFactoryName(row.getCell(11).toString()); // 厂家名称
            }

            if (row.getCell(12) != null) {
                try {
                    quotePrice.setManufactureDate(simpleDateFormat.format(row.getCell(12).getDateCellValue())); // 发布日期
                } catch (Exception e) {
                    temp += "，生产日期格式错误";
                    state = 1;
                }
            } else {
                temp += "，生产日期为空";
                state = 1;
            }

            if (row.getCell(13) != null) {
                quotePrice.setQualityGuaranteePeriod(row.getCell(13).toString()); // 保质期
            }

            if (row.getCell(14) != null) {
                quotePrice.setOriginPlace(row.getCell(14).toString()); // 产地
            }
            int count = execleAnalysisTool.materialStockDao.checkMaterial(quotePrice.getMaterialNumber(), quotePrice.getMaterialName(), quotePrice.getNorm(), quotePrice.getMaterialQuality());
            if (count < 1) {
                temp += "，编号、名称、规格、材质不符合";
                state = 1;
            } else {
                quotePrice.setMaterialId(execleAnalysisTool.materialStockDao.searchIdByNumber(quotePrice.getMaterialNumber()));
            }

            if (state == 1) {
                temp = "<br>第"+(i+1)+"行" + temp +"</br>";
                msg += temp;
            } else {
                int convertC = execleAnalysisTool.quotePriceDao.updateQuotePrice(quotePrice.getSupplierId(), quotePrice.getMaterialId());

                list.add(quotePrice);
            }
            state = 0;
        }
        if (!StringUtils.isEmpty(msg)) {
            map.put("error", msg);
            return map;
        }
        map.put("list", list);
        return map;
    }


    public Workbook getWorkbook(InputStream inStr, String type) throws Exception {
        Workbook wb = null;
        if (type.equals(".xls")) {
            wb = new HSSFWorkbook(inStr); // 2003-
        } else if (type.equals(".xlsx")) {
            wb = new XSSFWorkbook(inStr); //2007+
        }
        return wb;
    }


}
