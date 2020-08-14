package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.entity.QuotePrice;
import com.steel.service.QuotePriceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

/**
 * (QuotePrice)表控制层
 *
 * @author makejava
 * @since 2020-04-19 18:07:56
 */
@Slf4j
@RestController
@RequestMapping("/quotePrice")
@CrossOrigin
public class QuotePriceController {
    /**
     * 服务对象
     */
    @Resource
    private QuotePriceService quotePriceService;


    @VerifyToken
    @GetMapping("/queryQuotePrice")
    public Result queryQuotePrice(String norm, String materialQuality, String factoryName, Integer supplierId, String materialName, String releaseStartTime, String releaseEndTime, Integer state, Integer pageIndex, Integer pageSize) {
        return this.quotePriceService.queryQuotePrice(norm, materialQuality, factoryName, supplierId, materialName, releaseStartTime, releaseEndTime, state, pageIndex, pageSize);
    }

    @VerifyToken
    @GetMapping("/delQuotePrice")
    public Result delQuotePrice(Integer[] ids) {
        return this.quotePriceService.deleteById(ids);
    }

    @VerifyToken
    @PostMapping("/downloadQuotePriceTemp")
    public Result downloadQuotePriceTemp(HttpServletResponse response) {
        try {
            //创建文件
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("quotePrice");

            XSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.LEFT);//文字居左
            style.setVerticalAlignment(VerticalAlignment.BOTTOM);// 设置单元格垂直方向对齐方式
            style.setBorderBottom(BorderStyle.THIN);//下边框
            style.setBorderLeft(BorderStyle.THIN);//左边框
            style.setBorderRight(BorderStyle.THIN);//右边框
            style.setBorderTop(BorderStyle.THIN); //上边框

            // 创建表头 创建一行
            XSSFRow rowHeader = sheet.createRow(0);
            // 创建12个单元格
            for (int i=0 ; i<15 ; i++) {
                rowHeader.createCell(i).setCellStyle(style);
            }
            //填充内容
            XSSFRow row = sheet.getRow(0);
            row.getCell(0).setCellValue("材料编号");
            row.getCell(1).setCellValue("材料名称");
            row.getCell(2).setCellValue("规格");
            row.getCell(3).setCellValue("材质");
            row.getCell(4).setCellValue("起订量");
            row.getCell(5).setCellValue("材料单位");
            row.getCell(6).setCellValue("单价/元");
            row.getCell(7).setCellValue("发布日期");
            row.getCell(8).setCellValue("经销商编号");
            row.getCell(9).setCellValue("经销商");
            row.getCell(10).setCellValue("生产商编号");
            row.getCell(11).setCellValue("生产商");
            row.getCell(12).setCellValue("生产日期");
            row.getCell(13).setCellValue("保质期");
            row.getCell(14).setCellValue("产地");

            //设置Header并且输出文件
            String fileName = "报价信息模板.xlsx";
            response.setHeader("Content-type","application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            //设置响应头部，以及文件名进行中文防止乱码转码操作
            response.setHeader("Content-Disposition","attachment;filename="+ fileName);
            wb.write(response.getOutputStream());
            wb.close();
            return new Result("1001", "下载模板成功");
        } catch (Exception e) {
            log.error("下载模板错误",e);
            return new Result("2001", "下载模板失败");
        }
    }

    @PostMapping("/uploadQuotePriceData")
    public Result importQuotePrice(@RequestParam("file")MultipartFile file) throws Exception{
        return this.quotePriceService.uploadQuotePrice(file);
    }

    @PostMapping("/searchSupplierByMaterialId")
    @VerifyToken
    public Result searchSupplierByMaterialId(Integer materialId) {
        return this.quotePriceService.searchSupplierByMaterialId(materialId);
    }

    @GetMapping("/analysisQuotePrice")
    public Result analysisQuotePrice(Integer materialId, String startDate, String endDate) throws Exception {
        return this.quotePriceService.analysisQuotePrice(materialId, startDate, endDate);
    }
}

