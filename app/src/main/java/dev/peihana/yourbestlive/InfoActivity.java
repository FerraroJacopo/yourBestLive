package dev.peihana.yourbestlive;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class InfoActivity extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        b=(Button) findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new LibsBuilder()
                            .withLicenseShown(true)
                            .withAboutIconShown(true)
                            .withAutoDetect(true)
                            .withAboutVersionShown(true)
                            .withAboutDescription("Bookmark icon:<br />Lucy G<br />thttp://www.flaticon.com/free-icon/bookmark_118732\n" +
                                    "Gmail icon:<br />Cornmanthe3rd<br />http://cornmanthe3rd.deviantart.com/<br />" +
                                    "Facebook icon:<br />freepik<br />http://www.flaticon.com/free-icon/facebook_174848<br />" +
                                    "Earth icon:<br />freepik<br />http://www.flaticon.com/free-icon/planet-earth_263080")
                            .withActivityTitle("Licenze open source")
                            .withActivityTheme(R.style.AppTheme)
                            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                            .start(InfoActivity.this);
                }
            });
//
            }

}
