package cn.ff.burning.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

/**
 * 处理请求的util
 *
 * @author ff 20181013
 */
public class WebUtil {
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    }

    public static boolean isContentTypeJson(SavedRequest request) {
        return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String getIp(HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = httpRequest.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = httpRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = httpRequest.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    public static String getBrowser(HttpServletRequest httpRequest) {
        String agent = httpRequest.getHeader("User-Agent");
        String browserVersion = "";
        if (StringUtils.isEmpty(agent))
            return "未知";
        //从请求头中读取User-Agent值
        if (agent.indexOf("MSIE") > 0) {
            browserVersion = "IE";
        } else if (agent.indexOf("Firefox") > 0) {
            browserVersion = "Firefox";
        } else if (agent.indexOf("Chrome") > 0) {
            browserVersion = "Chrome";
        } else if (agent.indexOf("Safari") > 0) {
            browserVersion = "Safari";
        } else if (agent.indexOf("Camino") > 0) {
            browserVersion = "Camino";
        } else if (agent.indexOf("Konqueror") > 0) {
            browserVersion = "Konqueror";
        } else if (agent.indexOf("QQBrowser") > 0) {
            browserVersion = "QQBrowser";
        }else if (agent.contains("PostmanRuntime") ) {
            browserVersion = "Postman";
        }
        if ("".equals(browserVersion)) {
            StringTokenizer st = new StringTokenizer(agent, "(;)");
            while (st.hasMoreElements()) {
                browserVersion = st.nextToken();
            }
        }
        return browserVersion;
    }

    public static String getSysInfo(HttpServletRequest httpRequest) {
        String systenInfo = "";
        String agent = httpRequest.getHeader("User-Agent");
        if (StringUtils.isEmpty(agent))
            return "未知";
        if (agent.indexOf("NT 10.0") > 0) {
            systenInfo = "Windows Vista/Server 10";
        } else if (agent.indexOf("NT 8.0") > 0) {
            systenInfo = "Windows Vista/Server 8";
        } else if (agent.indexOf("NT 6.0") > 0) {
            systenInfo = "Windows Vista/Server 2008";
        } else if (agent.indexOf("NT 5.2") > 0) {
            systenInfo = "Windows Server 2003";
        } else if (agent.indexOf("NT 5.1") > 0) {
            systenInfo = "Windows XP";
        } else if (agent.indexOf("NT 6.0") > 0) {
            systenInfo = "Windows Vista";
        } else if (agent.indexOf("NT 6.1") > 0) {
            systenInfo = "Windows 7";
        } else if (agent.indexOf("NT 6.2") > 0) {
            systenInfo = "Windows Slate";
        } else if (agent.indexOf("NT 6.3") > 0) {
            systenInfo = "Windows 8";
        } else if (agent.indexOf("NT 5") > 0) {
            systenInfo = "Windows 2000";
        } else if (agent.indexOf("NT 4") > 0) {
            systenInfo = "Windows NT4";
        } else if (agent.indexOf("Me") > 0) {
            systenInfo = "Windows Me";
        } else if (agent.indexOf("98") > 0) {
            systenInfo = "Windows 98";
        } else if (agent.indexOf("95") > 0) {
            systenInfo = "Windows 95";
        } else if (agent.indexOf("Mac") > 0) {
            systenInfo = "Mac";
        } else if (agent.indexOf("Unix") > 0) {
            systenInfo = "UNIX";
        } else if (agent.indexOf("Linux") > 0) {
            systenInfo = "Linux";
        } else if (agent.indexOf("SunOS") > 0) {
            systenInfo = "SunOS";
        }else if (agent.contains("PostmanRuntime") ) {
            systenInfo = "Postman";
        }
        if ("".equals(systenInfo)) {
            StringTokenizer st = new StringTokenizer(agent, "(;)");
            int i = 0;
            while (st.hasMoreElements() && i <= 1) {
                i++;
                systenInfo = st.nextToken();
            }
        }
        return systenInfo;
    }


}
