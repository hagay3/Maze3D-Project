package view;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import presenter.Properties;

/**
 * XMLManager will be used for writing and reading from xml.
 */

public class XMLManager {
	private Properties properties;
	private String filename;
	/**
	 * readXML for reading xml file and load it to object Properties
	 * @param filenameIn string that contains filename and path
	 * @throws FileNotFoundException in this case default values will be used.
	 * @throws ClassCastException 
	 */
	public void readXML(String filenameIn)  throws FileNotFoundException,ClassCastException {
			Properties prop;
			XMLDecoder xmlDecoder = null;
			setFilename(filenameIn);
			try{
				FileInputStream fis = new FileInputStream(filename);
				BufferedInputStream bis = new BufferedInputStream(fis);
				xmlDecoder = new XMLDecoder(bis);
				prop = (Properties) xmlDecoder.readObject();
				setProperties(prop);
			}
			catch (ClassCastException e){
				e.printStackTrace();
			}catch (FileNotFoundException e){
				prop = new Properties();
				writeXML(prop);
			}
			finally{
				if(xmlDecoder != null)
					xmlDecoder.close();
			}
	}
	
	public  void writeXML(Properties propToWrite)  throws FileNotFoundException,ClassCastException {
		
		XMLEncoder xmlEncoder = null;
		try {
			xmlEncoder = new XMLEncoder(new FileOutputStream(filename));
			xmlEncoder.writeObject(propToWrite);
			xmlEncoder.flush();
			setProperties(propToWrite);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(xmlEncoder != null)
				xmlEncoder.close();
		}
}
	public Properties getProperties() {
		return properties;
	}


	public  void setProperties(Properties properties) {
		this.properties = properties;
	}


	public  String getFilename() {
		return filename;
	}


	public  void setFilename(String filename) {
		this.filename = filename;
	}
}
	
