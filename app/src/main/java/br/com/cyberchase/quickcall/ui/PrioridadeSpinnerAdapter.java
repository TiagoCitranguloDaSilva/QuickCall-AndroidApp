package br.com.cyberchase.quickcall.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PrioridadeSpinnerAdapter extends ArrayAdapter<String> {

    public PrioridadeSpinnerAdapter(Context context, List<String> nomes) {
        super(context, android.R.layout.simple_spinner_dropdown_item, nomes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return colocarBolinha(super.getView(position, convertView, parent), position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return colocarBolinha(super.getDropDownView(position, convertView, parent), position);
    }

    private View colocarBolinha(View view, int position) {
        TextView texto = (TextView) view;
        texto.setCompoundDrawables(PrioridadeCores.bolinha(getContext(), getItem(position)), null, null, null);
        texto.setCompoundDrawablePadding((int) (10 * getContext().getResources().getDisplayMetrics().density));
        return texto;
    }
}
