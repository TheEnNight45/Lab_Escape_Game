package com.example.mygame;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GameManager gameManager;
    private Button[] choiceButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Загружаем историю
        StoryLoader.loadStory(this);

        gameManager = new GameManager();
        choiceButtons = new Button[]{
                findViewById(R.id.btnChoice1),
                findViewById(R.id.btnChoice2),
                findViewById(R.id.btnChoice3)
        };

        updateScene();
    }

    private void updateScene() {
        // Обновляем текст
        ((TextView) findViewById(R.id.tvStory)).setText(gameManager.getSceneText());

        // Обновляем кнопки
        String[] choices = gameManager.getChoiceTexts();
        for (int i = 0; i < choiceButtons.length; i++) {
            if (i < choices.length) {
                choiceButtons[i].setText(choices[i]);
                choiceButtons[i].setVisibility(View.VISIBLE);
                final int choiceIndex = i;
                choiceButtons[i].setOnClickListener(v -> {
                    gameManager.handleChoice(choiceIndex);
                    updateScene();
                });
            } else {
                choiceButtons[i].setVisibility(View.GONE);
            }
        }
    }
}