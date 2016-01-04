/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openapi <br/>
 * 文件名：CACertConfig.java <br/>
 * 包：cn.tsign.www.openapi.ca.zjca <br/>
 * 描述：TODO <br/>
 * 修改历史： <br/>
 * 1.[2015年12月28日下午1:57:34]创建文件 by lcc
 */
package cn.tsign.www.openapi.ca.zjca;

import cn.tsign.www.openapi.constant.CACertConstants;
import cn.tsign.www.openapi.constant.ConfigProperties;
import zjca.ws.tseal.biz.Config;

/**
 * 类名：CACertConfig.java <br/>
 * 功能说明：证书配置文件 <br/>
 * 修改历史： <br/>
 * 1.[2015年12月28日下午1:57:34]创建类 by lcc
 */
public class CACertConfig {
    /** properties制证配置文件 */
    protected static ConfigProperties propertiesConfig = null;
    
    /** 制证配置信息 */
    protected static final Config config = new Config();

    /** 初始化config */
    static {
        propertiesConfig = new ConfigProperties(CACertConstants.PROPERTIES_NAME);
        config.setUrl(propertiesConfig.getString(CACertConstants.SERVER_URL));
        config.setEncKey(propertiesConfig.getString(CACertConstants.ENC_KEY));
        config.setAppCode(propertiesConfig.getString(CACertConstants.APP_CODE));
    }

}
