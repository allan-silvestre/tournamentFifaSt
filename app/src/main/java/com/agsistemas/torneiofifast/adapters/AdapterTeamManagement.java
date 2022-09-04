package com.agsistemas.torneiofifast.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.agsistemas.torneiofifast.screens.registration.AddPlayerTeam;
import com.agsistemas.torneiofifast.models.Player;
import com.agsistemas.torneiofifast.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterTeamManagement extends BaseAdapter  {

    private Context context;
    private List<Player> listJ;

    public AdapterTeamManagement(Context context, List<Player> listClubesC) {
        this.context = context;
        this.listJ = listClubesC;
    }

    List<Integer> imagemJogadores = new ArrayList<>();

    @Override
    public int getCount() {
        return listJ.size();
    }

    @Override
    public Object getItem(int i) {
        return listJ.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.custom_listview_ger_clubes, null);

        TextView id = v.findViewById(R.id.id);
        ImageView imgJ = v.findViewById(R.id.imagJ);
        TextView nJ = v.findViewById(R.id.nomeJ);
        TextView pJ = v.findViewById(R.id.posicaoJ);
        TextView oJ = v.findViewById(R.id.overallJ);

        imagensJogador();
        imgJ.setImageResource( imagemJogadores.get( Integer.parseInt( listJ.get(i).getImagem() ) ) );

        id.setText("ID:" + listJ.get(i).getIdJogador());
        nJ.setText(listJ.get(i).getnJogador());
        pJ.setText(listJ.get(i).getpJogador());
        oJ.setText(listJ.get(i).getoJogador());

        v.setTag(listJ.get(i).getIdJogador());

        return v;
    }

    private void imagensJogador(){
        AddPlayerTeam a = new AddPlayerTeam();
        for( int i = 0; i < a.jogadores.length; i++ ){

            imagemJogadores.add( context.getResources().getIdentifier("rjogador" + i , "drawable", context.getPackageName() ) );

        }


    }


}
