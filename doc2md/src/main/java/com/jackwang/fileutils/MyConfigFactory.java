package com.jackwang.fileutils;

/**
 * Created by Cheng on 2015/2/1.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.SequenceInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 配置文件配置类<br />
 * 单例模式<br />
 * 用户只需要一次初始化该工厂类，一次性倒入所有属性文件集<br />
 * 后续可以通过@function setPropsPath 和 @function loadAsProperty 或 loadAsPropertyMap 方法获取<br />
 * 功能：<br />
 *  1） 输入单个配置文件路径，获得单个配置文件集<br />
 *  2） 输入配置文件夹路径，获得该配置文件夹下所有配置文件<br />
 *  3） 获取有关配置文件属性等，比如成功获取的配置文件数目，配置文件路径等<br />
 *  输出：<br />
 *  获得配置文件属性集<br />
 *  返回相应的错误码<br />
 */
public class MyConfigFactory {
    //配置工厂配置文件路径
    @Deprecated
    private String factoryPropsPath = null;
    //配置工厂配置文件路径是否使用默认值
    @Deprecated
    private boolean isFactoryPropsDefault = false;
    //用户配置文件夹路径
    private String PropsFilesPath = null;
    //用户配置文件路径
    private String PropsPath = null;
    //用户的配置文件数目
    private int PropsCount = 0;
    //设置属性文件数目时，需要加锁
    private Lock lock = null;

    /**
     * Step 1 私有构造函数，实现单例模式<br />
     */
    private MyConfigFactory() {
        factoryPropsPath = "";
        isFactoryPropsDefault = false;
        PropsFilesPath = "";
        PropsPath = "";
        PropsCount = 0;
        lock = new ReentrantLock(true);
    }

    /**
     * Step 2 实现私有化静态Fianl实例类<br />
     */
    private static final class MyConfigFactoryInstance {
        private static final MyConfigFactory instance = new MyConfigFactory();
    }

    /**
     * 功能：获取MyConfigFactory实例<br />
     * 权限：public<br />
     * 先行方法：无<br />
     * Step 3 提供接口，获取实例<br />
     * @return MyConfigFactory 的单例<br />
     */
    public static MyConfigFactory getMyConfigFactoryInstance () {
        return MyConfigFactoryInstance.instance;
    }

    /**
     * 获取配置工厂配置文件路径，如果用户没有配置，则返回默认路径<br />
     * 权限: public<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @return 配置文件路径<br />
     * @return “-1”如果发生系统错误，路径读取失败<br />
     * @deprecated 该方法暂时废弃，实际意义不大<br />
     */
    @Deprecated
    public String getFactoryPropsPath() {
        if (null == factoryPropsPath || "".equals(factoryPropsPath)) {
            isFactoryPropsDefault = true;
            return getDefaultFactoryPropsPath();
        }
        isFactoryPropsDefault = false;
        return factoryPropsPath;
    }

    /**
     * 配置工厂配置文件默认路径<br />
     * 权限: private<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @return 正常返回默认路径<br />
     * @return "-1" 发生系统错误，路径读取失败<br />
     */
    @Deprecated
    private String getDefaultFactoryPropsPath() {
        String fileRootPath = Class.class.getClass().getResource("/").getPath() + "MyConfig.properties";
        File propsFile = new File(fileRootPath);

        if (! propsFile.exists()) {
            return "-1";
        }
        isFactoryPropsDefault = true;
        factoryPropsPath = fileRootPath;
        return factoryPropsPath;
    }

    /**
     * 手动设置配置工厂配置文件路径<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @param factoryPropsPath 用户自定义的配置文件路径<br />
     * @throws Exception 如果用户传入了非法参数，默认默认值<br />
     * @deprecated 该方法暂时废弃，实际以及不大<br />
     */
    @Deprecated
    public void setFactoryPropsPath(String factoryPropsPath) {
        if ("".equals(factoryPropsPath) || null == factoryPropsPath) {
            isFactoryPropsDefault = true;
        }
        if (!factoryPropsPath.endsWith(".properties")) {
            isFactoryPropsDefault = true;
        }

        File propsFile = new File(factoryPropsPath);

        if (!propsFile.exists()) {
            isFactoryPropsDefault =true;
        }
        isFactoryPropsDefault = false;
        this.factoryPropsPath = factoryPropsPath;
    }

    /**
     * 获取用户属性文件集文件夹路径<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @return 成功返回用户设置的属性文件集文件夹路径<br />
     */
    public String getPropsFilesPath() {

        return PropsFilesPath;
    }

    /**
     * 设置用户属性文件集文件夹路径<br />
     * 权限:public<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @param propsFilesPath 用户自定义设置的属性文件集文件夹路径<br />
     * @warning 不能动态设置<br />
     * @throws Exception 用户传入的参数有误<br />
     */
    public void setPropsFilesPath(String propsFilesPath) throws Exception{
        if (null == propsFilesPath || "".equals(propsFilesPath)) {
            throw new Exception("属性文件集文件夹路径不能为空");
        }
        File file = new File(propsFilesPath);
        if (!file.exists()) {
            throw new Exception("属性文件集文件夹不存在");
        }
        if (!file.isDirectory()) {
            throw new Exception("属性文件集文件夹非法");
        }
        PropsFilesPath = propsFilesPath;
    }

