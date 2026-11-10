package br.com.cyberchase.quickcall.data.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.cyberchase.quickcall.model.Tecnico;
import br.com.cyberchase.quickcall.network.HttpJsonClient;


public class TecnicoApiService {

    private final HttpJsonClient client = new HttpJsonClient();

    public List<Tecnico> listarTodos() {
        List<Tecnico> tecnicos = new ArrayList<>();
        JSONArray array = client.getArray("/tecnico");
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.optJSONObject(i);
            if (json == null) {
                continue;
            }
            Tecnico tecnico = new Tecnico();
            tecnico.setId(json.has("id") ? json.optLong("id") : (long) (i + 1));
            tecnico.setNome(json.optString("nome", "Tecnico " + (i + 1)));
            tecnico.setTelefone(json.isNull("telefone") ? null : json.optString("telefone"));
            JSONArray categorias = json.optJSONArray("categorias");
            if (categorias != null) {
                for (int j = 0; j < categorias.length(); j++) {
                    JSONObject categoria = categorias.optJSONObject(j);
                    if (categoria != null) {
                        tecnico.getCategoriaIds().add(categoria.optLong("id"));
                        tecnico.getCategoriaNomes().add(categoria.optString("nome", ""));
                    }
                }
            }
            tecnicos.add(tecnico);
        }
        return tecnicos;
    }
}
