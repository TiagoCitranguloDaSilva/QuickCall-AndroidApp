package br.com.cyberchase.quickcall.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;

import br.com.cyberchase.quickcall.R;

public final class PrioridadeCores {

    private PrioridadeCores() {
    }

    public static int cor(Context context, String nomePrioridade) {
        String n = nomePrioridade == null ? "" : nomePrioridade.toLowerCase();
        int idCor;
        if (n.contains("urgente")) {
            idCor = R.color.qc_vermelho;
        } else if (n.contains("alta")) {
            idCor = R.color.qc_laranja;
        } else if (n.contains("media") || n.contains("média")) {
            idCor = R.color.qc_amarelo;
        } else if (n.contains("baixa")) {
            idCor = R.color.qc_azul;
        } else {
            idCor = R.color.qc_texto_secundario;
        }
        return ContextCompat.getColor(context, idCor);
    }

    public static GradientDrawable bolinha(Context context, String nomePrioridade) {
        int tamanho = (int) (10 * context.getResources().getDisplayMetrics().density);
        GradientDrawable bolinha = new GradientDrawable();
        bolinha.setShape(GradientDrawable.OVAL);
        bolinha.setColor(cor(context, nomePrioridade));
        bolinha.setSize(tamanho, tamanho);
        bolinha.setBounds(0, 0, tamanho, tamanho);
        return bolinha;
    }
}
