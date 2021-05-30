import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;


public class XML {

	public static void main(String[] args) throws JsonProcessingException {
	    Data userData = new Data();
	    Map<String,String> nameStruct = new HashMap<String,String>();
	    nameStruct.put("first", "Joe");
	    nameStruct.put("last", "Sixpack");
	    userData.put("name", nameStruct);
	    userData.put("gender", "MALE");
	    userData.put("verified", Boolean.FALSE);
	    userData.put("userImage", "Rm9vYmFyIQ==");
	    XmlMapper xmlMapper = new XmlMapper();
	    xmlMapper.configure( ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true );
	    String xml = xmlMapper.writeValueAsString(userData);
	    System.out.println(xml);
	}
}
