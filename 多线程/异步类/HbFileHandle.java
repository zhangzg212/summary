package com.owinfo.whc.service;

import com.owinfo.solr.service.SolrService;
import com.owinfo.whc.service.dao.HbHandleDao;
import com.owinfo.whc.service.entity.GovInfoSolrEntity;
import com.owinfo.whc.service.entity.dto.AccessoryDto;
import org.dom4j.Document;
import org.dom4j.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件名：.java
 * 版权： 北京联众信安成都分公司
 * 描述： 异步处理HB文件
 * 创建时间：2018年06月26日
 * <p>
 * 〈一句话功能简述〉异步处理HB文件〈功能详细描述〉
 *
 * @author sun
 * @version [版本号, 2018年06月26日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class HbFileHandle extends Thread{

    private HbDataService hbDataService;
    private SolrService solrService;
    private HbHandleDao hbHandleDao;
    private Document document;
    private String govInfoId;

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getGovInfoId() {
        return govInfoId;
    }

    public void setGovInfoId(String govInfoId) {
        this.govInfoId = govInfoId;
    }

    public HbDataService getHbDataService() {
        return hbDataService;
    }

    public void setHbDataService(HbDataService hbDataService) {
        this.hbDataService = hbDataService;
    }

    public SolrService getSolrService() {
        return solrService;
    }

    public void setSolrService(SolrService solrService) {
        this.solrService = solrService;
    }

    public HbHandleDao getHbHandleDao() {
        return hbHandleDao;
    }

    public void setHbHandleDao(HbHandleDao hbHandleDao) {
        this.hbHandleDao = hbHandleDao;
    }

    public void run()
        {
            try{
                Element rootElt = document.getRootElement().element("ResponseData").element("BaseInfo");
                //原文
                List<Element> textElements = rootElt.element("FileList").elements("Item");
                List<AccessoryDto> accessoryList = new ArrayList<>();
                Map<String, Object> retMap = hbDataService.base64Upload(textElements,govInfoId,accessoryList);
                //解析原文为内容
                String body = hbDataService.getFileContentByPath((List<String>) retMap.get("filePathList"));
                //附件
                List<Element> fileElements = rootElt.element("AttachmentList").elements("Item");
                retMap = hbDataService.base64Upload(fileElements,govInfoId,(List<AccessoryDto>) retMap.get("accessoryList"));
                //相关材料
                List<Element> relateFileElements = rootElt.element("RelativeMaterialList").elements("Item");
                retMap = hbDataService.base64Upload(relateFileElements,govInfoId,(List<AccessoryDto>) retMap.get("accessoryList"));
                //从solr中查出该数据
                GovInfoSolrEntity govInfoSolr = solrService.queryById(govInfoId,GovInfoSolrEntity.class);
                govInfoSolr.setBody(body);
                solrService.saveBean(govInfoSolr,1000);
                //新增附件数据库记录信息
                hbHandleDao.insertHbFileByList((List<AccessoryDto>) retMap.get("accessoryList"));
                hbDataService.hbStatusUpdate(4,govInfoId,null,null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
}