    /**
     * 功能：获取单个属性文件路径<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @return 属性文件路径<br />
     */
    public String getPropsPath() {
        return PropsPath;
    }

    /**
     * 功能：设置单个属性文件路径<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @param propsPath 用户自定义的属性文件路径<br />
     * @warning 不能动态设置<br />
     * @throws Exception 用户传入的属性文件路径非法<br />
     */
    public void setPropsPath(String propsPath) throws Exception {
        if (null == propsPath || "".equals(propsPath)) {
            throw new Exception("属性文件路径不能为空");
        }
        if (!propsPath.endsWith(".properties")) {
            throw new Exception("属性文件格式错误，必须是Properties文件");
        }
        File file = new File(propsPath);
        if (!file.exists()) {
            throw new Exception("属性文件路径非法，不存在");
        }
        if (!file.isFile()) {
            throw new Exception("属性文件非法！");
        }
        PropsPath = propsPath;
    }

    /**
     *功能：获取当前读取到的属性文件数目<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @return  当前读取到的属性文件数目<br />
     */
    public int getPropsCount() {
        return PropsCount;
    }

    /**
     * 功能：根据用户通过@function setPropsPath设置的属性文件路径获取属性文件<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance setPropsPath<br />
     * @return 成功返回用户的属性文件Properties<br />
     * @warning 注意区分@function loadAsProperties 本方法导入的是单个属性文件<br />
     * @throws Exception 导入属性文件出错<br />
     */
    public Properties loadAsProperty() throws Exception{
        if (null == PropsPath || "".equals(PropsPath)) {
            throw new Exception("属性文件路径错误...");
        }

        PropsPath = getPropsPath();
        File file = new File(PropsPath);
        //该处无需再次校验属性文件路径的合法性，因为该方法必须保证路径设置正确并且文件合法
        Properties props = new Properties();

        props.load(new FileInputStream(file));
        lock.lock();
        PropsCount++;
        lock.unlock();
        return props;
    }

    /**
     *功能：用户可以直接传入一个属性文件路径，导入属性文件<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @param propsPath 用户传入的属性文件路径，本接口会检验合法性<br />
     * @return 用户希望获取的属性文件<br />
     * @warning 注意区分@function loadAsProperties 本方法导入的是单个属性文件<br />
     * @throws Exception 用户传入的属性文件路径错误或读取错误<br />
     */
    public Properties loadAsProperty(String propsPath) throws Exception {
        setPropsPath(propsPath);
        Properties pros = loadAsProperty();
        return pros;
    }

    /**
     *功能：导入属性文件，返回给用户一个HashMap，用户可以通过键获取值<br />
     * 权限：public<br />
     * 先行方法:getMyConfigFactoryInstance setPropsPath<br />
     * @return HashMap 用户属性文件中的键值对<br />
     * @warning 注意区分@function loadAsPropertiesMap 本方法导入的是单个属性文件<br />
     * @throws Exception 导入属性文件出错<br />
     */
    public HashMap loadAsPropertyMap() throws Exception {
        Properties props = loadAsProperty();
        HashMap<String,String> propsHashMap = getPropertiesMapFromProps(props);
        lock.lock();
        PropsCount++;
        lock.unlock();
        return propsHashMap;
    }

    /**
     * 功能：导入属性文件，返回给用户一个HashMap，用户可以通过键获取值<br />
     * 权限：public<br />
     * 先行方法:getMyConfigFactoryInstance<br />
     * @param propsPath 用户属性文件路径<br />
     * @return 用户属性文件键值对的一个HashMap<br />
     * @warning 注意区分@function laodAsPropertiesMap 本方法导入的是单个属性文件<br />
     * @throws Exception 读取用户属性文件出错<br />
     */
    public HashMap loadAsPropertyMap(String propsPath) throws Exception {
        setPropsPath(propsPath);
        HashMap<String,String> propsMap = loadAsPropertyMap();
        return propsMap;
    }

    /**
     * 功能：一次性导入属性文件集文件夹下所有的属性文件<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance setPropsFilesPath<br />
     * @return 属性文件夹下的全部属性键值对<br />
     * @warning 注意区分@function loadAsProperty 本方法导入的是属性文件集<br />
     * @warning 属性文件集下的属性文件中的键不能有相同的...<br />
     * @throws Exception 文件夹路径传入错误或读取错误<br />
     */
    public Properties loadAsProperties() throws Exception {
        if (null == PropsFilesPath || "".equals(PropsFilesPath)) {
            throw new Exception("属性文件集文件夹路径不能为空...");
        }
        PropsFilesPath = getPropsFilesPath();
        SequenceInputStream sin = getSeqInFromPath(PropsFilesPath);

        Properties props = new Properties();
        props.load(sin);
        return props;
    }

