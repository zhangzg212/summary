    @Override
    public Object ecxelInsert(MultipartFile file) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lineCout = sheet.getLastRowNum();
        dao.delete();
        for(int i = 1; i <= lineCout; i++){
            XSSFRow row = sheet.getRow(i);
            try {
                Integer trend = null;
                String name = row.getCell(0).getStringCellValue();
                String type = row.getCell(1).getStringCellValue();
                Double ratings = row.getCell(2).getNumericCellValue();
                String trendstr = row.getCell(3).getStringCellValue();
                if("上升".equals(trendstr)){
                    trend = 1;
                }
                if("下降".equals(trendstr)){
                    trend = 0;
                }
                dao.insertratings(name , type , ratings , trend);
            }catch (Exception e){
                continue;
            }
        }
        return ReturnInfo.success();
    }

    @Override
    public Object ecxelDownload(HttpServletResponse response) throws Exception {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        String excelPath  = path.toString() + File.separator + excelName;
        File file = new File(excelPath);
        //重置response
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //解决中文文件名显示问题
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1"));
        //设置文件长度
        int fileLength = (int) file.length();
        response.setContentLength(fileLength);
        if (fileLength != 0) {
            InputStream inStream = new FileInputStream(file);
            byte[] buf = new byte[4096];
            //创建输出流
            ServletOutputStream servletOS = response.getOutputStream();
            int readLength;
            //读取文件内容并写入到response的输出流当中
            while ((readLength = inStream.read(buf)) != -1) {
                servletOS.write(buf, 0, readLength);
            }
            //关闭输入流
            inStream.close();
            //刷新输出流缓冲
            servletOS.flush();
            //关闭输出流
            servletOS.close();
            return ReturnInfo.success();
        } else {
            return ReturnInfo.error("500","下载出错");
        }
    }