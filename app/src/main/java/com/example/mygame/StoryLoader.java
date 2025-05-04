package com.example.mygame;

import android.content.Context;
import android.content.res.Resources;
import com.google.gson.Gson;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class StoryLoader {
    private static StoryData storyData;

    public static void loadStory(Context context) {
        try {
            Resources res = context.getResources();
            InputStream is = res.openRawResource(R.raw.story);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            storyData = new Gson().fromJson(json, StoryData.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки story.json", e);
        }
    }

    public static Scene getScene(String id) {
        if (storyData == null || storyData.scenes == null || !storyData.scenes.containsKey(id)) {
            return null;
        }
        return storyData.scenes.get(id);
    }

    public static String getItemDescription(String itemId) {
        if (storyData == null || storyData.items == null) return null;
        Item item = storyData.items.get(itemId);
        return item != null ? item.description : null;
    }

    public static class StoryData {
        public Map<String, Scene> scenes;
        public Map<String, Item> items;
    }

    public static class Scene {
        public String text;
        public List<Choice> choices;
    }

    public static class Choice {
        public String text;
        public String nextScene;
        public String addItem;
        public String requiredItem;
    }

    public static class Item {
        public String description;
    }
}