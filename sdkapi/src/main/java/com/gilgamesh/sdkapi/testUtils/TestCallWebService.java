package com.gilgamesh.sdkapi.testUtils;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import javax.xml.namespace.QName;

public class TestCallWebService {
    public static void main(String[] args) {
        //String wsdl = "http://localhost:8764/cmsWebservice/soap/cmsWebservice?wsdl";//WSDL地址
        //String wsdl = "http://192.168.16.16:20085/cmsWebservice/soap/cmsWebservice?wsdl";//WSDL地址
        //String wsdl = "http://10.195.53.11:20000/cmsWebservice/soap/cmsWebservice?wsdl";//WSDL地址
        //String wsdl = "http://81.69.26.154:20050/cmsWebservice/soap/cmsWebservice?wsdl";//WSDL地址
        String wsdl = "http://10.195.52.21:10050/cmsWebservice/soap/cmsWebservice?wsdl";//WSDL地址


        String nameSpace = "http://web.ideal.sh.cn/";//接口命名空间<?xml v
//        String sendXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//                "<root>"+
//                "<url></url>"+
//                "<agentId>A20096569</agentId>"+
//                "<station>18916552706</station>"+
//                "<userData>TMST201533000002812571</userData>"+
//                "<reason>1</reason>"+
//                "<dnis>18918566831</dnis>"+
//                "<permission>picc123</permission>"+
//                "<event>MakeCall</event>"+
//                "<comcode>33120204</comcode>"+
//                "<processID>PROC201533000000108262</processID>"+
//                "<activityID>ACTI201533000000121161</activityID>"+
//                "<bpmProcessInstanceId>PIID201533000089601057</bpmProcessInstanceId>"+
//                "<taskID>TASK201533000262314961</taskID>"+
//                "<tasktracestatus>mu001</tasktracestatus>"+
//                "</root>";
        //登录
        String optionName = "login";//请求方法名diacall
        String sendXML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root><passWord>picc123456</passWord><reason>2</reason><agentId>B10043230</agentId><userData></userData><port>9000|9000</port><ip>10.133.240.223|157.122.153.68</ip><station>18918566831</station><permission>picc123</permission><state>3</state><event>Login</event></root>";
        //通话
//        String optionName = "diacall";//请求方法名diacall
//        String sendXML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
//                "<root>"+
//                "<dnis>18916552706</dnis>"+
//                "<reason></reason>"+
//                "<station>18918566831</station>"+
//                "<processID>PROC202031000000175213</processID>"+
//                "<agentId>B10043194</agentId>"+
//                "<permission>picc123</permission>"+
//                "<userData>TMST202231003620396133</userData>"+
//                "<tasktracestatus></tasktracestatus>"+
//                "<taskID>TASK202231004248016755</taskID>"+
//                "<comcode>31022530</comcode>"+
//                "<event>MakeCall</event>"+
//                "<activityID>ACTI202231000000282097</activityID>"+
//                "<bpmProcessInstanceId>PIID202231002943868072</bpmProcessInstanceId>"+
//                "</root>";
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsdl);
        QName name=new QName(nameSpace,optionName);
        Object[] objects= new Object[0];
        try {
            objects = client.invoke(name,sendXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(objects[0].toString());
    }
}
