package gordenyou.huadian.common;

import android.os.Handler;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Gordenyou on 2018/1/17.
 */

public class WebServiceRequest {

    //加了void修饰符后，便不是构造函数了
    public void WebServiceRequest(String MethodName, SoapObject soapObject,
                                  int m, Handler handler) {
        Message msg = new Message();
        try {
            String nameSpace = GlobalConstant.NAMESPACE;
            String methodName = MethodName;
//            String endPoint = SaveInfo.getProperties().getProperty("WebServiceurl").trim();
            String endPoint = "http://192.168.1.100/Huadian/Service.asmx";
            String soapAction = nameSpace + methodName;
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;
            HttpTransportSE transportSE = new HttpTransportSE(endPoint);

            transportSE.call(soapAction, envelope);
            if (envelope.getResponse() != null) {

                SoapObject object = (SoapObject) envelope.bodyIn;
                String result = object.getProperty(0).toString();
                msg.obj = result.trim();
                msg.what = m;
            }
        } catch (Exception e) {
            msg.obj = e.getMessage();
            msg.what = -99;
        } finally {
            handler.sendMessage(msg);
        }

    }

    public void WebServiceRequest1(String MethodName, SoapObject soapObject,
                                   int m, Handler handler) {
        Message msg = new Message();
        try {
            String nameSpace = GlobalConstant.NAMESPACE;
            String methodName = MethodName;
//            String endPoint = SaveInfo.getProperties().getProperty("WebServiceurl").trim();
            String endPoint = "http://128.0.4.243/Myway/Service.asmx";
//            String endPoint = "http://localhost:49380/MillionTECH.MES.Server/Service.asmx";
            String soapAction = nameSpace + methodName;
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;
            HttpTransportSE transportSE = new HttpTransportSE(endPoint);

            transportSE.call(soapAction, envelope);
            if (envelope.getResponse() != null) {

                SoapObject object = (SoapObject) envelope.bodyIn;
                String result = object.getProperty(0).toString();
                if (m != -1) {
                    msg.obj = result.trim();
                    msg.what = m;
                } else {
                    msg.what = Integer.parseInt(result.trim());
                }
            }
        } catch (Exception e) {
            msg.obj = e.getMessage();
            msg.what = -99;
        } finally {
            handler.sendMessage(msg);
        }

    }
}
