package com.dotTracePlugin.agent.tools;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
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


    public Object convertFromXMLToObject(String xmlFile, Class objClass) throws IOException {
        JAXBContext context = null;
        File file = new File(xmlFile);

        try {
            context = JAXBContext.newInstance(objClass);
            Unmarshaller um = context.createUnmarshaller();
            return objClass.cast(um.unmarshal(file));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }
}
