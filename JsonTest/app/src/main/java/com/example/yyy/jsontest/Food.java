package com.example.yyy.jsontest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YYY on 2017/7/22.
 */

public class Food {
    //"food_description":"Per 100g - Calories: 89kcal | Fat: 0.33g | Carbs: 22.84g | Protein: 1.09g""food_name":"Bananas"
    String Name;//Bananas
    int heat;   //89
    int fat;    //0.33
    int protein;//1.09
    int carbs;  //22.84
    String description;// Fat: 0.33g | Carbs: 22.84g | Protein: 1.09g

    public Food(String Name,int heat,int fat,int protein,int carbs,String description) {
        this.carbs = carbs;
        this.description = description;
        this.fat = fat;
        this.heat = heat;
        this.Name = Name;
        this.protein = protein;
    }

    public static Food JsontoFood(JSONObject object) {
        String food_description = object.optString("food_description");
        String food_name = object.optString("food_name");

        String des = food_description.split("-")[1];//description

        String[] item = des.split("\\|");//Calories,Fat,Carbs

        int heat = getNumber(item[0]);
        int fat = getNumber(item[1]);
        int carbs = getNumber(item[2]);
        int protein = getNumber(item[3]);

        return new Food(food_name,heat,fat,protein,carbs,food_description);
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getheat() {
        return heat;
    }

    public void setheat(int heat) {
        this.heat = heat;
    }

    public int getfat() {
        return fat;
    }

    public void setfat(int fat) {
        this.fat = fat;
    }

    public int getprotein() {
        return protein;
    }

    public void setprotein(int protein) {
        this.protein = protein;
    }

    public int getcarbs() {
        return carbs;
    }

    public void setcarbs(int carbs) {
        this.carbs = carbs;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public static int getNumber(String text) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        int after=Integer.valueOf(m.replaceAll("").trim());
        return after;
    }

}
