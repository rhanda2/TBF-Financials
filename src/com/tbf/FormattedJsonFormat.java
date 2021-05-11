package com.tbf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * This class contains methods to output json files
 */
public class FormattedJsonFormat {

    /**
     * Converts data stored in a list to a JSON output file
     * @param persons
     */
    public static void jsonConverterPerson(Map<String, Person> persons) {

        // GSON Imports for printing in Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File jsonOutput = new File("data/Persons.json");

        PrintWriter jsonPrintWriter = null;

        try {
            jsonPrintWriter = new PrintWriter(jsonOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        jsonPrintWriter.write("{\n" + "\"persons\": [\n");
        int i = 0;
        for (Person person : persons.values()) {
            // Use toJson method to convert Person object into a String
            String personOutput = gson.toJson(person);
            if(i++ == persons.size() - 1){
                jsonPrintWriter.write(personOutput + "\n");
            } else {
                jsonPrintWriter.write(personOutput + ",\n");
            }
        }
        jsonPrintWriter.write("] }");

        jsonPrintWriter.close();
    }

    /**
     * Converts data stored in a list to a JSON output file
     * @param assets
     */
    public static void jsonConverterAsset(Map<String, Asset> assets) {

        // GSON Imports for printing in Json
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File jsonOutput = new File("data/Assets.json");

        PrintWriter jsonPrintWriter = null;

        try {
            jsonPrintWriter = new PrintWriter(jsonOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        jsonPrintWriter.write("{\n" + "\"assets\": [\n");
        int i = 0;
        for (Asset asset : assets.values()) {
            // Use toJson method to convert Person object into a String
            String assetOutput = gson.toJson(asset);
            if(i++ == assets.size() - 1){
                jsonPrintWriter.write(assetOutput + "\n");
            } else {
                jsonPrintWriter.write(assetOutput + ",\n");
            }
        }
        jsonPrintWriter.write("] }");

        jsonPrintWriter.close();
    }
}
