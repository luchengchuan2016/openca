/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：projrest <br/>
 * 文件名：StringUtil.java <br/>
 * 包：cn.tsign.www.proj.util <br/>
 * 描述：工具包 <br/>
 * 修改历史： <br/>
 * 1.[2015年3月19日下午3:40:09]创建文件 by jsh
 */
package cn.tsign.www.openapi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名：StringUtil.java <br/>
 * 功能说明：字符串操作工具类 <br/>
 * 修改历史： <br/>
 * 1.[2015年3月19日下午3:40:09]创建类 by jsh
 */
public final class StringUtil {

    /**
     * StringUtil构造方法
     */
    private StringUtil() {
    }

    /**
     * 功能说明：判断字符串是否为空
     * @param str 字符串
     * @return 若为空，返回true，否则为false<br/>
     *         修改历史：<br/>
     *         1.[2015年3月19日下午3:41:37] 创建方法 by jsh
     */
    public static boolean isNull(final String str) {
        boolean result = null == str || str.trim().length() <= 0;
        if (!result) {
            result = isEqualIgnoreCase(str, "null");
        }
        return result;
    }

    /**
     * 功能说明：比较字符串
     * @param str1 字符串1
     * @param str2 字符串2
     * @return <br/>
     *         修改历史：<br/>
     *         1.[2015年3月26日下午12:19:54] 创建方法 by jsh
     */
    public static boolean isEqualIgnoreCase(final String str1, final String str2) {
        boolean result = false;
        if (null == str1 || null == str2) {
            return result;
        }
        if (str1.toLowerCase().equals(str2.toLowerCase())) {
            result = true;
        }
        return result;
    }

    /**
     * 功能说明：比较字符串
     * @param str1 字符串1
     * @param str2 字符串2
     * @return <br/>
     *         修改历史：<br/>
     *         1.[2015年3月26日下午12:19:54] 创建方法 by jsh
     */
    public static boolean isEqual(final String str1, final String str2) {
        boolean result = false;
        if (null == str1 || null == str2) {
            return result;
        }
        if (str1.equals(str2)) {
            result = true;
        }
        return result;
    }

    /**
     * 功能说明：清除空格
     * @param str 字符串
     * @return <br/>
     *         修改历史：<br/>
     *         1.[2015年4月1日下午3:09:48] 创建方法 by jsh
     */
    public static String clearBlank(final String str) {
        String result = null;
        if (isNull(str)) {
            return result;
        }
        final Pattern pattern = Pattern.compile("\\s*|\r|\n");
        final Matcher matcger = pattern.matcher(str);
        result = matcger.replaceAll("");
        return result;
    }
}
