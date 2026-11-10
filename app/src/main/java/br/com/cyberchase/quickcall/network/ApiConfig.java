package br.com.cyberchase.quickcall.network;

import android.content.Context;
import android.content.SharedPreferences;

public final class ApiConfig {

    public static final String IP_PADRAO = "10.0.2.2";
    public static final String PORTA_PADRAO = "8080";

    private static final String PREFS_NAME = "quickcall_config";
    private static final String KEY_IP = "servidor_ip";
    private static final String KEY_PORTA = "servidor_porta";

    private static String ip = IP_PADRAO;
    private static String porta = PORTA_PADRAO;

    private ApiConfig() {
    }

    public static String getBaseUrl() {
        return "http://" + ip + ":" + porta;
    }

    public static String getIp() {
        return ip;
    }

    public static String getPorta() {
        return porta;
    }

    public static void carregar(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        ip = prefs.getString(KEY_IP, IP_PADRAO);
        porta = prefs.getString(KEY_PORTA, PORTA_PADRAO);
    }

    public static void salvar(Context context, String novoIp, String novaPorta) {
        ip = novoIp;
        porta = novaPorta;
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_IP, novoIp)
                .putString(KEY_PORTA, novaPorta)
                .apply();
    }
}
