package com.noam.noamproject1.screens;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.noam.noamproject1.R;

import java.util.Random;

public class SuitcasesAndBombs extends AppCompatActivity {
    private ImageView player, suitcase, bomb;
    private TextView scoreText, livesText;
    private int score = 0, lives = 3;
    private float playerX;
    private boolean isMovingLeft = false, isMovingRight = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitcasesandbombs);

        player = findViewById(R.id.player);
        suitcase = findViewById(R.id.suitcase);
        bomb = findViewById(R.id.bomb);
        scoreText = findViewById(R.id.score);
        livesText = findViewById(R.id.lives);
        Button leftButton = findViewById(R.id.leftButton);
        Button rightButton = findViewById(R.id.rightButton);

        player.post(() -> {
            playerX = player.getX();
            resetObjectPosition(suitcase);
            resetObjectPosition(bomb);
        });

        leftButton.setOnTouchListener((v, event) -> {
            isMovingLeft = event.getAction() != MotionEvent.ACTION_UP;
            return true;
        });

        rightButton.setOnTouchListener((v, event) -> {
            isMovingRight = event.getAction() != MotionEvent.ACTION_UP;
            return true;
        });

        startGameLoop();
    }

    private void startGameLoop() {
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateGame();
                handler.postDelayed(this, 50);
            }
        }, 50);
    }

    private void updateGame() {
        if (isMovingLeft && playerX > 0) {
            playerX -= 20;
        }
        if (isMovingRight && playerX < findViewById(R.id.gameLayout).getWidth() - player.getWidth()) {
            playerX += 20;
        }
        player.setX(playerX);

        moveObject(suitcase);
        moveObject(bomb);

        checkCollision(suitcase, true);
        checkCollision(bomb, false);
    }

    private void moveObject(ImageView object) {
        float newY = object.getY() + 15;
        if (newY > findViewById(R.id.gameLayout).getHeight()) {
            resetObjectPosition(object);
        } else {
            object.setY(newY);
        }
    }

    private void checkCollision(ImageView object, boolean isSuitcase) {
        if (Math.abs(object.getX() - player.getX()) < 80 && Math.abs(object.getY() - player.getY()) < 80) {
            if (isSuitcase) {
                score++;
                scoreText.setText("Score: " + score);
            } else {
                lives--;
                livesText.setText("Lives: " + lives);
                if (lives <= 0) {
                    gameOver();
                }
            }
            resetObjectPosition(object);
        }
    }

    private void resetObjectPosition(ImageView object) {
        player.post(() -> {
            object.setX(random.nextInt(findViewById(R.id.gameLayout).getWidth() - object.getWidth()));
            object.setY(0);
        });
    }

    private void gameOver() {
        scoreText.setText("Game Over! Score: " + score);
    }
}
