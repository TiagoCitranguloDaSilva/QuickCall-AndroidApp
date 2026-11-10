package br.com.cyberchase.quickcall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import br.com.cyberchase.quickcall.data.repository.CatalogoRepository;
import br.com.cyberchase.quickcall.data.repository.ChamadoRepository;
import br.com.cyberchase.quickcall.data.repository.DemoDataSeeder;
import br.com.cyberchase.quickcall.model.Categoria;
import br.com.cyberchase.quickcall.model.Chamado;
import br.com.cyberchase.quickcall.model.Prioridade;
import br.com.cyberchase.quickcall.model.StatusChamado;
import br.com.cyberchase.quickcall.network.ApiConfig;
import br.com.cyberchase.quickcall.network.NetworkPolicy;
import br.com.cyberchase.quickcall.ui.ChamadoAdapter;

public class MainActivity extends AppCompatActivity {

    private final List<Chamado> chamados = new ArrayList<>();
    private ChamadoAdapter chamadosAdapter;
    private TextView resumoView;
    private ListView chamadosListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkPolicy.enable();
        ApiConfig.carregar(this);
        new DemoDataSeeder(this).seedIfNeeded();
        SessionManager sessionManager = new SessionManager(this);
        if (!sessionManager.estaLogado()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resumoView = findViewById(R.id.text_resumo);
        chamadosListView = findViewById(R.id.list_chamados);
        Button novoChamadoButton = findViewById(R.id.button_novo_chamado);
        Button sairButton = findViewById(R.id.button_sair);


        novoChamadoButton.setOnClickListener(v ->
                startActivity(new Intent(this, NovoChamadoActivity.class)));

        sairButton.setOnClickListener(v -> {
            new SessionManager(this).logout();
            br.com.cyberchase.quickcall.network.HttpJsonClient.setToken(null);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        chamadosListView.setOnItemClickListener((parent, view, position, id) -> {
            Chamado chamado = chamados.get(position);
            Intent intent = new Intent(this, ChamadoDetalheActivity.class);
            intent.putExtra(ChamadoDetalheActivity.EXTRA_CHAMADO_ID, chamado.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarDashboard();
    }

    private void carregarDashboard() {
        chamados.clear();
        chamados.addAll(new ChamadoRepository(this).listarTodos());

        CatalogoRepository catalogoRepository = new CatalogoRepository();
        List<StatusChamado> status = catalogoRepository.listarStatusPadrao();
        List<Prioridade> prioridades = catalogoRepository.listarPrioridadesPadrao();
        List<Categoria> categorias = catalogoRepository.listarCategoriasPadrao();

        SessionManager sessionManager = new SessionManager(this);
        String nomeUsuario = sessionManager.obterNomeUsuarioLogado();
        String perfilUsuario = sessionManager.obterPerfilUsuarioLogado();

        chamadosAdapter = new ChamadoAdapter(this, chamados, status, prioridades, categorias);
        chamadosListView.setAdapter(chamadosAdapter);

        resumoView.setText(
                "Usuario logado: " + (nomeUsuario == null ? "-" : nomeUsuario)
                        + "\nPerfil: " + (perfilUsuario == null ? "nao informado pelo servidor" : perfilUsuario)
                        + "\nChamados carregados: " + chamados.size()
        );
    }
}
