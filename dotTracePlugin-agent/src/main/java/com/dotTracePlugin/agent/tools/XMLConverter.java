package com.dotTracePlugin.agent.tools;



import com.dotTracePlugin.agent.model.ProfiledMethod;
import com.dotTracePlugin.agent.model.ReportPattern;
import com.dotTracePlugin.agent.model.ProfiledMethodList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;


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


    public Object convertFromXMLToObject(String xmlFile, Class objClass) throws IOException {
//    public ProfiledMethodList convertFromXMLToObject(String xmlFile) throws IOException {

        JAXBContext context = null;
        File file = new File(xmlFile);

        try {
//            context = JAXBContext.newInstance(ProfiledMethodList.class);
            context = JAXBContext.newInstance(objClass);
            Unmarshaller um = context.createUnmarshaller();
            return objClass.cast(um.unmarshal(file));
//            return (ProfiledMethodList) um.unmarshal(file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }
}
