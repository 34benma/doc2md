package com.jackwang.xwpf;

import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by JackWang on 2015/5/12.
 * 核心类 将doc文件转换为md文件
 */
public class Doc2MD {
    /**
     * 输入单个文件时的文件流
     */
    private FileInputStream inputDoc = null;
    /**
     * 输入文件集时的文件流
     * Key：文件名字，去除了后缀名
     * Value：文件流
     */
    private HashMap<String,FileInputStream> inputDocMap = null;
    /**
     * 读到的文件流
     */
    private XWPFDocument document = null;
    /**
     * 当前文档中所有非空段落行；
     */
    private List<XWPFParagraph> paragraphList = null;
    /**
     * 当前文档中所有非空表格对象
     */
    private List<XWPFTable> tableList = null;
    /**
     * 当前文档中所有非空图片数目
     */
    private List<XWPFPicture> pictureList = null;
    /**
     * 当前文档中所有超链接
     */
    private XWPFHyperlink[] hyperlinks = null;

    /**
     * 多个文件流构造函数
     * @param inputDocMap 多个文件流的Map
     */
    public Doc2MD(HashMap<String, FileInputStream> inputDocMap) {
        this.inputDocMap = inputDocMap;
    }
    /**
     * 单个文件流构造函数
     * @param inputDoc
     */
    public Doc2MD(FileInputStream inputDoc) {
        this.inputDoc = inputDoc;
    }

    /**
     * 读取文档流
     * @param stream
     * @return
     * @throws Exception
     */
    private XWPFDocument readDocument(FileInputStream stream) throws Exception{
        return new XWPFDocument(stream);
    }

    /**
     * 计算当前文档中所有非空行
     * @return
     */
    private int countObjects() {
        return paragraphList.size() + tableList.size() ;
    }

    public boolean parseDoc() throws Exception {
        Set<String> keySet = inputDocMap.keySet();
        for(String title: keySet) {
            document = readDocument(inputDocMap.get(title));
            paragraphList = document.getParagraphs();
            tableList = document.getTables();
            System.out.println(title + " Creating... ");
            FileOutputStream fileOut = new FileOutputStream(new File(title));
            String[] temp2Write = new String[countObjects()];

        }
        return true;
    }

    private List<XWPFParagraph> getNotNullParas() {
        List<XWPFParagraph> tempParags = document.getParagraphs();
        List<XWPFParagraph> paragraphs = new ArrayList<XWPFParagraph>();
        for(XWPFParagraph paragraph : tempParags) {
            if(!("".equals(paragraph.getText()) || null == paragraph.getText())) {
                paragraphs.add(paragraph);
            }
        }
        return paragraphs;
    }


}
