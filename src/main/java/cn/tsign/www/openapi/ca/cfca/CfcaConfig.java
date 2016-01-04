package cn.tsign.www.openapi.ca.cfca;
import cfca.ra.toolkit.CFCARAClientContext;
import cfca.ra.toolkit.exception.RATKException;
import cn.tsign.www.openapi.constant.CACertConstants;
import cn.tsign.www.openapi.constant.ConfigProperties;

/**
 * 
 * 类名：CfcaConfig.java <br/>
 * 功能说明：cfca取得服务器连接 <br/>
 * 修改历史： <br/>
 * 1.[2015年12月31日下午5:47:38]创建类 by lcc
 */
public class CfcaConfig {

    /** properties制证配置文件 */
    protected static ConfigProperties propertiesConfig = new ConfigProperties(CACertConstants.CACF_PROPERTIES_NAME);
    
    public static CFCARAClientContext getCFCARAClientContext() throws RATKException {        
        // 连接超时 时间（ms）
        int connectTimeout = Integer.valueOf(propertiesConfig.getString(CACertConstants.connectTimeout));
        // 读取超时 时间（ms）
        int readTimeout = Integer.valueOf(propertiesConfig.getString(CACertConstants.readTimeout));

        // 证书配置
        String cfcajksPath = CfcaConfig.class.getClass().getResource(propertiesConfig.getString(CACertConstants.keyStorePath)).getPath();
        String keyStorePath = cfcajksPath ;
        String keyStorePassword = propertiesConfig.getString(CACertConstants.keyStorePassword);
        // 证书信任链配置
        String trustStorejksPath = CfcaConfig.class.getClass().getResource(propertiesConfig.getString(CACertConstants.trustStorePath)).getPath();
        String trustStorePath = trustStorejksPath;
        String trustStorePassword = propertiesConfig.getString(CACertConstants.trustStorePassword);

        CFCARAClientContext client = null;
        
        //指定连接方式
        int type = 1;
        switch (type) {
        case 1:
            // HTTPS服务器地址
            String ip = propertiesConfig.getString(CACertConstants.serviceURL);
            // HTTPS连接端口
            int httpsPort=Integer.valueOf(propertiesConfig.getString(CACertConstants.servicePort));
            // 服务名称
            String serviceName = propertiesConfig.getString(CACertConstants.serviceName);
            
            // 初始化为https 连接方式。指定ip和端口，以及服务器名称和动作名称。另需配置ssl的证书及证书链。
            client = new CFCARAClientContext(ip, httpsPort, serviceName, "webPageServer", connectTimeout, readTimeout);
            client.initSSL(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
            break;
        case 2:
                
            // 完整的服务URL。
            String url = propertiesConfig.getString(CACertConstants.serviceURLType2);
            
            // 初始化为https 连接方式。指定服务的url。另需配置ssl的证书及证书链。
            client = new CFCARAClientContext(url, connectTimeout, readTimeout);
            client.initSSL(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
            break;
        default:
            break;
        }
        return client;
    }

}
