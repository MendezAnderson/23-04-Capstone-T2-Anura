package com.anura;

import com.anura.main.Helper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.anura.*;
import org.fusesource.jansi.Ansi;

import java.io.FileReader;
import java.util.Scanner;

public class GameObject {
    // fields and attributes
    private String name;
    private String item;
    private String description;

    public GameObject(String type, String item) {
        this.name = type;
        this.item = item;
        this.description = readDescriptionFromJsonFile();
    }

    private String readDescriptionFromJsonFile() {
        try {
            Gson gson = new Gson();
            FileReader fileReader = new FileReader(getJsonFilePath(name));
            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);

            return jsonObject.get(item).getAsJsonObject().get("desc").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Description not found!";
    }

    private String getJsonFilePath(String objectType) {
        String basePath = "src/main/resources/";
        String itemName;

        switch (objectType) {
            case "item":
                itemName = "item.json";
                break;
            case "location":
                itemName = "Location.json";
                break;
            case "npc":
                itemName = "npcs.json";
                break;
            default:
                throw new IllegalArgumentException("Invalid object type: " + objectType);
        }

        return basePath + itemName;
    }

    // Getter for the description
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}