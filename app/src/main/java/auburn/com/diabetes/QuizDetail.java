package auburn.com.diabetes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QuizDetail extends AppCompatActivity implements View.OnClickListener{
    private TextView quizResult;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab) {
            int correct = getIntent().getIntExtra("correctAnswer",0);
            int quizNumber = getIntent().getIntExtra("quizNum",0);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "My Quiz Report "+new java.util.Date().toString());
            intent.putExtra(Intent.EXTRA_TEXT, "Hello\n    I just finished my quizs." + "There are " + quizNumber + " quizs\n I have " + correct + " correct.");
            try{
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizdetail_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initilizeComponents();

    }

    private void initilizeComponents() {
        quizResult = (TextView) findViewById(R.id.quizresult);

        int correct = getIntent().getIntExtra("correctAnswer",0);
        int quizNumber = getIntent().getIntExtra("quizNum",0);
        quizResult.setText("There are " + quizNumber + " quizs\n You have " + correct + " correct.");
    }
}
