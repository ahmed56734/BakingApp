package com.example.ahmed.bakingapp.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 6/26/17.
 */

public class Recipe implements Parcelable {

    private String id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private String servingsNumber;
    private String imageName;

    public Recipe(String id, String name, List<Ingredient> ingredients, List<Step> steps, String servingsNumber, String imageName) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servingsNumber = servingsNumber;
        this.imageName = imageName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageName(){
        return imageName;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public String getServingsNumber() {
        return servingsNumber;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + servingsNumber + "\n" + ingredients + "\n" + steps.toString();
    }

    public static JsonReader getJsonReader(Context context) throws IOException {

        AssetManager assetManager = context.getAssets();
        String uri = null;


        try {
            for (String asset : assetManager.list("")) {
                if (asset.endsWith("baking.json")) {
                    uri = "asset:///" + asset;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String userAgent = Util.getUserAgent(context, "BakingApp");
        DataSource dataSource = new DefaultDataSource(context, null, userAgent, false);
        DataSpec dataSpec = new DataSpec(Uri.parse(uri));
        InputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        } finally {
            Util.closeQuietly(dataSource);
        }

        return  reader;

    }

    public static List<Recipe> parseJson(Context context) throws  IOException{

        List<Recipe> recipes = new ArrayList<Recipe>();
        JsonReader reader = getJsonReader(context);

        reader.beginArray();
        while (reader.hasNext()){
            recipes.add(readRecipe(reader));
        }
        reader.endArray();

        return  recipes;
    }

    public static Recipe getRecipeById(Context context, String id ) throws IOException{
        JsonReader reader = getJsonReader(context);
        Recipe recipe = null;
        reader.beginArray();
        while (reader.hasNext()){
            Recipe tempRecipe = readRecipe(reader);
            if(tempRecipe.getId().equals(id))
                recipe = tempRecipe;
        }
        reader.endArray();

        return  recipe;
    }

    private static Recipe readRecipe(JsonReader reader) throws IOException {
        String id = null;
        String name = null;
        List<Ingredient> ingredients = null;
        List<Step> steps = null;
        String servingsNumber =null;
        String imageName = null;

        reader.beginObject();
        while (reader.hasNext()){
            String tokenName = reader.nextName();

            if(tokenName.equals("id"))
                id = reader.nextString();
            else if (tokenName.equals("name"))
                name = reader.nextString();
            else if(tokenName.equals("servings"))
                servingsNumber = reader.nextString();
            else if(tokenName.equals("ingredients"))
                ingredients = readIngredients(reader);
            else if(tokenName.equals("steps"))
                steps = readSteps(reader);
            else if(tokenName.equals("image"))
                imageName = reader.nextString();

        }
        reader.endObject();

        return new Recipe(id, name, ingredients, steps, servingsNumber, imageName);

    }

    private static List<Step> readSteps(JsonReader reader) throws IOException {
        List<Step> steps = new ArrayList<Step>();
        String id = null;
        String shortDescription = null;
        String description = null;
        String videoURL = null;
        String thumbnailURL = null;

        reader.beginArray();
        while (reader.hasNext()){

            reader.beginObject();
            while (reader.hasNext()){
                String tokenName = reader.nextName();
                switch (tokenName){
                    case "id":
                        id = reader.nextString(); break;
                    case "shortDescription":
                        shortDescription = reader.nextString(); break;
                    case "description":
                        description = reader.nextString(); break;
                    case "videoURL":
                        videoURL = reader.nextString(); break;
                    case "thumbnailURL":
                        thumbnailURL = reader.nextString(); break;
                }
            }
            steps.add(new Step(id, shortDescription, description, videoURL, thumbnailURL));
            reader.endObject();

        }
        reader.endArray();


        return steps;
    }

    private static List<Ingredient> readIngredients(JsonReader reader) throws IOException {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        String quantity = null;
        String measure = null;
        String ingredientName = null;

        reader.beginArray();
        while (reader.hasNext()){

            reader.beginObject();
            while (reader.hasNext()) {
                String tokenName = reader.nextName();
                if (tokenName.equals("quantity"))
                    quantity = reader.nextString();
                else if (tokenName.equals("measure"))
                    measure = reader.nextString();
                else if (tokenName.equals("ingredient"))
                    ingredientName = reader.nextString();
            }
            ingredients.add(new Ingredient(quantity, measure, ingredientName));
            reader.endObject();

        }
        reader.endArray();

        return  ingredients;
    }

    protected Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<Ingredient>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
        if (in.readByte() == 0x01) {
            steps = new ArrayList<Step>();
            in.readList(steps, Step.class.getClassLoader());
        } else {
            steps = null;
        }
        servingsNumber = in.readString();
        imageName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }
        dest.writeString(servingsNumber);
        dest.writeString(imageName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}