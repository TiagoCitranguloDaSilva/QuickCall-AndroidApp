package br.com.cyberchase.quickcall.data.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.cyberchase.quickcall.model.HistoricoChamado;
import br.com.cyberchase.quickcall.network.HttpJsonClient;


public class HistoricoApiService {

    private final HttpJsonClient client = new HttpJsonClient();

    public List<HistoricoChamado> listarPorChamado(long chamadoId) {
        List<HistoricoChamado> historicos = new ArrayList<>();
        String rota = "/relatorio/chamado/" + chamadoId;


        JSONArray array = client.getArray(rota);
        if (array.length() == 0) {
            JSONObject unico = client.getObject(rota);
            if (unico != null && unico.has("id")) {
                array.put(unico);
            }
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.optJSONObject(i);
            if (json == null) {
                continue;
            }
            HistoricoChamado historico = new HistoricoChamado();
            historico.setId(json.optLong("id"));
            historico.setChamadoId(chamadoId);
            historico.setAcao(json.isNull("tipoMudanca") ? "Alteracao" : json.optString("tipoMudanca"));
            historico.setValorAnterior(json.isNull("valorAntigo") ? null : json.optString("valorAntigo"));
            historico.setValorNovo(json.isNull("valorNovo") ? null : json.optString("valorNovo"));
            if (!json.isNull("dataHora")) {
                try {
                    historico.setDataHora(LocalDateTime.parse(json.optString("dataHora")));
                } catch (Exception ignored) {
                    // formato de data inesperado: deixa sem data
                }
            }
            historicos.add(historico);
        }
        return historicos;
    }

    //tiago ja deixou salvo o historico no servidor
    public HistoricoChamado salvar(HistoricoChamado historico) {
        return historico;
    }
}
