package pl.example.wgcontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class MacroReceiver extends BroadcastReceiver {

    private static final String PERMISSION =
        "com.wireguard.android.permission.CONTROL_TUNNELS";

    @Override
    public void onReceive(Context context, Intent command) {

        if (context.checkSelfPermission(PERMISSION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context,
                "Brak uprawnienia WireGuard",
                Toast.LENGTH_SHORT).show();
            return;
        }

        String action;

        if ("pl.example.wgcontroller.DOWN".equals(command.getAction())) {
            action = "com.wireguard.android.action.SET_TUNNEL_DOWN";
        } else if ("pl.example.wgcontroller.UP".equals(command.getAction())) {
            action = "com.wireguard.android.action.SET_TUNNEL_UP";
        } else {
            return;
        }

        Intent intent = new Intent(action);

        intent.setComponent(new ComponentName(
            "com.wireguard.android",
            "com.wireguard.android.model.TunnelManager$IntentReceiver"
        ));

        intent.putExtra("tunnel", "Ramkotablet");

        context.sendBroadcast(intent);
    }
}
