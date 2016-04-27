package org.bigmouth.nvwa.network.http.security;

import java.util.Iterator;
import java.util.regex.Pattern;

import javax.servlet.FilterConfig;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 安全过滤配置管理类，由XSSSecurityManger修改
 * 
 * @author Allen.Hu / 2013-3-23
 */
public class XSSSecurityManager {

    private static Logger logger = Logger.getLogger(XSSSecurityManager.class);

    /**
     * REGEX：校验正则表达式
     */
    public static String REGEX;

    /**
     * 特殊字符匹配
     */
    private static Pattern XSS_PATTERN;

    private XSSSecurityManager() {
        // 不可被实例化
    }

    public static void main(String[] args) {
        XSSSecurityManager.init(null);
        System.out.println(matches("\"><script>alert('XSS');</script><\""));
    }

    public static void init(FilterConfig config) {
        logger.info("XSSSecurityManager init(FilterConfig config) begin");
        // 初始化过滤配置文件
        String xssPath = config.getServletContext().getRealPath("/") + config.getInitParameter("securityconfig");
        // String xssPath = "F:\\workspace\\skymarket-cmsrd\\WebRoot\\WEB-INF\\classes\\xss\\xss_security_config.xml";
        // 初始化安全过滤配置
        try {
            if (initConfig(xssPath)) {
                // 生成匹配器
                XSS_PATTERN = Pattern.compile(REGEX);
            }
        }
        catch (DocumentException e) {
            logger.error("安全过滤配置文件xss_security_config.xml加载异常", e);
        }
        logger.info("XSSSecurityManager init(FilterConfig config) end");
    }

    /**
     * 读取安全审核配置文件xss_security_config.xml 设置XSSSecurityConfig配置信息
     * 
     * @param path 配置文件地址
     * @return
     * @throws DocumentException
     */
    @SuppressWarnings("unchecked")
    public static boolean initConfig(String path) throws DocumentException {
        logger.info("XSSSecurityManager.initConfig(String path) begin");
        Element superElement = new SAXReader().read(path).getRootElement();
        XSSSecurityConfig.IS_CHECK_HEADER = new Boolean(getEleValue(superElement, XSSSecurityCon.IS_CHECK_HEADER));
        XSSSecurityConfig.IS_CHECK_PARAMETER = new Boolean(getEleValue(superElement, XSSSecurityCon.IS_CHECK_PARAMETER));
        XSSSecurityConfig.IS_LOG = new Boolean(getEleValue(superElement, XSSSecurityCon.IS_LOG));
        XSSSecurityConfig.IS_CHAIN = new Boolean(getEleValue(superElement, XSSSecurityCon.IS_CHAIN));
        XSSSecurityConfig.REPLACE = new Boolean(getEleValue(superElement, XSSSecurityCon.REPLACE));

        Element regexEle = superElement.element(XSSSecurityCon.REGEX_LIST);

        if (regexEle != null) {
            Iterator<Element> regexIt = regexEle.elementIterator();
            StringBuffer tempStr = new StringBuffer("^");
            // xml的cdata标签传输数据时，会默认在\前加\，需要将\\替换为\
            while (regexIt.hasNext()) {
                Element regex = (Element) regexIt.next();
                String tmp = regex.getText();
                tmp = tmp.replaceAll("\\\\\\\\", "\\\\");
                tempStr.append(tmp);
                tempStr.append("|");
            }
            if (tempStr.charAt(tempStr.length() - 1) == '|') {
                REGEX = tempStr.substring(0, tempStr.length() - 1) + "$";
                logger.info("安全匹配规则" + REGEX);
            }
            else {
                logger.error("安全过滤配置文件加载失败:正则表达式异常 " + tempStr.toString());
                return false;
            }
        }
        else {
            logger.error("安全过滤配置文件中没有 " + XSSSecurityCon.REGEX_LIST + " 属性");
            return false;
        }
        logger.info("XSSSecurityManager.initConfig(String path) end");
        return true;

    }

    /**
     * 从目标element中获取指定标签信息，若找不到该标签，记录错误日志
     * 
     * @param element 目标节点
     * @param tagName 制定标签
     * @return
     */
    private static String getEleValue(Element element, String tagName) {
        if (isNullStr(element.elementText(tagName))) {
            logger.error("安全过滤配置文件中没有 " + XSSSecurityCon.REGEX_LIST + " 属性");
        }
        return element.elementText(tagName);
    }

    /**
     * 对非法字符进行替换
     * 
     * @param text
     * @return
     */
    public static String securityReplace(String text) {
        if (isNullStr(text)) {
            return text;
        }
        else {
            return text.replaceAll(REGEX, XSSSecurityCon.REPLACEMENT);
        }
    }

    /**
     * 匹配字符是否含特殊字符
     * 
     * @param text
     * @return
     */
    public static boolean matches(String text) {
        if (text == null) {
            return false;
        }
        return XSS_PATTERN.matcher(text).matches();
    }

    /**
     * 释放关键信息
     */
    public static void destroy() {
        logger.info("XSSSecurityManager.destroy() begin");
        XSS_PATTERN = null;
        REGEX = null;
        logger.info("XSSSecurityManager.destroy() end");
    }

    /**
     * 判断是否为空串
     * 
     * @param value
     * @return
     */
    public static boolean isNullStr(String value) {
        return value == null || value.trim().equals("");
    }
}
