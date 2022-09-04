package com.agsistemas.torneiofifast.screens.general;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.agsistemas.torneiofifast.R;
import com.agsistemas.torneiofifast.adapters.AdapterListPlayers;
import com.agsistemas.torneiofifast.adapters.AdapterHistoricTransfers;
import com.agsistemas.torneiofifast.models.Competitor;
import com.agsistemas.torneiofifast.screens.edit.EditTeamManagement;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class TeamManagement extends Activity {

    ArrayList<LastTrasfers> listT = new ArrayList<>();
    AdapterHistoricTransfers arrayAdapterListT;

    ArrayList<Competitor> listP = new ArrayList<>();
    AdapterListPlayers arrayAdapterListP;
    Competitor p;

    ImageButton tabelaId, historicoId, configId;

    ListView utmTransf, listPlayers;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String uLogadoId;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_clubes);

        //sendBroadcast(new Intent(getBaseContext(), BootCompletedIntentReceiver.class));

        progressDialog = new ProgressDialog(TeamManagement.this, R.style.MyAlertDialogStyle );
        //Configura o título da progress dialog
        progressDialog.setTitle("Aguarde");
        //configura a mensagem de que está sendo feito o carregamento
        progressDialog.setMessage("Carregando!!!");
        //configura se a progressDialog pode ser cancelada pelo usuário
        progressDialog.setCancelable(false);
        progressDialog.show();

        inicializarFirebase();
        listarClubes();
        listarUtmTransf();

        utmTransf = findViewById(R.id.listUltimasT);
        listPlayers = findViewById(R.id.listPlayers);

        tabelaId = findViewById(R.id.tabelaId);
        historicoId = findViewById(R.id.historicoId);
        configId = findViewById(R.id.configId);

        tabelaId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamManagement.this, Classification.class);
                intent.putExtra("idLogado", uLogadoId );
                startActivity(intent);
            }
        });

        historicoId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamManagement.this, CurrentSeason.class);
                intent.putExtra("idLogado", uLogadoId );
                startActivity(intent);
            }
        });

        configId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamManagement.this, Settings.class);
                intent.putExtra("idLogado", uLogadoId );
                startActivity(intent);
            }
        });

        listPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Competitor jogador = (Competitor) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(TeamManagement.this, EditTeamManagement.class);
                intent.putExtra("id", jogador.getId());
                intent.putExtra("imagem", jogador.getImagem());
                intent.putExtra("time", jogador.getTime());
                intent.putExtra("nome", jogador.getNome());
                intent.putExtra("imgPerfil", jogador.getImgPerfil());

                intent.putExtra("idLogado", uLogadoId );

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        Bundle b = new Bundle();
        b = getIntent().getExtras();

        uLogadoId = b.getString("idLogado");


    }

    private void listarUtmTransf(){

        databaseReference.child("UltimasTransf").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listT.clear();
                int a = 0;
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    LastTrasfers p = objSnapshot.getValue(LastTrasfers.class);

                    listT.add(p);

                }

                Collections.reverse(listT);

                for(int i = 0; i < dataSnapshot.getChildrenCount(); i++){

                    if(i > 14){
                        databaseReference.child("UltimasTransf").child(listT.get(i).getId()).removeValue();
                    }

                }

                arrayAdapterListT = new AdapterHistoricTransfers(getApplicationContext(), listT);
                utmTransf.setAdapter(arrayAdapterListT);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void listarClubes(){

        databaseReference.child("Competidores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listP.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    p = objSnapshot.getValue(Competitor.class);

                    if(p.getNome().equals("INATIVO")){

                    } else {
                        listP.add(p);
                    }

                }

                Collections.sort(listP);
                arrayAdapterListP = new AdapterListPlayers (getApplicationContext(), listP);
                listPlayers.setAdapter(arrayAdapterListP);


                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

}