package com.sobey.util;

import com.sobey.entity.HotNews;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    public static DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Object readExcel(String path,int sheetAt) throws IOException {
        LOGGER.info("读入excel地址: " + path + "...");
        InputStream is = new FileInputStream(path);
        String type = path.substring(path.lastIndexOf("."),path.length());
        LOGGER.info("文件类型:" + type);
        List<HotNews> interNewsList = new ArrayList<>();
        int sheetNum = 0;
        if(".xlsx".equals(type)){
            try{
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
                //sheet页数目
                sheetNum = xssfWorkbook.getNumberOfSheets();
                //读取的sheet页码索引
                XSSFSheet sheet = xssfWorkbook.getSheetAt(sheetAt);
                int rowNum = sheet.getLastRowNum();
                for (int i = 1; i <= rowNum; i++) {
                    XSSFRow hssfRow = sheet.getRow(i);
                    HotNews hotNews = new HotNews();
                    XSSFCell title = hssfRow.getCell(0);
                    if(title != null && !"".equals(title.toString())){
                        hotNews.setTitle(title.toString());
                        Date time = hssfRow.getCell(1).getDateCellValue();
                        hotNews.setDt(formater.format(time));
                        hotNews.setProvince(hssfRow.getCell(2).toString());
                        hotNews.setDocCount((int)hssfRow.getCell(3).getNumericCellValue());
                        String str = hssfRow.getCell(4).toString();
                        hotNews.setAddress(str.substring(0,str.indexOf(".")));
                        interNewsList.add(hotNews);
                    }
                }
                LOGGER.info("读取excel内容完成!");
                return ResultInfo.success(interNewsList,sheetNum);
            }catch (Exception e){
                e.printStackTrace();
                LOGGER.error("读取excel内容出错!");
                return ResultInfo.error("500","读取excel内容出错");
            }
        }else{
            LOGGER.error("文件类型不符!");
            return ResultInfo.error("500","请导入xlsx类型的文件");
        }
    }

   /* public static void main(String[] args) throws IOException, ParseException {
        readExcel("D://互联网线索.xlsx");
    }*/
}
