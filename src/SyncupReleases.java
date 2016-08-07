import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SyncupReleases {
	
	public static void main(String[] args) {
		

	      try {
	         File inputFile = new File("D://config.xml");
	         DocumentBuilderFactory dbFactory 
	            = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder;

	         dBuilder = dbFactory.newDocumentBuilder();

	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();

	         XPath xPath =  XPathFactory.newInstance().newXPath();

	         //String expression = "/class/student";
	         String expression = "/hudson/views/au.com.centrumsystems.hudson.plugin.buildpipeline.BuildPipelineView";
	         NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
	         Node node1 = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
	         //System.out.println(nodeToString(node1));
	         //Element eElement = (Element) node1;
             //System.out.println("Student roll no : "  + eElement.getAttribute("rollno"));
	         //System.out.println("Vaue is ........."+value);
	         StringBuilder strBuilder = new StringBuilder();
	         System.out.println(nodeList.toString());
	         for (int i = 0; i < nodeList.getLength(); i++) {
	            Node nNode = nodeList.item(i);
	        	 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                 Element eElement = (Element) nNode;
	                 System.out.println("new name................"+eElement.getElementsByTagName("name").item(0).getTextContent());
	                 if(matchPattern(eElement.getElementsByTagName("name").item(0).getTextContent()) == true){
	                	 continue;
	                 }
	                 else{
//                	 System.out.println("***************************");
//	                	 System.out.println("********new block*********");
//	                	 System.out.println("===========================");
//	     	            System.out.println(nodeToString(nNode));
//	    	            System.out.println("**************************");
//	    	            System.out.println("**************************");
//	    	            System.out.println("**************************");
	    	            strBuilder = strBuilder.append(nodeToString(nNode));
	    	            strBuilder = strBuilder.append("\n");
	    	            

	                 }
	        	 }
	         }
	         System.out.println(strBuilder);
	         writeToFile(strBuilder.toString());
	      } catch (ParserConfigurationException e) {
	         e.printStackTrace();
	      } catch (SAXException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (XPathExpressionException e) {
	         e.printStackTrace();
	      } catch (TransformerException e) {
			e.printStackTrace();
		}
	   
		
	}

	 private static String nodeToString(Node node)
			   throws TransformerException
	   {
	       StringWriter buf = new StringWriter();
	       Transformer xform = TransformerFactory.newInstance().newTransformer();
	       xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	       xform.transform(new DOMSource(node), new StreamResult(buf));
	       return(buf.toString());
	   }
	
	 
	 private static boolean matchPattern(String stringToSearch){
		    Pattern p = Pattern.compile("^(R(\\d)+)[ _](.*)");   // the pattern to search for
		    Matcher m = p.matcher(stringToSearch);
		    
		    // now try to find at least one match
		    if (m.find()){
		    	System.out.println("Found a match");
		    	return true;
		    }
		    else{
		    	System.out.println("Did not find a match");
		    	return false;
		    }
		  }
	 
	 private static void writeToFile(String appender) throws IOException{
		 String line = null;
		 String line2 = null;
         FileWriter fwj = new FileWriter("D://jobList.xml");
         BufferedWriter bwj = new BufferedWriter(fwj);
         FileReader fr = new FileReader("D://config.xml");
         BufferedReader br = new BufferedReader(fr);
         FileWriter fw = new FileWriter("D://config_new.xml");
         BufferedWriter bw = new BufferedWriter(fw);
         while ((line = br.readLine()) != null) {
        	 line2=line+"\n";
             if (line.contains("</views>")){
            	 line2=line;
            	 String[] lines = appender.split("\n");
            	 for(String tempLine: lines){
            		 System.out.println("Line>>>>>>>" + tempLine);
            		 if(tempLine.contains("<firstJob>")){
            			String sampleLine = tempLine;
            			//System.out.println("firstJob is............"+sampleLine.replaceAll("^\\s+", "").substring(10).split("<")[0]);
            			bwj.write(sampleLine.replaceAll("^\\s+", "").substring(10).split("<")[0]+"\n");
            			bwj.flush();
            		 }
            		 bw.write(tempLine);
            		 bw.flush();
            	 }
            	 //line2.replace("</views>","*******************"+appender+"\n </views>\n");
            	 bw.write("\n  </views>\n");
            	 bw.flush();
             }
             else{
            	 bw.write(line2);
            	 bw.flush();
             }
             
         }  //end if                 

	 }
	 
}
