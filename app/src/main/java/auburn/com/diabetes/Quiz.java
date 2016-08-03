package auburn.com.diabetes;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Question;

public class Quiz extends AppCompatActivity implements View.OnClickListener {
    private int quizIndex = 0;
    private int correctAnswer = 0;
    private List<Question> quizs;
    private TextView quiz;
    private RadioButton optiona;
    private RadioButton optionb;
    private RadioButton optionc;
    private RadioButton optiond;
    private RadioGroup options;
    private Button submit;
    private TextView quizStatus;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.submitquiz) {
            int checkedid = options.getCheckedRadioButtonId();
            if (checkedid == -1) {
                Toast.makeText(getApplicationContext(),"Please select one",Toast.LENGTH_SHORT).show();
                return;
            }

            if (quizIndex == quizs.size() - 1) {
                Intent intent = new Intent(getApplicationContext(),QuizDetail.class);
                intent.putExtra("correctAnswer", correctAnswer);
                intent.putExtra("quizNum",quizs.size());
                startActivity(intent);
                return;
            }
            Question question =  quizs.get(quizIndex);

            RadioButton rb = (RadioButton)findViewById(checkedid);
            if (rb.getText().equals(question.getAnswer())) {
                correctAnswer++;
            } else {
                Toast.makeText(getApplicationContext(),"Wrong, Correct Answer: " + question.getAnswer(),Toast.LENGTH_LONG).show();
            }
            quizIndex++;
            updateQuiz();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeComponents();
    }

    private List<Question> getQuizs() {
        List<Question> quizlist= new ArrayList<Question>();
        AssetManager manager = getApplicationContext().getAssets();
        try{
            InputStream inputstream = manager.open("quizs.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
            String line;

            while ((line = br.readLine()) != null) {
                String[] column = line.split("\\t");
                if (column.length == 6){
                    quizlist.add(new Question(column[0],column[1],column[2],column[3],column[4],column[5]));
                }
            }
        } catch (IOException ex) {
            Log.v("IO ERROR in Quizlist", ex.getMessage());
        }
        return quizlist;
    }

    private void initializeComponents() {
        quizs = getQuizs();
        Collections.shuffle(quizs);

        quiz = (TextView) findViewById(R.id.quiz);
        options = (RadioGroup) findViewById(R.id.options);
        optiona = (RadioButton) findViewById(R.id.optiona);
        optionb = (RadioButton) findViewById(R.id.optionb);
        optionc = (RadioButton) findViewById(R.id.optionc);
        optiond = (RadioButton) findViewById(R.id.optiond);
        submit = (Button) findViewById(R.id.submitquiz);
        submit.setOnClickListener(this);

        quizStatus = (TextView) findViewById(R.id.quizstatus);
        updateQuiz();
    }

    private void updateQuiz() {
        Question question =  quizs.get(quizIndex);
        question.shuffle();
        quiz.setText(question.getQuiz());
        optiona.setText(question.getOptionA());
        optionb.setText(question.getOptionB());
        optionc.setText(question.getOptionC());
        optiond.setText(question.getOptionD());
        quizStatus.setText("Question: " + (quizIndex + 1) + " / " + quizs.size());
        options.clearCheck();
    }


}
