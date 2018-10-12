   public Object callback(Message message) throws Exception{
        Long count = messageDao.findBySQLRequireToNumber("signals='"+message.getSignals()+"'" , Message.class);
        if(count == 0)
            messageDao.save(message);
        else
            messageDao.updateBySQLRequire("tallystatus = '"+ message.getTallystatus() +"' where signals = '"+message.getSignals()+"'" , Message.class);
        LOGGER.info("新增/修改信号源消息成功");
        int tallyStatus = messageDao.findTallyStatus(message.getSignals());
        urlinfoDao.updateBySQLRequire("tallystatus = "+tallyStatus +" where signals = '"+message.getSignals()+"'" , Urlinfo.class);
        LOGGER.info("修改推送信号源URL状态成功");
        //通过信号源地址查询该信号源别名
        List<Urlinfo> urlinfos = urlinfoDao.findSignalalias(message.getSignals());
        if(urlinfos.size()>0){
            int tallystatus = 0;
            JSONObject obj = new JSONObject();
            for(Urlinfo urlinfo : urlinfos){
                //信号源别名查询同别名的信号源tally信号状态 1点亮 0不点亮  只要一个为点亮则全部点亮
                tallystatus = urlinfoDao.findTallystatus(urlinfo.getSignalalias());
                if (tallystatus>1){
                    tallystatus = 1;
                    break;
                }
            }
            //更新所有别名下所有信号源状态
            for(Urlinfo urlinfo : urlinfos){
                obj.put(urlinfo.getSignalalias(),tallystatus);
                urlinfoDao.updateBySQLRequire("tallystatus = "+tallystatus +" where signalalias = '"+urlinfo.getSignalalias()+"'" , Urlinfo.class);
            }
            LOGGER.info("修改与推送信号源同名信号源状态成功");
            obj.put("tallyStatus",tallystatus);
            //向前端推送信息
            MyClient client = new MyClient();
            String url = "ws://localhost:"+ port + path + "/websocket" ;
            LOGGER.info("向前端推送信号源变更信息  url :" + url +" , params "+ obj.toString());
            client.start(url);
            try {
                client.sendMessage(obj.toString());
                LOGGER.info("向前端推送信号源变更信息成功");
                client.closeSocket();
            } catch (IOException e) {
                LOGGER.error("向前端推送信号源变更信息失败");
                e.printStackTrace();
                client.closeSocket();
            }
        }
        return ResultUtil.success();
    }