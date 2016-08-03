package auburn.com.diabetes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

/**
 * Created by victorm on 4/14/16.
 */
public class Setting extends Fragment {
    private TableRow privacy;
    private TableRow about;
    private TableRow acknowledgements;

    private final String privacyContent = "Privacy policy goes here";
    private final String aboutContent = "Auburn University\n Advisor: Dr. Cheryl Seals, Dr. Richard Chapman\n Author:Chunzhang Mo\n Inspired from Jacob Conaway";
    private final String ackContent = "Auburn University";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_setting,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeComponents();
    }

    private void initializeComponents() {
        privacy = (TableRow) getView().findViewById(R.id.privacy);
        about = (TableRow) getView().findViewById(R.id.about);
        acknowledgements = (TableRow) getView().findViewById(R.id.ack);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SettingDetail.class);
                intent.putExtra("title","Privacy Policy");
                intent.putExtra("content",privacyContent);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SettingDetail.class);
                intent.putExtra("title","About");
                intent.putExtra("content",aboutContent);
                startActivity(intent);
            }
        });

        acknowledgements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SettingDetail.class);
                intent.putExtra("title","Acknowledgements");
                intent.putExtra("content",ackContent);
                startActivity(intent);
            }
        });
    }
}
