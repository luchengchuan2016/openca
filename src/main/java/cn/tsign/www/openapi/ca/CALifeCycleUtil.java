/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openapi <br/>
 * 文件名：CALifeCycleUtil.java <br/>
 * 包：cn.tsign.www.openapi.ca <br/>
 * 描述：TODO <br/>
 * 修改历史： <br/>
 * 1.[2015年5月28日下午10:02:28]创建文件 by jsh
 */
package cn.tsign.www.openapi.ca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.tsign.www.openapi.ca.cfca.CfcaCertUtil;
import cn.tsign.www.openapi.ca.tgca.TgcaCertUtil;
import cn.tsign.www.openapi.ca.zjca.ZjcaCertUtil;
import cn.tsign.www.openapi.constant.CACertConstants;
import cn.tsign.www.openapi.util.Base64;
import cn.tsign.www.openapi.util.StringUtil;

/**
 * 类名：CALifeCycleUtil.java <br/>
 * 功能说明：云证书工具 <br/>
 * 修改历史： <br/>
 * 1.[2015年5月28日下午10:02:28]创建类 by jsh
 */
public class CALifeCycleUtil {
    
    private static final Logger LOG = LoggerFactory .getLogger(CALifeCycleUtil.class);
    
    /**
     * 功能说明：创建云证书
     * @param issuer 发布者
     * @param certParam 证书信息
     * @return <br/>
     *         修改历史：<br/>
     *         1.[2015年5月29日上午9:47:46] 创建方法 by jsh
     */
    public final CloudCert createCloudCert(final String issuer,
            CloudCert certParam) {
        final SoftKeyTool skt = new SoftKeyTool();
        String csr = null; 
        try {
            if (StringUtil.isEqualIgnoreCase(issuer,
                    CACertConstants.ACCOUNT_CERT_ISSUER_TGCA)) {
                csr = skt.generateCSR(certParam.getCertName(),
                        CACertConstants.ORGANIZE_TIMEVALE, CACertConstants.ORGANIZE_TIMEVALE,
                        certParam.getCity(), certParam.getProvince(),
                        certParam.getCountry());
                certParam.setPk(skt.getPrivateKey());
            }
            else {
                byte[] csrbytes;
                csrbytes = skt.generatePKCS10(certParam.getCertName(),
                        CACertConstants.ORGANIZE_TIMEVALE, CACertConstants.ORGANIZE_TIMEVALE,
                        certParam.getCity(), certParam.getProvince(),
                        certParam.getCountry()); 
                certParam.setPk(skt.getPrivateKey());
                csr = Base64.encode(csrbytes);
            }
            LOG.debug("[tgdebug:creatCloudCert]generate private key done,csr="
                    + csr + ",pk=" + certParam.getPk());

            if (StringUtil.isEqualIgnoreCase(issuer,
                    CACertConstants.ACCOUNT_CERT_ISSUER_TGCA)) {
                String signCert = TgcaCertUtil.createSignCert(CACertConstants.RSA, csr,
                        certParam.getCertName(), certParam.getIdNo(),
                        CACertConstants.ORGANIZE_TIMEVALE, CACertConstants.ORGANIZE_TIMEVALE,
                        certParam.getCity(), certParam.getProvince(),
                        certParam.getCountry(), certParam.getMobile(),
                        certParam.getEmail(), certParam.getAddress(),
                        certParam.getCertType(), certParam.getPassword());
                certParam.setSignCert(signCert);
            } 
            else if(StringUtil.isEqualIgnoreCase(issuer,
                    CACertConstants.ACCOUNT_CERT_ISSUER_CFCA)){
                CfcaCertUtil.createCfcaCert(certParam,csr);
            }  else {
                certParam = ZjcaCertUtil.createSignCert(certParam,csr);
            }
            LOG.debug("[creatCert]generate signcert done,");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("[tgerror:creatCloudCert]", e);
        }

        return certParam;
    }
}
