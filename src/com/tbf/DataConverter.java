package com.tbf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataConverter {

    public static void main(String[] args) {
        //Creating a map of Persons
        Map<String, Person> personMap = FileReader.readPersonFile("data/Persons.dat");

        //Creating a map of Assets
        Map<String, Asset> assets = FileReader.readAssetFile("data/Assets.dat");

        //Calling the output methods
        FormattedXMLOutput.xmlConverterPerson(personMap);
        FormattedXMLOutput.xmlConverterAsset(assets);
        FormattedJsonFormat.jsonConverterPerson(personMap);
        FormattedJsonFormat.jsonConverterAsset(assets);
    }
}
