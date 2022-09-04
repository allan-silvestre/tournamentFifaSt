package com.agsistemas.torneiofifast.screens.general;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.agsistemas.torneiofifast.screens.registration.AddGunner;
import com.agsistemas.torneiofifast.R;
import com.agsistemas.torneiofifast.adapters.AdapterGunnerLargerImages;
import com.agsistemas.torneiofifast.models.Gunner;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Gunners extends Activity {

    ArrayList<Gunner> listArtilheiro = new ArrayList<>();
    AdapterGunnerLargerImages arrayAdapterArtilheiro;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ListView artilharia;

    EditText pesquisarArt;

    Gunner selecionarArtilheiro;

    ProgressDialog progressDialog;

    ImageButton tabelaId, transf, historicoId, configId, sairId, addArtilharia, fecharPesquisarArt;

    String uLogadoId;

    Gunner art;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_art);

        progressDialog = new ProgressDialog(Gunners.this, R.style.MyAlertDialogStyle );
        //Configura o título da progress dialog
        progressDialog.setTitle("Aguarde");
        //configura a mensagem de que está sendo feito o carregamento
        progressDialog.setMessage("Carregando!!!");
        //configura se a progressDialog pode ser cancelada pelo usuário
        progressDialog.setCancelable(false);
        progressDialog.show();

        artilharia = findViewById(R.id.artilharia);
        addArtilharia = findViewById(R.id.addArtilharia2);
        fecharPesquisarArt = findViewById(R.id.fecharPesquisaArt2);
        pesquisarArt = findViewById(R.id.pesquisarArt2);

        tabelaId = findViewById(R.id.tabelaId);
        transf = findViewById(R.id.transfId);
        historicoId = findViewById(R.id.historicoId);
        configId = findViewById(R.id.configId);

        tabelaId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Gunners.this, Classification.class);
                intent.putExtra("idLogado", uLogadoId );
                startActivity(intent);
                finish();
            }
        });

        transf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Gunners.this, TeamManagement.class);
                intent.putExtra("idLogado", uLogadoId );
                startActivity(intent);
                finish();
            }
        });

        historicoId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Gunners.this, CurrentSeason.class);
                intent.putExtra("idLogado", uLogadoId );
                startActivity(intent);
                finish();
            }
        });

        configId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Gunners.this, Settings.class);
                intent.putExtra("idLogado", uLogadoId );
                startActivity(intent);
                finish();
            }
        });


        pesquisarArt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Gunners.this.arrayAdapterArtilheiro.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        fecharPesquisarArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               pesquisarArt.setText("");

            }
        });

        addArtilharia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Gunners.this, AddGunner.class);
                intent.putExtra("idLogado", uLogadoId );
                startActivity(intent);
            }
        });

        artilharia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selecionarArtilheiro = (Gunner) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(Gunners.this, com.agsistemas.torneiofifast.screens.edit.EditGunner.class);
                intent.putExtra("id", selecionarArtilheiro.getId());
                intent.putExtra("jogador", selecionarArtilheiro.getJogador());
                intent.putExtra("time", selecionarArtilheiro.getTime());
                intent.putExtra("gols", selecionarArtilheiro.getGols());
                intent.putExtra("imagemClube", selecionarArtilheiro.getImagemClube());
                intent.putExtra("imagemJogador", selecionarArtilheiro.getImagemJogador());

                intent.putExtra("idLogado", uLogadoId );

                startActivity(intent);

            }
        });

        Bundle b = new Bundle();
        b = getIntent().getExtras();

        uLogadoId = b.getString("idLogado");



        inicializarFirebase();
        listarDadosArtilharia();


    }

    private void listarDadosArtilharia() {

        databaseReference.child("Artilharia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listArtilheiro.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    art = objSnapshot.getValue(Gunner.class);
                    listArtilheiro.add(art);

                }

                Collections.sort(listArtilheiro);
                arrayAdapterArtilheiro = new AdapterGunnerLargerImages(getApplicationContext(), listArtilheiro);
                artilharia.setAdapter(arrayAdapterArtilheiro);
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

        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
}