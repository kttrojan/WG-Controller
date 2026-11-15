package pl.example.wgcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String PERMISSION =
        "com.wireguard.android.permission.CONTROL_TUNNELS";

    private static final String DOWN =
        "com.wireguard.android.action.SET_TUNNEL_DOWN";

    private static final String UP =
        "com.wireguard.android.action.SET_TUNNEL_UP";

    private void control(boolean activate) {

        if (checkSelfPermission(PERMISSION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{PERMISSION}, 10);
            return;
        }

        Intent intent = new Intent(activate ? UP : DOWN);

        intent.setComponent(new ComponentName(
            "com.wireguard.android",
            "com.wireguard.android.model.TunnelManager$IntentReceiver"
        ));

        intent.putExtra("tunnel", "Ramkotablet");

        sendBroadcast(intent);

        Toast.makeText(this,
            activate ? "Wysłano ON" : "Wysłano OFF",
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);

        TextView title = new TextView(this);
        title.setText("WG Controller\nTunel: Ramkotablet");
        title.setTextSize(22);
        layout.addView(title);

        Button permission = new Button(this);
        permission.setText("Nadaj uprawnienie WireGuard");
        permission.setOnClickListener(v -> {
            if (checkSelfPermission(PERMISSION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{PERMISSION}, 10);
            } else {
                Toast.makeText(this, "Uprawnienie już nadane",
                    Toast.LENGTH_SHORT).show();
            }
        });
        layout.addView(permission);

        Button off = new Button(this);
        off.setText("WYŁĄCZ VPN");
        off.setOnClickListener(v -> control(false));
        layout.addView(off);

        Button on = new Button(this);
        on.setText("WŁĄCZ VPN");
        on.setOnClickListener(v -> control(true));
        layout.addView(on);

        setContentView(layout);
    }
}
