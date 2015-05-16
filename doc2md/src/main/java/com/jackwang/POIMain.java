package com.jackwang;

import com.jackwang.fileutils.FileOperator;
import com.jackwang.fileutils.MyConfigFactory;
import com.jackwang.xwpf.Doc2MD;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by JackWang on 2015/5/12.
 * 初始化类，程序入口
 * 功能：
 * 1）属性文件读取（输入输出以及配置文件）
 * 2）程序入口，Main启动程序
 */
public class POIMain {
    /**
     * 输入输出以及配置属性文件路径
     * 注意，是文件路径
     * 保证后缀为properties即可
     */
    private static String propsPath = "";
    /**
     * 从属性文件中读取到的属性文件集
     */
    private static HashMap<String,String> propsMap = null;
    /**
     * 初始化配置文件读取
     * 将配置属性读入HashMap中
     */
    private static boolean doc2mdInit() throws Exception {
        MyConfigFactory configFactory = MyConfigFactory.getMyConfigFactoryInstance();
        propsMap = configFactory.loadAsPropertyMap(propsPath);

        String suffix = propsMap.get("suffix");
        /**
         * TODO:目前只支持docx格式，后续实现多种后缀；
         */
//        String suffixs = propsMap.get("suffixList");
        String docFilePath = propsMap.get("docFilePath");

        if(!("".equals(suffix) || suffix == null)) {
            FileOperator.setSuffix(suffix);
        }else {
            System.out.println("后缀设置为空，将使用默认配置：docx");
        }
        if(!("".equals(docFilePath) || docFilePath == null)) {
            FileOperator.setPath(docFilePath);
        }else {
            System.out.println("输入输出路径设置为空...");
            return false;
        }
        String imgFilePath = propsMap.get("imgFilePath");
        if("".equals(imgFilePath) || imgFilePath == null) {
            System.out.println("图片写入路径将使用默认值...");
            return true;
        }else{
            FileOperator.setImgFilePath(imgFilePath);
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String execuPath = POIMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        propsPath = "H:\\06_maven_project\\poi\\doc2md\\libs\\doc2md.properties";
        execuPath += File.separator + ".." + File.separator;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = dateFormat.format(new Date());
        String logFilePath = execuPath + "log" + File.separator + dateTime + ".log";
        PrintStream printStream1 = new PrintStream(new FileOutputStream(new File(logFilePath)));
//        PrintStream printStream2 = new PrintStream(System.out);
        System.setOut(printStream1);
//        System.setOut(printStream2);
        propsPath = execuPath + "doc2md.properties";

        System.out.println("属性文件路径:" + propsPath);
        try{
            doc2mdInit();
            HashMap<String, FileInputStream> fileStreamMap = FileOperator.readFiles();
            if(!fileStreamMap.isEmpty()) {
                Doc2MD doc2MDParser = new Doc2MD(fileStreamMap);
                doc2MDParser.parseDoc();
            } else {
                System.out.println("文件集下没有文件流...");
                return ;
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
