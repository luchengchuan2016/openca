/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openca <br/>
 * 文件名：CfcaCertUtil.java <br/>
 * 包：cn.tsign.www.openapi.ca.cfca <br/>
 */
package cn.tsign.www.openapi.ca.cfca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cfca.ra.toolkit.CFCARAClientContext;
import cfca.ra.vo.request.CertApplyRequestVO;
import cfca.ra.vo.response.CertResponseVO;
import cn.tsign.www.openapi.ca.CloudCert;
import cn.tsign.www.openapi.constant.CACertConstants;

/**
 * 类名：CfcaCertUtil.java <br/>
 * 中文名：中国金融认证中心证书工具类 <br/>
 * 描述：中国金融认证中心证书工具类 <br/>
 * 修改历史： <br/>
 * 1.[2015年12月30日上午9:46:40]创建文件 by lcc
 */
public class CfcaCertUtil {

    private static final Logger LOG = LoggerFactory
            .getLogger(CfcaCertUtil.class);

    /**
     * 功能说明：创建cfca证书
     * @param certParam 用户信息
     * @return 返回证书id和证书编码
     * 修改历史：<br/>
     * 1.[2015年12月30日上午11:04:53] 创建方法 by lcc
     */
    public static CloudCert createCfcaCert(CloudCert certParam,String csr) {
        
        try {
            CFCARAClientContext context = CfcaConfig.getCFCARAClientContext();
            
            CertResponseVO certResponseVO = context.tx3401(getCertApplyRequestVO(certParam,csr));
            // 证书id
            certParam.setCertId(certResponseVO.getSerialNo());
            // Base64编码的签名证书
            certParam.setSignCert(certResponseVO.getSignatureCert());
            
            if(LOG.isInfoEnabled()){
                LOG.info(" 调用CfcaCertUtil.createCfcaCert成功  用户名： "+ certParam.getCertName());
            }
            return certParam;
        } catch (Exception e) {
            LOG.error("调用CfcaCertUtil.createCfcaCert失败  用户名： "+ certParam.getCertName() + " 失败信息："+e.getMessage(),e );
        }
        return null;
    }
    
    /**
     * 
     * 功能说明：获取制证请求参数
     * @param certParam 用户信息
     * @return 返回制证请求参数
     * 修改历史：<br/>
     * 1.[2015年12月30日上午11:13:21] 创建方法 by lcc
     */
    private static CertApplyRequestVO getCertApplyRequestVO(CloudCert certParam,String csr){
        
        
        CertApplyRequestVO certApplyRequestVO = new CertApplyRequestVO();
        
        //创建证书并下载公钥
        certApplyRequestVO.setTxType(CACertConstants.CFCA_REQ_TYPE.CREATE_DOWN.getValue());
        
        // 通信机构：通信证书所在机构
        certApplyRequestVO.setCommunicationOrgID(CfcaConfig.propertiesConfig.getString(CACertConstants.communicationOrgID));
        // 应用(OU)
        certApplyRequestVO.setApplication(CfcaConfig.propertiesConfig.getString(CACertConstants.application));
        
        
        // 密钥长度
        certApplyRequestVO.setKeyLength(CACertConstants.KeyLength);
        // 系统标识（DN规则为5.1时的输入项）
        certApplyRequestVO.setSysIdentification(CfcaConfig.propertiesConfig.getString(CACertConstants.sysIdentification));
        // 机构：证书所属机构
        certApplyRequestVO.setOrganizationId(CfcaConfig.propertiesConfig.getString(CACertConstants.organizationID));
        // 有效期
        certApplyRequestVO.setPeriod("");
        certApplyRequestVO.setCsr(csr);
        // 用户名称
        certApplyRequestVO.setSubscriberName(certParam.getCertName());
        // 证件类型
        certApplyRequestVO.setIdentificationType("Z");
        // 证件号码
        certApplyRequestVO.setIdentificationNo(certParam.getIdNo());
        certApplyRequestVO.setEmail(certParam.getEmail());
        // 是否将Email加到主题备用名(1：加，0：不加)
        certApplyRequestVO.setIsSecurityEmailCert(CfcaConfig.propertiesConfig.getString(CACertConstants.SecurityEmailCert));
        // 地址
        certApplyRequestVO.setAddress(certParam.getAddress());
        // 证书主题用户名
        certApplyRequestVO.setSubscriberNameInDn(certParam.getCertName());
        // 用户唯一标识（DN规则为5.1时的输入项）
        certApplyRequestVO
                .setSubscriberIdentification("");
        
        if (CACertConstants.CERT_TYPE.ENTERPRICE.getValue().equals(certParam.getCertType())) {
            // 自定义扩展域
            String extensionInfo = "";
            // 证书模板
            certApplyRequestVO.setTemplateId(CACertConstants.ENTERPRICE_NORMAL_TEMPLATE_ID);
            certApplyRequestVO.setExtensionInfo(extensionInfo);
            // 电话
            certApplyRequestVO.setPhoneNo(certParam.getPhone());
        }else{
            // 证书模板
            certApplyRequestVO.setTemplateId(CACertConstants.PERSON_NORMAL_TEMPLATE_ID);
        }
        
        return certApplyRequestVO;
    }
}
