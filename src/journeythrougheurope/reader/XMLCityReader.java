/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journeythrougheurope.reader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import journeythrougheurope.botalgorithm.Vertex;
import journeythrougheurope.game.JourneyThroughEuropeCity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Karl
 */
public class XMLCityReader {

    private HashMap<String, JourneyThroughEuropeCity> cityHashMap;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private File xmlFileName;

    public XMLCityReader(String xmlFileName) {
       
        try {
            cityHashMap = new HashMap<String, JourneyThroughEuropeCity>(240);
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            
            this.xmlFileName = new File(xmlFileName);
            document = documentBuilder.parse(xmlFileName);
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLCityReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLCityReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLCityReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buildCityHashMap() {   
        NodeList cityDataList = document.getElementsByTagName("citydata");
        for (int i = 0; i < cityDataList.getLength(); i++) {
           
            JourneyThroughEuropeCity city = new JourneyThroughEuropeCity();
           
            Node cityData = cityDataList.item(i);
            if (cityData.getNodeType() == Node.ELEMENT_NODE) {
                Element cityDataElement = (Element) cityData;
                NodeList cityDataNameList = cityDataElement.getElementsByTagName("name");
                Element cityDataNameElement = (Element) cityDataNameList.item(0);
                NodeList cityDataName = cityDataNameElement.getChildNodes();
                city.setCityName(cityDataName.item(0).getNodeValue().toUpperCase().trim());
            }

            NodeList cityLand = cityData.getChildNodes();
            for (int j = 0; j < cityLand.getLength(); j++) {
                Node cityLandNode = cityLand.item(j);

                if ((cityLandNode.getNodeType() == Node.ELEMENT_NODE) && cityLandNode.getNodeName().equalsIgnoreCase("land")) {
                    Element cityLandElement = (Element) cityLandNode;
                    NodeList cityLandList = cityLandElement.getElementsByTagName("city");
                    for (int k = 0; k < cityLandList.getLength(); k++) {
                        Element cityLandElementTemp = (Element) cityLandList.item(k);
                        NodeList cityLandNames = cityLandElementTemp.getChildNodes();
                        city.addNeighboringLandCity(cityLandNames.item(0).getNodeValue().toUpperCase().trim());
                    }
                }
            }

            NodeList citySea = cityData.getChildNodes();
            for (int j = 0; j < citySea.getLength(); j++) {
                Node citySeaNode = citySea.item(j);

                if ((citySeaNode.getNodeType() == Node.ELEMENT_NODE) && citySeaNode.getNodeName().equalsIgnoreCase("sea")) {
                    Element citySeaElement = (Element) citySeaNode;
                    NodeList citySeaList = citySeaElement.getElementsByTagName("city");
                    for (int k = 0; k < citySeaList.getLength(); k++) {
                        Element citySeaElementTemp = (Element) citySeaList.item(k);
                        NodeList citySeaNames = citySeaElementTemp.getChildNodes();
                        city.addNeighboringSeaCity(citySeaNames.item(0).getNodeValue().toUpperCase().trim());
                    }
                }
            }
            city.setVertex(new Vertex(city.getCityName()));
            cityHashMap.put(city.getCityName(),city);
        }
    }

    public JourneyThroughEuropeCity getCity(String cityName) {
        JourneyThroughEuropeCity temp = cityHashMap.get(cityName.toUpperCase().trim());
        if (temp == null) {
            throw new NullPointerException("The requested city is not contained in this hashmap.\t" + cityName);
        }
        return temp;
    }
    
    public HashMap<String,JourneyThroughEuropeCity> getCityHashMap()
    {
        return cityHashMap;
    }

}
