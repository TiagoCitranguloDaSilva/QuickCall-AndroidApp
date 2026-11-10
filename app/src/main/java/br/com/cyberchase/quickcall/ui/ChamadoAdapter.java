package br.com.cyberchase.quickcall.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

import br.com.cyberchase.quickcall.R;
import br.com.cyberchase.quickcall.model.Categoria;
import br.com.cyberchase.quickcall.model.Chamado;
import br.com.cyberchase.quickcall.model.Prioridade;
import br.com.cyberchase.quickcall.model.StatusChamado;

public class ChamadoAdapter extends ArrayAdapter<Chamado> {

    private final List<StatusChamado> status;
    private final List<Prioridade> prioridades;
    private final List<Categoria> categorias;

    public ChamadoAdapter(Context context, List<Chamado> chamados, List<StatusChamado> status,
                          List<Prioridade> prioridades, List<Categoria> categorias) {
        super(context, 0, chamados);
        this.status = status;
        this.prioridades = prioridades;
        this.categorias = categorias;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_chamado, parent, false);
        }

        Chamado chamado = getItem(position);

        TextView numero = view.findViewById(R.id.text_item_numero);
        TextView data = view.findViewById(R.id.text_item_data);
        TextView titulo = view.findViewById(R.id.text_item_titulo);
        TextView categoria = view.findViewById(R.id.text_item_categoria);
        TextView statusView = view.findViewById(R.id.text_item_status);
        TextView prioridadeView = view.findViewById(R.id.text_item_prioridade);

        numero.setText("#" + chamado.getId());
        data.setText(UiFormatter.formatDateTime(chamado.getDataAbertura()));
        titulo.setText(chamado.getTitulo() == null ? "Chamado" : chamado.getTitulo());
        categoria.setText(UiFormatter.findCategoriaNome(categorias, chamado.getCategoriaId()));

        String nomeStatus = UiFormatter.findStatusNome(status, chamado.getStatusId());
        String nomePrioridade = UiFormatter.findPrioridadeNome(prioridades, chamado.getPrioridadeId());
        statusView.setText(nomeStatus);
        prioridadeView.setText(nomePrioridade);

        pintarStatus(statusView, nomeStatus);
        pintarPrioridade(prioridadeView, nomePrioridade);

        return view;
    }

    private void pintarStatus(TextView etiqueta, String nome) {
        String n = nome.toLowerCase();
        int azul = cor(R.color.qc_azul);
        if (n.contains("aberto")) {
            pintar(etiqueta, azul, azul, cor(R.color.qc_texto));
        } else if (n.contains("resolvido") || n.contains("fechado")) {
            pintarCinza(etiqueta);
        } else {
            pintar(etiqueta, 0x00000000, azul, azul);
        }
    }

    private void pintarPrioridade(TextView etiqueta, String nome) {
        int borda = cor(R.color.qc_borda);
        pintar(etiqueta, 0x00000000, borda, cor(R.color.qc_texto));
        etiqueta.setCompoundDrawables(PrioridadeCores.bolinha(getContext(), nome), null, null, null);
        etiqueta.setCompoundDrawablePadding((int) (6 * getContext().getResources().getDisplayMetrics().density));
    }

    private void pintarCinza(TextView etiqueta) {
        int borda = cor(R.color.qc_borda);
        pintar(etiqueta, borda, borda, cor(R.color.qc_texto_secundario));
    }

    private void pintar(TextView etiqueta, int corFundo, int corBorda, int corTexto) {
        GradientDrawable fundo = new GradientDrawable();
        fundo.setCornerRadius(100f);
        fundo.setColor(corFundo);
        fundo.setStroke(2, corBorda);
        etiqueta.setBackground(fundo);
        etiqueta.setTextColor(corTexto);
    }

    private int cor(int idCor) {
        return ContextCompat.getColor(getContext(), idCor);
    }
}
