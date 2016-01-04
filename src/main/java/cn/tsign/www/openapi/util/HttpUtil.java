package cn.tsign.www.openapi.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {
    public String PostResponse(String url, String param) throws IOException {
        String result = null;
        URL myurl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        OutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(param.getBytes());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        if (null != buffer)
            result = buffer.toString();
        return result;
    }

    public String GetResponse(String url) throws IOException {
        String result = null;
        HttpURLConnection httpconn = null;
        URL myurl = new URL(url);
        URLConnection conn = myurl.openConnection();
        httpconn = (HttpURLConnection) conn;
        httpconn.setRequestMethod("GET");
        httpconn.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpconn.getInputStream(), "UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        if (null != buffer)
            result = buffer.toString();
        return result;
    }

    public String uploadFile(String url, File file, String fileName)
            throws IOException {
        String result = null;
        String BOUNDARY = "---------7d4a6d158c9";
        URL myurl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);
        OutputStream out = new DataOutputStream(conn.getOutputStream());
        byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"fileInput\";filename=\""
                + fileName + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] data = sb.toString().getBytes();
        out.write(data);
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        out.write(end_data);
        out.flush();
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        if (null != buffer)
            result = buffer.toString();
        return result;
    }

    public static void main(String[] args) {
        HttpUtil hu = new HttpUtil();
        try {
            System.out
                    .print(hu
                            .GetResponse("http://www.kuaidi100.com/query?type=shunfeng&postid=203092098157"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