    /**
     * 功能：一次性导入属性文件集文件夹下所有的属性文件<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance<br />
     * @param propsFilesPath 属性文件集文件夹路径<br />
     * @return 属性文件夹下的全部属性键值对<br />
     * @warning 注意区分@function loadAsProperty 本方法导入的是属性文件集<br />
     * @warning 属性文件集下的属性文件中的键不能有相同的...<br />
     * @throws Exception 文件夹路径传入错误或读取错误<br />
     */
    public Properties loadAsProperties(String propsFilesPath) throws Exception {
        setPropsFilesPath(propsFilesPath);
        PropsFilesPath = getPropsFilesPath();
        Properties props = loadAsProperties();
        return props;
    }

    /**
     * 功能：一次性导入属性文件集文件夹下所有的属性文件，并且返回一个HashMap<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance setPropsFilesPath<br />
     * @return 属性文件夹下的全部属性键值对的HashMap<br />
     * @warning 注意区分@function loadAsProperty 本方法导入的是属性文件集<br />
     * @warning 属性文件集下的属性文件中的键不能有相同的...<br />
     * @throws Exception 文件夹路径传入错误或读取错误<br />
     */
    public HashMap loadAsPropertiesMap() throws Exception {
        Properties props = loadAsProperties();
        HashMap<String, String> propertiesMap = getPropertiesMapFromProps(props);

        return propertiesMap;
    }

    /**
     * 功能:将属性文件Properties对象转换为HashMap对象<br />
     * 权限 public<br />
     * 先行方法：无<br />
     *
     * @param props 属性文件Properties对象<br />
     * @return 转换之后的HashMap对象<br />
     */
    public HashMap getPropertiesMapFromProps(Properties props) {
        String[] propsKeys = (String[]) props.stringPropertyNames().toArray(new String[0]);
        int propsSize = propsKeys.length;
        HashMap<String, String> propsHashMap = new HashMap<String, String>(propsSize);
        for (int index = 0; index < propsSize; index++) {
            String key = propsKeys[index];
            String value = props.getProperty(key);
            propsHashMap.put(key, value);
        }
        return propsHashMap;
    }


    /**
     * 功能：一次性导入属性文件集文件夹下所有的属性文件，并且返回一个HashMap<br />
     * 权限：public<br />
     * 先行方法：getMyConfigFactoryInstance setPropsFilesPath<br />
     * @return 属性文件夹下的全部属性键值对的HashMap<br />
     * @param propsFilesPath 属性文件集文件夹路径<br />
     * @warning 注意区分@function loadAsProperty 本方法导入的是属性文件集<br />
     * @warning 属性文件集下的属性文件中的键不能有相同的...<br />
     * @throws Exception 文件夹路径传入错误或读取错误<br />
     */
    public HashMap loadAsPropertiesMap(String propsFilesPath) throws Exception {
        setPropsFilesPath(propsFilesPath);
        HashMap<String, String> propertiesMap = loadAsPropertiesMap();
        return propertiesMap;
    }

    /**
     * 功能：将给定文件夹路径的属性文件合并为一个流<br />
     * 权限：private<br />
     * 先行方法：无<br />
     * @param propsFilesPath 给定的文件夹路径<br />
     * @warning 只合并后缀为.properties的文件<br />
     * @return 合并流<br />
     * @throws Exception 文件路径错误或读取错误<br />
     */
    private SequenceInputStream getSeqInFromPath(String propsFilesPath) throws Exception{
        setPropsFilesPath(propsFilesPath);
        PropsFilesPath = getPropsFilesPath();
        final File files = new File(propsFilesPath);
        String[] propsFilesName = files.list();
        int propsFilesNameSize = propsFilesName.length;

        if (0 == propsFilesNameSize) {
            throw new Exception("属性文件集文件夹下没有文件...");
        }
        Vector<FileInputStream> fileInputStreams = new Vector<FileInputStream>(propsFilesNameSize);

        for (int index = 0; index < propsFilesNameSize; index++) {
            if (propsFilesName[index].endsWith(".properties")) {
                String tempFileName = PropsFilesPath + File.separator + propsFilesName[index];
                //必须保证每个文件后面还有回车换行符，不然两个流合并会导致属性合并
                addCRLFAtEndOfFile(tempFileName);
                File file = new File(tempFileName);
                fileInputStreams.add(new FileInputStream(file));
                lock.lock();
                PropsCount++;
                lock.unlock();
            }
        }

        if (0 == fileInputStreams.size()) {
            throw new Exception("属性文件集文件夹下没有合法文件...");
        }

        SequenceInputStream seqInputStream = new SequenceInputStream(fileInputStreams.elements());
        return seqInputStream;
    }

    /**
     * 功能：在已有文件后面添加一个回车换行符<br />
     * 权限：private<br />
     * 先行方法：无<br />
     * @param tempFileName 要修改的文件名（包含路径）<br />
     * @throws Exception 文件路径有错<br />
     */
    private void addCRLFAtEndOfFile(String tempFileName) throws Exception{
        FileWriter fileWriter = new FileWriter(new File(tempFileName),true);
        String CRLF = System.getProperty("line.separator");
        fileWriter.write(CRLF);
        fileWriter.close();
    }

}