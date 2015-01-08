package com.glevel.dungeonhero.models.discussions.riddles;

import com.glevel.dungeonhero.models.Reward;

/**
 * Created by guillaume on 12/2/14.
 */
public class OpenRiddle extends Riddle {

    private final String correctAnswer;

    public OpenRiddle(int timer, String question, String correctAnswer, Reward reward) {
        super(timer, question, reward);
        this.correctAnswer = correctAnswer;
    }

    public boolean isAnswerCorrect(String answer) {
        return formatAnswer(correctAnswer).equals(formatAnswer(answer));
    }

    private static String formatAnswer(String answerToFormat) {
        return answerToFormat.toLowerCase().replaceAll(" ", "");
    }

}
