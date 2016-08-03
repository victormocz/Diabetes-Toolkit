package model;


import java.util.Random;

/**
 * Created by victorm on 4/20/16.
 */
public class Question{
    private String quiz;
    private String[] options = new String[4];
    private String answer;

    public Question(String quiz, String optionA, String optionB, String optionC, String optionD, String answer) {
        this.quiz = quiz;
        this.options[0] = optionA;
        this.options[1] = optionB;
        this.options[2] = optionC;
        this.options[3] = optionD;
        this.answer = answer;
    }
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }


    public String getOptionA() {
        return options[0];
    }

    public String getOptionB() {
        return options[1];
    }

    public String getOptionC() {
        return options[2];
    }

    public String getOptionD() {
        return options[3];
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void shuffle() {
        Random rd = new Random();
        for (int i = 0; i < options.length; i++) {
            String temp = options[i];
            int num = rd.nextInt(i + 1);
            options[i] =  options[num];
            options[num] = temp;
        }
    }
}
