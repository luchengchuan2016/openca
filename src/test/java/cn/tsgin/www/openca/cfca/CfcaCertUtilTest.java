/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openca <br/>
 * 文件名：CfcaCertUtilTest.java <br/>
 * 包：cn.tsgin.www.openca <br/>
 */
package cn.tsgin.www.openca.cfca;

import cn.tsgin.www.openca.util.IdCardGenerator;
import cn.tsgin.www.openca.util.NamePhoneGenerator;
import cn.tsign.www.openapi.ca.CloudCert;
import cn.tsign.www.openapi.ca.SoftKeyTool;
import cn.tsign.www.openapi.ca.cfca.CfcaCertUtil;
import cn.tsign.www.openapi.util.Base64;
import junit.framework.TestCase;

/**
 * 类名：CfcaCertUtilTest.java <br/>
 * 中文名：TODO <br/>
 * 描述：TODO <br/>
 * 修改历史： <br/>
 * 1.[2015年12月30日上午9:52:32]创建文件 by lcc
 */
public class CfcaCertUtilTest extends TestCase {

    /** 测试个人 */
    public static void testPerson() throws Exception {
        
        byte[] csrbytes;
        final SoftKeyTool skt = new SoftKeyTool();
        csrbytes = skt.generatePKCS10("dd",
                "timevale", "timevale",
                "zj", "hz",
              "cn"  );
        String csr = Base64.encode(csrbytes);
        
        CloudCert certParam = getPerson();
        CfcaCertUtil.createCfcaCert(certParam,csr);
        System.out.println("个人证书certId:"+certParam.getCertId());
    }


    public static CloudCert getPerson() {
        //名称
         String certName = NamePhoneGenerator.getName() ;
        //国家
         String country ="cn";
        //省
         String province ="zj" ;
        //市
         String city ="hangzhou";
        //手机
         String mobile = "13288888888";
        //邮箱
         String email ="test@163.com";
        //地址 
         String address ="address";
        //密码
         String password ="111111";
         String idNo = new IdCardGenerator().generate();
        CloudCert certParam = new CloudCert();
        certParam.initPerson(certName, country, province, city, idNo, mobile, email, address, password);
        
        return certParam;
    }
    

}
