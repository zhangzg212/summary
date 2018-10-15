 //高速争议处理导入
    @RequiresPermissions(value = {"ltdisputehandle:import"})
    @RequestMapping("/importDisputeExcel")
    public Map<String, Object> importDisputeExcel(@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        //如果是xls，使用HSSFWorkbook；如果是xlsx，使用XSSFWorkbook
        logger.info("<==  接收文件成功!");
        List<String> clearDateList = new ArrayList<>();
        List<DisputeRecordInside> dataList = new ArrayList<DisputeRecordInside>();
        Map<String, Object> requestMap = new HashMap<>();
        int rowNum = 0;
        String name = file.getOriginalFilename();
        String type = name.substring(name.lastIndexOf("."),name.length());
        logger.info("请求文件类型:" + type);
        if(".xlsx".equals(type)){
            /*if (name.endsWith("xls")) {
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
                HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
                rowNum = sheet.getLastRowNum();
                for (int i = 1; i <= rowNum; i++) {
                    HSSFRow hssfRow = sheet.getRow(i);
                    //钱包交易系列号
                    HSSFCell etcTradNo = hssfRow.getCell(9);
                    String etcTrad = etcTradNo.toString();
                    if(etcTrad.contains(".")){
                        etcTrad = etcTrad.substring(0,etcTrad.indexOf("."));
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //ETC卡号
                    HSSFCell etcCardId = hssfRow.getCell(0);
                    HSSFCell laneId = hssfRow.getCell(hssfRow.getLastCellNum() - 5);
                    HSSFCell clearDate = hssfRow.getCell(hssfRow.getLastCellNum() - 6);
                    DisputeRecordInside disputeRecordInside = new DisputeRecordInside();
                    disputeRecordInside.setEtcTradNo(etcTrad);
                    disputeRecordInside.setEtcCardId(etcCardId.toString());
                    disputeRecordInside.setLaneId(laneId.toString());
                    String clearDat = clearDate.toString().replaceAll("年","-").replaceAll("月","-").replaceAll("日","");
                    clearDateList.add(clearDat);
                    if(!"".equals(etcTrad) && !"".equals(etcCardId)){
                        HSSFCell result = hssfRow.getCell(hssfRow.getLastCellNum() - 1);
                        if("确认记账".equals(result.toString())){
                            disputeRecordInside.setDisputeStatus("3");
                        }
                        if("坏账交易".equals(result.toString())){
                            disputeRecordInside.setDisputeStatus("2");
                        }
                        disputeRecordInside.setClearDate(ClearingUtil.getAfterDate(new Date()));
                        disputeRecordInside.setUpdateBy("ExcelImprot" + ""+ UserSessionUtil.getUserName());
                        disputeRecordInside.setUpdateTime(new Date());
                        dataList.add(disputeRecordInside);
                    }
                }
            }*/
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFCell handleResult =  sheet.getRow(0).getCell(sheet.getRow(0).getLastCellNum() - 2);
            rowNum = sheet.getLastRowNum();
            if(!"处理方式".equals(handleResult.toString())){
                logger.info("该文件没有处理结果,不能进行操作,请检查!");
                return ReturnResult.error("该文件没有处理结果,不能进行操作,请检查!");
            }
            for (int i = 1; i <= rowNum; i++) {
                XSSFRow hssfRow = sheet.getRow(i);
                //获取主键ID
                XSSFCell id = hssfRow.getCell(0);
                XSSFCell clearDate = hssfRow.getCell(6);
                DisputeRecordInside disputeRecordInside = new DisputeRecordInside();
                disputeRecordInside.setId(id.toString());
                clearDateList.add(clearDate.toString());
                if(!"".equals(id.toString())){
                    XSSFCell result = hssfRow.getCell(hssfRow.getLastCellNum() - 2);
                    if("".equals(result.toString())){
                        logger.info("该文件没有处理结果,不能进行操作,请检查!");
                        return ReturnResult.error("该文件没有处理结果,不能进行操作,请检查!");
                    }
                    if("确认记账".equals(result.toString())){
                        disputeRecordInside.setDisputeStatus("3");
                    }else if("坏账交易".equals(result.toString())){
                        disputeRecordInside.setDisputeStatus("2");
                    }else{
                        logger.info("处理结果只能为确认记账、坏账交易,第"+i+"条数据有错,请修改后重试!");
                        return ReturnResult.error("处理结果只能为确认记账、坏账交易,第"+i+"条数据有错,请修改后重试!");
                    }
                    disputeRecordInside.setUpdateBy("ExcelImprot" + ""+ UserSessionUtil.getUserName());
                    dataList.add(disputeRecordInside);
                }
            }
            requestMap.put("data",dataList);
            Map<String, Object> res = motorwayService.importDisputeExcel(requestMap);
            if( res != null && "1".equals(res.get("code"))){
                logger.info("处理文件完成,更新了" + res.get("data").toString() + "条数据");
                Map<String, Object> params = new HashedMap();
                params.put("data",clearDateList);
                //调用异步统计接口
                motorwayService.updateAfterExcelImport(params);
            }
            return res;
        }else{
            logger.info("请求文件类型不符");
            return ReturnResult.error("请导入xlsx类型的文件");
        }
    }
