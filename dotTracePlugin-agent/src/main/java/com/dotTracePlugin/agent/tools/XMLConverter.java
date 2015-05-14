package com.dotTracePlugin.agent.tools;



import com.dotTracePlugin.agent.model.ReportPattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Alexey.Totin on 5/11/2015.
 */
public class XMLConverter {

    public void convertFromObjectToXML(Object object, String filePath)
            throws IOException {

        JAXBContext context = null;

        try {
            context = JAXBContext.newInstance(object.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            m.marshal(object, new File(filePath));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    public Object convertFromXMLToObject(String xmlFile) throws IOException {

//        FileInputStream is = null;
//        try {
//            is = new FileInputStream(xmlFile);
//            return unmarshaller.unmarshal(new StreamSource(is));
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
        return new Object();
    }
}
