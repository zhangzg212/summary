package com.owinfo.service.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by liyue on 2017/5/5.
 */

public class ExcelImport2 {

    public static ParseXMLUtil parseXmlUtil;
    public static StringBuffer errorString;
    /**当前实体类的code**/
    public static String curEntityCode;
    /**表头map对象：key:entityCode, value:headMap(index,headTitle)**/
    public static Map curEntityHeadMap ;

    /**字段的必填：key:entityCode+headTitle, value:true(必填),false(不必填)**/
    public static Map curEntityColRequired;
    //总行数
    private static int totalRows = 0;
    //总条数
    private static int totalCells = 0;

    private ExcelImport2(){}

    public static boolean validateExcel(String filePath) {
        if (filePath != null ) {
            if (filePath.endsWith("xls") || filePath.endsWith("xlsx")){
                return true;
            }
        }
        return false;
    }

    public static Map<String, Object> getExcelInfo(String fileName,MultipartFile Mfile, String[] fields) {
        Map<String, Object> excelData = new HashMap<String, Object>();
        CommonsMultipartFile cf = (CommonsMultipartFile) Mfile;
        errorString = new StringBuffer();
        File file = new File("D:/XML");
        if (!file.exists()){
            file.mkdirs();
        }
        File file1 = new File("D:/XML/" + fileName);
        try {
            cf.getFileItem().write(file1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            if (!validateExcel(fileName)) {
                return null;
            }
            boolean version = true;
            if (fileName.endsWith("xlsx")) {
                version = false;
            }
            is = new FileInputStream(file1);
            excelData = getExcelData(is, version, fields);
            file1.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excelData;
    }

    private static Map<String, Object> getExcelData(InputStream is, boolean version, String[] fields) {
        Map<String, Object> dataList = new HashMap<String , Object>();
        try {
            Workbook wb = null;
            if (version) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            System.out.println("zouni::::::"+wb);
            dataList = readExcelValue(wb, fields);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(dataList);
        return dataList;
    }

    private static Map<String, Object> readExcelValue(Workbook wb, String[] fields) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Sheet sheet = wb.getSheetAt(0);
        readSheetHeadData(sheet);
        ExcelImport2.totalRows = sheet.getPhysicalNumberOfRows();//获取物理行数
        Map headMap = (Map) getCurEntityHeadMap().get(getCurEntityCode());
        Map colMap = parseXmlUtil.getColumnMap();
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            ExcelImport2.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();//获取物理列数
        }
        if(totalRows == 0){
            System.out.println("================excel中数据为空！");
            errorString.append(ParseConstans.ERROR_EXCEL_NULL);
        }
        int xmlRowNum = colMap.size();
        if(xmlRowNum  != totalCells){
            System.out.println("==================xml列数与excel列数不相符，请检查");
            errorString.append(ParseConstans.ERROR_EXCEL_COLUMN_NOT_EQUAL);
        }
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);//行开始
            if (row == null) {
                continue;
            }
            Map curRowCellMap = new HashMap();
            for (int c = 0; c < ExcelImport2.totalCells; c++) {
                int cout =  headMap.get(c).toString().indexOf("*");
                String headTitle ="";
                if(cout == -1){
                    headTitle = headMap.get(c).toString();
                }else{
                    headTitle =  headMap.get(c).toString().substring(0, cout);
                }
                Map curColMap =  (Map) colMap.get(wb.getSheetName(0)+"_"+headTitle);//excel中的第一个sheet
                String curColCode = (String) curColMap.get("code");
                String curColType = (String) curColMap.get("type");
                Cell colCell = row.getCell(c);
                String value =getCellValue(colCell);
                if(value != null){
                    value = value.trim();
                }
                String xmlColType = (String) curColMap.get("type");
                if(xmlColType.equals("int")){
                    int   intVal = Integer.valueOf(value);
                    curRowCellMap.put(curColCode, intVal);  //将这一行的数据以code-value的形式存入map
                }else{
                    curRowCellMap.put(curColCode, value);
                }
                /*Cell cell = row.getCell(c);//遍历为每行各列赋值数据
                map.put(fields[c], getCellValue(cell));*/
                /**验证cell数据**/
                validateCellData(r+1,c+1,colCell,wb.getSheetName(0),headTitle,curColType);
            }
            dataList.add(curRowCellMap);
        }
        if(getErrorString().length() ==0){//如果没有任何错误，就保存
            resultMap.put("resultCode",1);
            resultMap.put("data",dataList);
            return resultMap;
        }else{
            //清理所有的缓存clearMap();现在暂时未清理
            List errorLists = new ArrayList();
            String[] strArr = errorString.toString().split("<br>");
            for(String s: strArr){
                errorLists.add(s);
            }
            resultMap.put("resultCode",0);
            resultMap.put("errorData",errorLists);
            return resultMap;
        }

    }
    
    /** 
     * 描述：对表格中数值进行格式化 
     * @param cell 
     * @return 
     */  
    public static  String getCellValue(Cell cell){  
        String value = null;  
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符  
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");  //日期格式化  
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字    
        switch (cell.getCellType()) {  
        case Cell.CELL_TYPE_STRING:
            value = cell.getRichStringCellValue().getString();  
            break;  
        case Cell.CELL_TYPE_NUMERIC:
            if("General".equals(cell.getCellStyle().getDataFormatString())){  //编号统一为常规
            	value = df.format(cell.getNumericCellValue());  
            }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){  
                value = sdf.format(cell.getDateCellValue());  
            }else{//金额统一设置为数值
                value = df2.format(cell.getNumericCellValue());  
            } 
            break;
        case Cell.CELL_TYPE_BOOLEAN:
            value = String.valueOf(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_FORMULA:  
        	value = String.valueOf(cell.getNumericCellValue());  
            break; 
        case Cell.CELL_TYPE_BLANK:  
            value = "";  
            break;  
        default:  
            break;  
        }  
        return value;  
    }


    /**验证单元格数据**/
    @SuppressWarnings("static-access")
    public static void validateCellData(int curRow, int curCol, Cell colCell, String entityName, String headName, String curColType){

        List rulList = (List) parseXmlUtil.getColumnRulesMap().get(entityName+"_"+headName);
        if(rulList != null && rulList.size()>0){
            for(int i=0 ; i<rulList.size() ; i++){
                Map rulM = (Map) rulList.get(i);
                String rulName = (String) rulM.get("name");
                String rulMsg = (String) rulM.get("message");
                String cellValue = null;
                if(colCell != null){
                    cellValue = getCellValue(colCell).trim();
                }
                if(rulName.equals(ParseConstans.RULE_NAME_NULLABLE)){
                    if("".equals(cellValue)||cellValue == null){
                        errorString.append("第"+curRow+"行,第"+curCol+"列:"+rulMsg+"<br>");
                    }
                }else if(rulName.equals(ParseConstans.RULE_NAME_CHINESE)){
                    if(!isChinese(cellValue)){
                        errorString.append("第"+curRow+"行,第"+curCol+"列:"+rulMsg+"<br>");
                    }
                }else if(rulName.equals(ParseConstans.RULE_NAME_NOCHINESE)){
                    if(!isLetterDigit(cellValue)){
                        errorString.append("第"+curRow+"行,第"+curCol+"列:"+rulMsg+"<br>");
                    }
                }else if(rulName.equals(ParseConstans.RULE_NAME_NUMBER)){
                    if(!isInteger(cellValue)){
                        errorString.append("第"+curRow+"行,第"+curCol+"列:"+rulMsg+"<br>");
                    }
                }else if(rulName.equals(ParseConstans.RULE_NAME_DOUBLE)){
                    try{
                        Double.valueOf(cellValue);
                    }catch(Exception e){
                        errorString.append("第"+curRow+"行,第"+curCol+"列:"+rulMsg+"<br>");
                    }

                }
            }
        }
    }

    /**读取sheet页中的表头信息**/
    @SuppressWarnings({ "unchecked", "static-access"})
    public static void readSheetHeadData(Sheet sheet){

        Map headMap = new HashMap();
        curEntityHeadMap = new HashMap();
        curEntityColRequired = new HashMap();
        Row excelheadRow = sheet.getRow(0);
        int excelLastRow = excelheadRow.getLastCellNum();
        String headTitle = "";
        for(int i=0;i<excelLastRow;i++){
            Cell cell = excelheadRow.getCell(i);
            headTitle = getCellValue(cell).trim();
            if(headTitle.endsWith("*")){
                curEntityColRequired.put(getCurEntityCode()+"_"+headTitle,true);
            }else{
                curEntityColRequired.put(getCurEntityCode()+"_"+headTitle,false);
            }
            headMap.put(i, headTitle);
        }
        curEntityHeadMap.put(getCurEntityCode(), headMap);
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isLetterDigit(String str) {
        String regex = "[\\w]+";//英文+数字+_
        return str.matches(regex);
    }

    public static boolean isChinese(String str) {
        String regex = "[\\u4e00-\\u9fa5]+";
        return str.matches(regex);
    }

    public static String getCurEntityCode() {
        return curEntityCode;
    }

    public void setCurEntityCode(String curEntityCode) {
        this.curEntityCode = curEntityCode;
    }

    public static Map getCurEntityHeadMap() {
        return curEntityHeadMap;
    }

    public void setCurEntityHeadMap(Map curEntityHeadMap) {
        this.curEntityHeadMap = curEntityHeadMap;
    }

    public Map getCurEntityColRequired() {
        return curEntityColRequired;
    }

    public void setCurEntityColRequired(Map curEntityColRequired) {
        this.curEntityColRequired = curEntityColRequired;
    }

    public ParseXMLUtil getParseXmlUtil() {
        return parseXmlUtil;
    }

    public void setParseXmlUtil(ParseXMLUtil parseXmlUtil) {
        this.parseXmlUtil = parseXmlUtil;
    }

    public static StringBuffer getErrorString() {
        return errorString;
    }

    public void setErrorString(StringBuffer errorString) {
        this.errorString = errorString;
    }
}
