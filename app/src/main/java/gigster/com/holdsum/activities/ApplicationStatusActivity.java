package gigster.com.holdsum.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import gigster.com.holdsum.R;
import gigster.com.holdsum.fragments.TopHeaderFragment;
import gigster.com.holdsum.helper.Logger;

/**
 * Created by Eshaan on 3/5/2016.
 */
public class ApplicationStatusActivity extends AppCompatActivity {

    // TODO: not yet used

    private TextView headerText;
    private ImageView backButton;
    private ImageView pageIcon;
    private TextView statusText;
    private TextView statusExplanationText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("Activity", "ApplicationStatusActivity.onCreate");
        setContentView(R.layout.activity_application_status);
        backButton = (ImageView) findViewById(R.id.back);
        pageIcon = (ImageView) findViewById(R.id.status_image);
        statusText = (TextView) findViewById(R.id.status_text);
        statusExplanationText = (TextView) findViewById(R.id.status_explanation);
        ((TopHeaderFragment)getFragmentManager().findFragmentById(R.id.top_bar_fragment)).setHeaderText(R.string.status);

        //TODO some network request
        Random r = new Random();
        int random = r.nextInt(3);
        switch (random) {
            case 0:

                break;
            case 1:
                break;
            case 2:
                break;
        }
    }
}
