package com.owinfo.whc.controller;

import com.owinfo.solr.service.SolrService;
import com.owinfo.whc.hb.IMobileOAWcfService;
import com.owinfo.whc.service.HbDataService;
import com.owinfo.whc.service.dao.GovInfoDao;
import com.owinfo.whc.service.dao.HbHandleDao;
import com.owinfo.whc.service.entity.*;
import com.owinfo.whc.service.HbFileHandle;
import com.owinfo.whc.util.BeanToMapUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * <p>
 *
 *     接收HB系统推送的消息
 *          已办 待办
 *
 *
 * @author liyue
 * @version v1
 * @create 2018-06-07 11:19:23
 * @copyright 北京联众信安成都分公司
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private GovInfoDao govInfoDao;
    @Autowired
    private HbHandleDao hbHandleDao;
    @Autowired
    private SolrService solrService;
    @Autowired
    private HbDataService hbDataService;
    @Value("${hb.message.messageDetail}")
    private String messageDetail;

    /**
     * 待办消息接收
     * @param
     */
    @RequestMapping("/hbInfo")
    @Async
    public void unfinished(@RequestBody GovUnfinishedItem[] items) throws Exception {
        logger.info("HB数据开始新增传入参数 params = "+ Arrays.asList(items));
        //消息入库
        hbHandleDao.hbDataPreInsert(Arrays.asList(items));
        //对同一个稿件通过resourceId去重
        List<GovUnfinishedItem> itemList = Stream.of(items).filter(BeanToMapUtil.distinctByKey(GovUnfinishedItem::getResourceId)).collect(Collectors.toList());
        logger.info("HB数据新增传入参数去重后结果 params = "+ itemList);
        handleHbData(itemList,0);
    }

    /**
     * 已办消息接收
     * @param items
     */
    @RequestMapping("/finished")
    public int finished(@RequestBody GovUnfinishedItem[] items) {
        logger.info("HB数据开始新增传入参数 params = "+ Arrays.asList(items));
        List<GovUnfinishedItem> collect = Stream.of(items).filter(BeanToMapUtil.distinctByKey(GovUnfinishedItem::getResourceId)).collect(Collectors.toList());
        return 1;
    }

    // flag 0未获取到详情  1获取到了详情
    public void handleHbData(List<GovUnfinishedItem> itemList,int flag){
        List<Integer> operationList = new ArrayList<>();
        for (GovUnfinishedItem item : itemList){
            try{
                if(item.getOperation() == 4){
                    //删除该条消息
                    Map<String, Object> map = new HashMap<>();
                    map.put("govInfoId",item.getResourceId());
                    hbDataService.hbDataDelete(map);
                }else if(item.getOperation() != 0){
                    GovInfoEntity info = new GovInfoEntity();
                    info.setGovInfoId(item.getResourceId());
                    info.setTitle(item.getTitle());
                    info.setCreateTime(BeanToMapUtil.fmtHbTimeTwo(item.getDeliverTime()));
                    info.setUpdateTime(BeanToMapUtil.fmtHbTimeTwo(item.getLastModifyTime()));
                    info.setHbStauts(1);
                    info.setBody("数据可能过大,仅在solr存储");
                    switch (item.getApplicationName()) {
                        case "HIDEALSW" : info.setGovType("100200201"); break;
                        case "HIDEALFW" : info.setGovType("100200202"); break;
                        case "HIDEALNOTICE" : info.setGovType("100200203"); break;
                        default : continue;
                    }
                    switch (item.getEmergency()) {
                        case "0" : info.setHbEmergency("普通"); break;
                        case "1" : info.setHbEmergency("急件"); break;
                        case "2" : info.setHbEmergency("平急"); break;
                        case "3" : info.setHbEmergency("加急"); break;
                        case "4" : info.setHbEmergency("特急"); break;
                        case "5" : info.setHbEmergency("特提"); break;
                        default : info.setHbEmergency("未知"); break;
                    }
                    switch (item.getType()) {
                        case "task" : info.setHbInfoType("待办"); break;
                        case "notice" : info.setHbEmergency("通知"); break;
                        case "focus" : info.setHbEmergency("关注"); break;
                        case "issue" : info.setHbEmergency("待阅"); break;
                        case "finish" : info.setHbEmergency("已办"); break;
                        case "reading" : info.setHbEmergency("必阅"); break;
                        case "readed" : info.setHbEmergency("已阅"); break;
                        default : info.setHbEmergency("未知"); break;
                    }
                    String detailXmlStr = "";
                    if(flag == 0){
                        //获取附件信息 主办协办 消息主体内容信息
                        IMobileOAWcfService hbService = hbDataService.getHbService();
                        //获取消息详情
                        detailXmlStr = hbService.resourceDetail(item.getUserGuid(),item.getResourceId(),item.getActivityId(),item.getApplicationName());
                    }else if(flag == 1){
                        detailXmlStr = item.getDetailXml();
                    }
                    Document document = DocumentHelper.parseText(detailXmlStr);
                    String responseCode = document.getRootElement().element("ResponseCode").getTextTrim();
                    if("0".equals(responseCode)){
                        logger.info("调用HB详情接口成功返回结果 result = " + detailXmlStr);
                        //修改消息入库数据 详情XML字段
                        hbDataService.hbStatusUpdate(2,item.getResourceId(),detailXmlStr,null);
                    }else if("1".equals(responseCode)){
                        logger.info("调用HB详情接口成功,但未拿到数据 result = " + detailXmlStr);
                        hbDataService.hbStatusUpdate(1,item.getResourceId(),detailXmlStr,null);
                        continue;
                    }
                    Map<String, Object> rangeMap = hbDataService.getHbMainAndHelp(document,item.getUserGuid());
                    rangeMap.put("labelStrList",hbDataService.getLabels());
                    info.setUserId(String.valueOf(rangeMap.get("cUserId")));
                    info.setCreateBy(String.valueOf(rangeMap.get("cUserId")));
                    info.setDeptId(String.valueOf(rangeMap.get("cDeptId")));
                    //新增政务信息----需要单条新增 控制事务
                    hbDataService.hbDataInsert(info,rangeMap);
                    handleHbFile(document,item);
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.info("HB数据处理异常 params = "+ item.toString());
                logger.error("HB数据操作 Operation："+item.getOperation()+" 出错 govInfoId/resourceId = "+item.getResourceId());
                operationList.add(item.getOperation());
                hbDataService.hbStatusUpdate(null,item.getResourceId(),null,e.toString());
                continue;
            }
        }

    }

    public void handleHbFile(Document document,GovUnfinishedItem item){
        //调用异步处理文件方法
        HbFileHandle fileThread = new HbFileHandle();
        fileThread.setDocument(document);
        fileThread.setGovInfoId(item.getResourceId());
        fileThread.setHbDataService(hbDataService);
        fileThread.setSolrService(solrService);
        fileThread.setHbHandleDao(hbHandleDao);
        fileThread.start();
    }

    //未获取到所需详情hb_data_status 0、1 数据处理
    @RequestMapping("/noDetailHb")
    @Scheduled(cron = "0 05,15 00 * * ?")
    public int noDetailHb(){
        List<GovUnfinishedItem> list = hbHandleDao.findNoDetailHb();
        handleHbData(list,0);
        return 1;
    }

    //获取到所需详情,新增政务出错hb_data_status 2 数据处理
    @RequestMapping("/noGovInfoHb")
    @Scheduled(cron = "0 25,35 00 * * ?")
    public int noGovInfoHb(){
        List<GovUnfinishedItem> list = hbHandleDao.findNoGovInfoHb();
        handleHbData(list,1);
        return 1;
    }

    //新增政务成功,附件处理失败 hb_data_status 3  数据处理
    @RequestMapping("/noFileHb")
    @Scheduled(cron = "0 35,45 00 * * ?")
    public int noFileHb() throws DocumentException {
        List<GovUnfinishedItem> list = hbHandleDao.findNoFileHb();
        for (GovUnfinishedItem item : list){
            Document document = DocumentHelper.parseText(item.getDetailXml());
            handleHbFile(document,item);
        }
        return 1;
    }

    //移动处理完成的HB消息
    @RequestMapping("/moveFinishHb")
    @Scheduled(cron = "0 58 23 * * ?")
    public void handleNormalHb(){
        hbDataService.handleNormalHb();
    }
}
