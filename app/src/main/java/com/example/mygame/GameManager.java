package com.example.mygame;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private String currentSceneId = "0";
    private final Map<String, Boolean> inventory = new HashMap<>();
    private final int health = 100;

    public String getSceneText() {
        StoryLoader.Scene scene = StoryLoader.getScene(currentSceneId);
        if (scene == null) {
            return "ОШИБКА: Сцена не найдена! ID: " + currentSceneId;
        }

        StringBuilder sb = new StringBuilder(scene.text);

        if (!inventory.isEmpty()) {
            sb.append("\n\nИнвентарь:");
            for (String item : inventory.keySet()) {
                sb.append("\n- ").append(item);
                String description = StoryLoader.getItemDescription(item);
                if (description != null) {
                    sb.append(" (").append(description).append(")");
                }
            }
        }
        sb.append("\n\nЗдоровье: ").append(health).append("/100");
        return sb.toString();
    }

    public String[] getChoiceTexts() {
        StoryLoader.Scene scene = StoryLoader.getScene(currentSceneId);
        if (scene == null || scene.choices == null) {
            return new String[]{"Продолжить"};
        }

        String[] choices = new String[scene.choices.size()];
        for (int i = 0; i < choices.length; i++) {
            choices[i] = scene.choices.get(i).text != null ?
                    scene.choices.get(i).text : "???";
        }
        return choices;
    }

    public boolean isChoiceEnabled(int choiceIndex) {
        StoryLoader.Scene scene = StoryLoader.getScene(currentSceneId);
        if (scene == null || scene.choices == null || choiceIndex >= scene.choices.size()) {
            return false;
        }
        StoryLoader.Choice choice = scene.choices.get(choiceIndex);
        return choice.requiredItem == null || inventory.containsKey(choice.requiredItem);
    }

    public void handleChoice(int choiceIndex) {
        StoryLoader.Scene scene = StoryLoader.getScene(currentSceneId);
        if (scene == null || scene.choices == null || choiceIndex >= scene.choices.size()) {
            currentSceneId = "0";
            return;
        }

        StoryLoader.Choice choice = scene.choices.get(choiceIndex);
        if (choice.requiredItem != null && !inventory.containsKey(choice.requiredItem)) {
            return;
        }

        if (choice.addItem != null) {
            inventory.put(choice.addItem, true);
        }

        currentSceneId = choice.nextScene != null ? choice.nextScene : "0";
    }

    public int getHealth() {
        return health;
    }

    public Map<String, Boolean> getInventory() {
        return inventory;
    }
}