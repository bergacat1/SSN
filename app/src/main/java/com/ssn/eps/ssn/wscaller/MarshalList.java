package com.ssn.eps.ssn.wscaller;

import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by lluis on 9/1/16.
 */
public class MarshalList<T> implements Marshal {
    @Override
    public Object readInstance(XmlPullParser xmlPullParser, String s, String s1, PropertyInfo propertyInfo) throws IOException, XmlPullParserException {
        return null;
    }

    @Override
    public void writeInstance(XmlSerializer xmlSerializer, Object o) throws IOException {

        for (Iterator i = ((List<T>)o).iterator(); i.hasNext();){
            T obj = (T) i.next();
            xmlSerializer.text(obj.toString());
            if(i.hasNext()){
                xmlSerializer.endTag(xmlSerializer.getNamespace(),xmlSerializer.getName());
                xmlSerializer.startTag(xmlSerializer.getNamespace(), xmlSerializer.getName());
            }
        }

    }

    @Override
    public void register(SoapSerializationEnvelope soapSerializationEnvelope) {
        soapSerializationEnvelope.addMapping(soapSerializationEnvelope.xsd, "double", Vector.class, this);
    }
}
