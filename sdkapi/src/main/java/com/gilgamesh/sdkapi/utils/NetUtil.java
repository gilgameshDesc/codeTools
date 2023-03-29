package com.gilgamesh.sdkapi.utils;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhqsdhr
 */
@Slf4j
public class NetUtil {
    public static final String GET = "GET";
    public static final String POST = "POST";

    public static String send(String reqUrl,String fileUrl) throws IOException {
        String[] fileParts = fileUrl.split("/");
        String filename = fileParts[fileParts.length-1];
        String result = null;

        URL fileObj = new URL(fileUrl);
        HttpURLConnection con1 = (HttpURLConnection) fileObj.openConnection();


        /**
         * 第一部分
         */
        URL urlObj = new URL(reqUrl);
        // 连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();


        /**
         * 设置关键值
         */
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存


        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");


        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);


        // 请求正文信息


        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
                + filename + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");


        byte[] head = sb.toString().getBytes("utf-8");


        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);


        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(con1.getInputStream());
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();


        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线


        out.write(foot);


        out.flush();
        out.close();


        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
            try {
                if(con!=null) {
                    con.disconnect();
                }
            } catch (Exception e) {}
            try {
                if(con1!=null) {
                    con1.disconnect();
                }
            } catch (Exception e) {}
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        } finally {
            if (reader != null) {
                reader.close();
            }

        }


        return result;


    }


    /**
     * Http请求
     *
     * @param reqUrl
     * @param reqMeth  POST或GET
     * @param reqParam
     * @return
     */
    public static String send(String reqUrl, String reqMeth, String reqParam, String contentType) {
        log.info("NetUtil request[url="+reqUrl+"]-[param="+reqParam+"]");
        DataOutputStream dataOutStream = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        HttpURLConnection connection = null;
        try {

            URL url = new URL(reqUrl);
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(reqMeth);
            //connection.setRequestProperty("Content-type", "text/html");
            //connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Content-type", contentType);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.connect();

            if ("POST".equals(reqMeth)) {

                outputStream = connection.getOutputStream();
                dataOutStream = new DataOutputStream(outputStream);
                dataOutStream.write(reqParam.getBytes("utf-8"));
                dataOutStream.flush();
                outputStream.flush();
            }
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(
                    inputStreamReader);
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = bufferedReader.readLine()) != null) {
                sb.append(lines);
            }
            return sb.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return "";
        } finally {
            try {
                if (dataOutStream != null) {
                    dataOutStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public static void main(String[] args) {
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        String x = format.format(time);
        Integer hour = Integer.parseInt(x);
        System.out.println(hour);
    }

    static HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new Mitm();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class Mitm implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

}
