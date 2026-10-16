package br.com.cyberchase.quickcall.data.service;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.cyberchase.quickcall.model.Usuario;
import br.com.cyberchase.quickcall.network.HttpJsonClient;

public class LoginApiService {

    private final HttpJsonClient client = new HttpJsonClient();

    // lembrete para eu nao fazer coco!
    //1- a logica é enviar email e senha, auth/login ai recebe token
    //2- vc guarda o token, ai todo pediod usa ele
    //3- busca nome, id e perfil do usuario/email
    // NAO SEJA BURRO DE ESQUECER QUE TEM QUE RETORNAR NULL SE O EMAIL E SENHA ESTIVER ERRADO
    // E DEIXE O SERVIDOR LIGADO!
    public Usuario login(String email, String senha) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("senha", senha);

            HttpJsonClient.setToken(null);
            JSONObject json = client.post("/auth/login", body);
            if (json == null || !json.has("token")) {
                return null;
            }

            HttpJsonClient.setToken(json.getString("token"));

            Usuario usuario = new UsuarioApiService().buscarPorEmail(email);
            if (usuario == null || usuario.getId() == null || usuario.getId() == 0) {
                HttpJsonClient.setToken(null);
                return null;
            }
            return usuario;
        } catch (JSONException e) {
            return null;
        }
    }
}
