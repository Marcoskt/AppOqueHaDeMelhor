package appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import appoquehademelhorbsb.studio.com.appoquehademelhorbsb.R;
import appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Helper.Permissao;

public class MainActivity extends AppCompatActivity {

    private Button btMain;
    private String[] permissoesNecessarias = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.INTERNET

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Permissao.validaPermissoes(1,this,permissoesNecessarias);


        btMain = (Button)findViewById(R.id.bt_restaurante);
        btMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RestauranteActivity.class);

                startActivity(intent);
                Toast.makeText(MainActivity.this,"Se quiser ver sua posição clique na bolinha ao canto superior direito da tela, mas lembre-se de ligar seu GPS ",Toast.LENGTH_LONG).show();
            }
        });
    }

}

