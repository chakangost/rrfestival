package kr.rrcoporation.rrfestival.festival.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kr.rrcoporation.rrfestival.festival.R;

public class LoginActivity extends CommonFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button goMain = (Button) findViewById(R.id.button);
        goMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FragmentContainerActivity.class);
                startActivity(intent);
            }
        });
    }
}
