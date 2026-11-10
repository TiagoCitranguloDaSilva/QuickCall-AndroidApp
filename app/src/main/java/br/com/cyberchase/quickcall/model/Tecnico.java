package br.com.cyberchase.quickcall.model;

import java.util.ArrayList;
import java.util.List;


public class Tecnico extends Usuario {

    private String especialidade;
    private String nivelTecnico;
    private boolean disponivel;
    private final List<Long> categoriaIds = new ArrayList<>();
    private final List<String> categoriaNomes = new ArrayList<>();

    public Tecnico() {
        super();
        setTipoPerfil(TipoPerfil.TECNICO);
        this.disponivel = true;
    }

    public void aceitarChamado(Chamado chamado) {
        chamado.atribuirTecnico(getId());
    }

    public void atualizarStatusChamado(Chamado chamado, Long novoStatusId) {
        chamado.alterarStatus(novoStatusId);
    }

    public void registrarSolucao(Chamado chamado, String solucao) {
        chamado.setSolucaoFinal(solucao);
    }

    public Comentario adicionarComentarioTecnico(Chamado chamado, String texto) {
        Comentario comentario = new Comentario();
        comentario.setChamadoId(chamado.getId());
        comentario.setAutorId(getId());
        comentario.setTexto(texto);
        return comentario;
    }

    public void encerrarChamado(Chamado chamado, String solucao) {
        chamado.fechar(solucao);
    }

    public boolean atende(Long categoriaId) {
        return categoriaId != null && categoriaIds.contains(categoriaId);
    }


    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getNivelTecnico() {
        return nivelTecnico;
    }

    public void setNivelTecnico(String nivelTecnico) {
        this.nivelTecnico = nivelTecnico;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public List<Long> getCategoriaIds() {
        return categoriaIds;
    }

    public List<String> getCategoriaNomes() {
        return categoriaNomes;
    }
}
