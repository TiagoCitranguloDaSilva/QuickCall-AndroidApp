package br.com.cyberchase.quickcall;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import br.com.cyberchase.quickcall.data.repository.DemoDataSeeder;
import br.com.cyberchase.quickcall.data.service.LoginApiService;
import br.com.cyberchase.quickcall.model.Usuario;
import br.com.cyberchase.quickcall.network.ApiConfig;
import br.com.cyberchase.quickcall.network.HttpJsonClient;
import br.com.cyberchase.quickcall.network.NetworkPolicy;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText senhaInput;
    private TextView servidorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkPolicy.enable();
        ApiConfig.carregar(this);
        new DemoDataSeeder(this).seedIfNeeded();


        new SessionManager(this).logout();
        HttpJsonClient.setToken(null);

        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.input_email);
        senhaInput = findViewById(R.id.input_senha);
        Button entrarButton = findViewById(R.id.button_entrar);
        TextView credenciaisView = findViewById(R.id.text_credenciais_demo);

        credenciaisView.setText(
                "Teste: admin@quickcall.com / admin"
        );

        entrarButton.setOnClickListener(v -> realizarLogin());

        servidorView = findViewById(R.id.text_servidor);
        atualizarTextoServidor();
        servidorView.setOnClickListener(v -> abrirConfiguracaoServidor());
    }

    private void atualizarTextoServidor() {
        servidorView.setText("⚙  Servidor: " + ApiConfig.getIp() + ":" + ApiConfig.getPorta());
    }

    private void abrirConfiguracaoServidor() {
        EditText ipInput = new EditText(this);
        ipInput.setHint("IP (ex: 10.0.2.2)");
        ipInput.setText(ApiConfig.getIp());
        ipInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);

        EditText portaInput = new EditText(this);
        portaInput.setHint("Porta (ex: 8080)");
        portaInput.setText(ApiConfig.getPorta());
        portaInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        LinearLayout campos = new LinearLayout(this);
        campos.setOrientation(LinearLayout.VERTICAL);
        int margem = (int) (20 * getResources().getDisplayMetrics().density);
        campos.setPadding(margem, margem / 2, margem, 0);
        campos.addView(ipInput);
        campos.addView(portaInput);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Servidor")
                .setMessage("Emulador: 10.0.2.2 | Celular: IP do computador na mesma rede")
                .setView(campos)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String ip = ipInput.getText().toString().trim();
                    String porta = portaInput.getText().toString().trim();
                    if (ip.isEmpty() || porta.isEmpty()) {
                        Toast.makeText(this, "Preencha IP e porta.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ApiConfig.salvar(this, ip, porta);
                    atualizarTextoServidor();
                    Toast.makeText(this, "Servidor salvo.", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("Padrao", (dialog, which) -> {
                    ApiConfig.salvar(this, ApiConfig.IP_PADRAO, ApiConfig.PORTA_PADRAO);
                    atualizarTextoServidor();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void realizarLogin() {
        String email = emailInput.getText().toString().trim();
        String senha = senhaInput.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha email e senha.", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new LoginApiService().login(email, senha);

        if (usuario != null && usuario.getId() != null) {
            new SessionManager(this).salvarToken(HttpJsonClient.getToken());
            new SessionManager(this).salvarSessao(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getTipoPerfil() == null ? null : usuario.getTipoPerfil().name()
            );
            abrirTelaPrincipal();
            return;
        }

        Toast.makeText(this, "Email/senha invalidos ou servidor desligado.", Toast.LENGTH_LONG).show();
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
