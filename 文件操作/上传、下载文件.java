 //京东对账文件导入
    @RequestMapping("/importJdCSV")
    public Map<String, Object> importJdCSV(@RequestParam("file") MultipartFile file) throws IOException, ParseException, InterruptedException {
        logger.info("<==  接收文件成功!");
        Map<String, Object> params = new HashedMap();
        //params.put("createBy", UserSessionUtil.getUserNo());
        //params.put("updateBy", UserSessionUtil.getUserNo());
        params.put("createBy", "123456");
        params.put("updateBy", "123456");
        String name = file.getOriginalFilename();
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1);//文件类型
        if(!"csv".equals(fileType)){
            return ReturnResult.error("请导入csv格式的文件");
        }
        String dirs = RECHARGE_JD;
        String filePath = dirs + File.separator + fileName;
        logger.info("保存路径： " + filePath);
        File dir = new File(dirs);
        OwinfoBeanUtils.delAllFile(dirs); //删除完里面所有内容
        dir.mkdirs();
        try {
            file.transferTo(new File(filePath));   // 转存文件
            logger.info("转存文件成功,开始读取文件数据");
            BufferedReader reader = new BufferedReader(new FileReader(filePath));//换成你的文件名
            reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉
            String line = null;
            String tradeDate = "";
            List<OnlineRechargeDetail> onlineRechargeDetailList = new ArrayList<>();
            while((line=reader.readLine())!=null){
                OnlineRechargeDetail onlineRechargeDetail = new OnlineRechargeDetail();
                String[] item = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                onlineRechargeDetail.setTransId(item[0]);
                onlineRechargeDetail.setTradeAmount(Long.parseLong(item[10]));
                onlineRechargeDetail.setTradeTime(item[6]);
                tradeDate = item[6];
                onlineRechargeDetail.setSpare(item[14]);
                //onlineRechargeDetail.setCreateBy(UserSessionUtil.getUserNo());
                //onlineRechargeDetail.setUpdateBy(UserSessionUtil.getUserNo());
                onlineRechargeDetail.setCreateBy("123456");
                onlineRechargeDetail.setUpdateBy("123456");
                onlineRechargeDetailList.add(onlineRechargeDetail);
            }
            logger.info("读取文件数据完成,开始插入数据");
            if(onlineRechargeDetailList == null){
                return ReturnResult.error("文件内容为空,请检查后重新导入");
            }
            params.put("list",onlineRechargeDetailList);
            tradeDate  = tradeDate.replace("/","-");
            tradeDate = OwinfoBeanUtils.formatDateStr(tradeDate);
            params.put("tradeDate",tradeDate);
            logger.info("开始插入JD在线充值数据  params = " + params);
            //调用在线充值详情数据接口(异步)
            onlineRechargeService.jdListAdd(params);
            return ReturnResult.success("接收文件成功,正在处理数据请稍等两分钟");
        } catch (IOException e) {
            e.printStackTrace();
            return ReturnResult.error("上传文件出错,请重试");
        }
    }
	
 //京东模板下载导出
    @RequestMapping(value = "/modelExport")
    public Map<String, Object> modelExport(@RequestParam Map<String, Object> param, HttpServletResponse response) throws IOException {
        Map<String, Object> tempMap = new HashMap<String, Object>();
        if(param == null || param.get("type") == null ||"".equals(String.valueOf(param.get("type")))){
            tempMap.put("ResultCode", 0);
            tempMap.put("msg", "获取模板类型为空");
            return tempMap;
        }
        String filePath = RECHARGE_MODEL + File.separator;
        String fileName = "";
        if("京东".equals(String.valueOf(param.get("type")))){
            fileName = "京东充值模板.csv";
            filePath = filePath + fileName;
        }
        File file = new File(filePath);
        //重置response
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        //解决中文文件名显示问题
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
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
            tempMap.put("ResultCode", 1);
            tempMap.put("msg", "下载成功");
        } else {
            tempMap.put("ResultCode", 0);
            tempMap.put("msg", "下载出错");
        }
        return tempMap;
    }
	