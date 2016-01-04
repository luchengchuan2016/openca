/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openca <br/>
 * 文件名：CALifeCycleUtilTest.java <br/>
 * 包：cn.tsgin.www.openca <br/>
 */
package cn.tsgin.www.openca;

import cn.tsgin.www.openca.util.IdCardGenerator;
import cn.tsgin.www.openca.util.NamePhoneGenerator;
import cn.tsign.www.openapi.ca.CALifeCycleUtil;
import cn.tsign.www.openapi.ca.CloudCert;
import cn.tsign.www.openapi.constant.CACertConstants;
import junit.framework.TestCase;

/**
 * 类名：CALifeCycleUtilTest.java <br/>
 * 中文名：云证书工具测试类 <br/>
 * 描述：云证书工具测试类 <br/>
 * 修改历史： <br/>
 * 1.[2015年12月29日下午7:33:47]创建文件 by lcc
 */
public class CALifeCycleUtilTest extends TestCase {

    /** 测试个人 */
    public static void testPerson() throws Exception {
        

        String issuer = CACertConstants.ACCOUNT_CERT_ISSUER_ZJCA;
        //String issuer = CACertConstants.ACCOUNT_CERT_ISSUER_TGCA;
        CloudCert certParam = getPerson();
        new CALifeCycleUtil().createCloudCert(issuer,certParam);
        System.out.println("个人证书certId:"+certParam.getCertId()+certParam.getSignCert());
        //ZjcaCertUtil.delayCert(certParam.getCertId(), CACertConstants.CERT_TYPE.PERSON.getValue());
    }

    /** 测试企业  */
    public static void testEnterPrice() throws Exception {

        //String issuer = CACertConstants.ACCOUNT_CERT_ISSUER_ZJCA;
        String issuer = CACertConstants.ACCOUNT_CERT_ISSUER_TGCA;
        CloudCert certParam = getEnterPrice();

        new CALifeCycleUtil().createCloudCert(issuer,certParam);
        System.out.println("企业证书certId:"+certParam.getCertId()+certParam.getSignCert());
        //ZjcaCertUtil.delayCert(certParam.getCertId(), CACertConstants.CERT_TYPE.ENTERPRICE.getValue());
        
    }

    public static CloudCert getPerson() {
        //名称
         String certName = "13288888888" ;
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

    public static CloudCert getEnterPrice() {

        //名称
         String certName = NamePhoneGenerator.getName();
        //国家
         String country ="cn";
        //省
         String province ="zj" ;
        //市
         String city ="hangzhou";
        //身份证
         String idNo = new IdCardGenerator().generate();
        //手机
         String mobile = NamePhoneGenerator.getTelphone();
        //邮箱
         String email ="test@163.com";
        //地址 
         String address ="address";
        //密码
         String password ="111111";
        //工商注册号
         String regCode =NamePhoneGenerator.getTelphone();
        //联系人
         String linkMan =NamePhoneGenerator.getName();
        //组织机构代码
         String orgCode ="88888888-9";
        //电话
         String phone ="";
        //统一社会信用代码
         String unitedCode ="";
        CloudCert certParam = new CloudCert();
        certParam.initEnterPrice(certName, country, province, city, idNo, mobile, email, address, password, regCode, linkMan, orgCode, phone, unitedCode);
        return certParam;
    }


}
