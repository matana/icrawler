package de.uni_koeln.phil_fak.info.icrawler.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class WebDocMarshaller {
	
	public static void toXML(Class<?> clazz, File outPutFile, boolean console) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		if(console)
			marshaller.marshal(clazz, System.out);
		marshaller.marshal(clazz, outPutFile);
	}

}
