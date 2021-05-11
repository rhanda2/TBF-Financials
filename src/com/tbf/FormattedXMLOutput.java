package com.tbf;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class contains the methods to output in an XML file
 */
public class FormattedXMLOutput {


    /**
     * Converts data stored in a list to an XML output file
     * @param personsMap
     */
    public static void xmlConverterPerson(Map<String, Person> personsMap) {

        // New XStream
        XStream xstream = new XStream();

        // File reader for XML
        File xmlOutput = new File("data/Persons.xml");

        PrintWriter xmlPrintWriter = null; // writer

        try { // file try
            xmlPrintWriter = new PrintWriter(xmlOutput);
        } catch (FileNotFoundException e) { // Catch statement for file not found exception
            e.printStackTrace();
        }


        // XML encoder
        xmlPrintWriter.write("<?xml version=\"1.0\" ?>\n");

        xmlPrintWriter.write("<persons>");

        xstream.alias("Person", Person.class);

        for (Person person : personsMap.values()) {
            // Use toXML method to convert Person object into a String

            String personOutput = xstream.toXML(person);
            xmlPrintWriter.write(personOutput);
            xmlPrintWriter.write("\n");
        }
        xmlPrintWriter.write("</persons>");
        xmlPrintWriter.close();// Writer closes
    }

    /**
     * Converts data stored in a list to a XML output file
     * @param assetsMap
     */
    public static void xmlConverterAsset(Map<String, Asset> assetsMap) {

        // New XStream
        XStream xstream = new XStream();

        // File reader for XML
        File xmlOutput = new File("data/Assets.xml");

        PrintWriter xmlPrintWriter = null; // writer

        try { // file try
            xmlPrintWriter = new PrintWriter(xmlOutput);
        } catch (FileNotFoundException e) { // Catch statement for file not found exception
            e.printStackTrace();
        }

        // XML encoder
        xmlPrintWriter.write("<?xml version=\"1.0\" ?>\n");

        xmlPrintWriter.write("<assets>\n");

        xstream.alias("Asset" , Asset.class);

        for (Asset asset : assetsMap.values()) {
            // Use toXML method to convert Person object into a String

            String assetOutput = xstream.toXML(asset);
            xmlPrintWriter.write(assetOutput);
            xmlPrintWriter.write("\n");
        }
        xmlPrintWriter.write("</assets>");
        xmlPrintWriter.close();// Writer closes
    }

}

