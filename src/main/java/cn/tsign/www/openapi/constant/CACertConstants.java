/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openapi <br/>
 * 文件名：CACertConstants.java <br/>
 * 包：cn.tsign.www.openapi.constant <br/>
 * 描述：TODO <br/>
 * 修改历史： <br/>
 * 1.[2015年12月27日上午9:42:31]创建文件 by lcc
 */
package cn.tsign.www.openapi.constant;


/**
 * 类名：CACertConstants.java <br/>
 * 功能说明：制证常量类 <br/>
 * 修改历史： <br/>
 * 1.[2015年12月27日上午9:42:31]创建类 by lcc
 */
public class CACertConstants {

    /** 加密类型rsa */
    public static final String RSA = "RSA";
    
    /** 安全哈希算法SHA1RSA */
    public static final String SHA1_WITH_RSA = "SHA1withRSA";
    
    /** ========================【zjcaconfig.properties属性文件字段】开始===========  */
    /** 制证属性文件名称 */
    public static final String PROPERTIES_NAME = "zjcaconfig.properties";

    /** 制证服务器网址 */
    public static final String SERVER_URL = "URL";

    /** 密钥 */
    public static final String ENC_KEY = "PWD";

    /** 应用id */
    public static final String APP_CODE = "APP_CODE";
    
    /** 企业证书策略 */
    public static final String ENT_POLICY = "ENT_POLICY";

    /** 企业收费策略 */
    public static final String ENT_FEE = "ENT_FEE";
    
    /** 用户证书策略 */
    public static final String USER_POLICY = "USER_POLICY";

    /** 用户收费策略 */
    public static final String USER_FEE = "USER_FEE";

    /** 数字证书请求参数 */
    public static final String RSA_PKCS10_REQ = "RSA_PKCS10_REQ";

    /** 私钥 */
    public static final String PRIKEY = "PRIKEY";
    

    /** ========================【zjcaconfig.properties属性文件字段】结束===========  */
    /** ========================【cfcaconfig.properties属性文件字段】开始===========  */
    /** 制证属性文件名称 */
    public static final String CACF_PROPERTIES_NAME = "cfcaconfig.properties";
    
    /** 企业普通证书 */
    public static final String ENTERPRICE_NORMAL_TEMPLATE_ID = "1019";
    
    /** 个人普通证书 */
    public static final String PERSON_NORMAL_TEMPLATE_ID = "1017";

    /** 通信机构：通信证书所在机构 */
    public static final String communicationOrgID = "communicationOrgID";

    /** 机构：证书所属机构 */
    public static final String organizationID = "organizationID";
    
    /** 应用(OU) */
    public static final String application = "application";
    
    /** 证书模板 */
    public static final String templateID = "templateID";
    
    /** 系统标识 */
    public static final String sysIdentification = "sysIdentification";
    
    /** 服务url 类型2网址 */
    public static final String serviceURLType2 = "serviceURLType2";
    
    /** 服务url */
    public static final String serviceURL = "serviceURL";
    
    /** 服务端口 */
    public static final String servicePort = "servicePort";
    
    /** 服务器名称 */
    public static final String serviceName = "serviceName";

    /** 是否将Email加到主题备用名(1：加，0：不加) */
    public static final String SecurityEmailCert = "SecurityEmailCert";

    /** 密钥长度 */
    public static final String KeyLength = "2048";

    /** 证书配置 */
    public static final String keyStorePath = "keyStorePath";

    /** 证书配置 */
    public static final String keyStorePassword = "keyStorePassword";

    /** 证书信任链配置 */
    public static final String trustStorePath = "trustStorePath";

    /** 证书信任链配置  */
    public static final String trustStorePassword = "trustStorePassword";

    /**  连接超时 时间（ms） */
    public static final String connectTimeout = "connectTimeout";

    /** 读取超时 时间（ms）  */
    public static final String readTimeout = "readTimeout";
    
    
    /**  cfca请求类型  */
    public static enum CFCA_REQ_TYPE {
        //创建证书并下载公钥
        CREATE_DOWN("3401");
        private String value;

        private CFCA_REQ_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
        
    };

    /** ========================【cfcaconfig.properties属性文件字段】结束===========  */
    
    //天谷证书
    public static final String ACCOUNT_CERT_ISSUER_TGCA = "Timevale User CA";
    //zjca证书
    public static final String ACCOUNT_CERT_ISSUER_ZJCA = "ZJCA OCA2";
    //cfca证书
    public static final String ACCOUNT_CERT_ISSUER_CFCA = "CFCA OCA2";
    
    //天谷信息，用于生成csr
    public static final String ORGANIZE_TIMEVALE = "timevale";
    
    /**  证书类型 1为个人，2为公司  */
    public static enum CERT_TYPE {
        PERSON("1"), ENTERPRICE("2");
        private String value;

        private CERT_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static CERT_TYPE getByName(String name) {
            for (CERT_TYPE prop : values()) {
                if (prop.getValue().equals(name)) {
                    return prop;
                }
            }
            throw new IllegalArgumentException(
                    name + " is not a valid PropName");
        }
    };

}
