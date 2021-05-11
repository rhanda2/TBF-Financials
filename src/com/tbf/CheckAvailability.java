package com.tbf;

import java.util.HashMap;
import java.util.Map;

/**
 * This class checks if the constituents of a portfolio exist previously or not
 */
public class CheckAvailability {

    /**
     * Returns the asset having a particular assetCode
     * @param assetCode
     * @return
     */
    public static Asset checkAsset(String assetCode){
        Map<String, Asset> assetMap= new HashMap<>();
        assetMap = FileReader.readAssetFile("data/Assets.dat");
        Asset asset;
        for (String key : assetMap.keySet()) {
            if(key.equals(assetCode)) {
                asset = assetMap.get(key);
                return asset;
            }
        }
        return null;
    }

    /**
     * Returns the person having a particular personCode
     * @param personCode
     * @return
     */
    public static Person checkPerson(String personCode){
        Map<String, Person> personMap = new HashMap<>();
        personMap = FileReader.readPersonFile("data/Persons.dat");
        Person person;
        for (String key : personMap.keySet()){
            if(key.equals(personCode)) {
                person = personMap.get(key);
                return person;
            }
        }
        return null;
    }

    /**
     * Returns the person having a particular personCode and also checking if it is a manager.
     * @param personCode
     * @return
     */
    public static Person checkManager(String personCode){
        Person person = checkPerson(personCode);
        if(person instanceof JuniorBroker || person instanceof ExpertBroker) {
            return person;
        }
        return null;
    }
}
