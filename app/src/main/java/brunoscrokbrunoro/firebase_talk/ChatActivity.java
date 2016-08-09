package brunoscrokbrunoro.firebase_talk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import brunoscrokbrunoro.firebase_talk.model.Mensagem;

/**
 * @author Bruno Scrok Brunoro
 * @create 8/8/16 20:15
 * @project Firebase-Talk
 */
public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getCanonicalName();

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;

    private MensagensAdapter adapter;
    private List<Mensagem> mensagens;

    private ListView lstChat;
    private EditText edtMensagem;
    private Button butEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        lstChat = (ListView) findViewById(R.id.lstChat);
        edtMensagem = (EditText) findViewById(R.id.edtMensagem);
        butEnviar = (Button) findViewById(R.id.butEnviar);
        mensagens = new ArrayList<Mensagem>();
        adapter = new MensagensAdapter(getLayoutInflater(),mensagens);
        lstChat.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("mensagens");

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getDisplayName() == null){
            NomeDialog nomeDialog = NomeDialog.getInstance();
            nomeDialog.setCancelable(false);
            nomeDialog.show(getSupportFragmentManager(), "nomeDialog");
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                mensagens.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshots) {
                    Mensagem mensagem = dataSnapshot1.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        butEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                Mensagem mensagem = new Mensagem();
                mensagem.setMensagem(edtMensagem.getText().toString());
                if(user.getDisplayName() != null) {
                    mensagem.setUsuario(user.getDisplayName());
                }else{
                    mensagem.setUsuario(user.getEmail());
                }
                mensagem.setUid(myRef.push().getKey());
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(mensagem.getUid(), mensagem.toMap());
                myRef.updateChildren(childUpdates);
                edtMensagem.setText("");
            }
        });

    }

    public class MensagensAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<Mensagem> mensagens;

        public MensagensAdapter(LayoutInflater inflater, List<Mensagem> mensagens){
            this.inflater = inflater;
            this.mensagens = mensagens;

        }

        @Override
        public int getCount() {
            return mensagens.size();
        }

        @Override
        public Mensagem getItem(int position) {
            return mensagens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = new ViewHolder();

            if(convertView == null){
                convertView = inflater.inflate(android.R.layout.simple_list_item_2,null);
                holder.mensagem = (TextView) convertView.findViewById(android.R.id.text1);
                holder.usuario = (TextView) convertView.findViewById(android.R.id.text2);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            Mensagem mensagem = mensagens.get(position);
            holder.mensagem.setText(mensagem.getMensagem());
            holder.usuario.setText(mensagem.getUsuario());

            return convertView;
        }

        class ViewHolder{
            public TextView mensagem;
            public TextView usuario;
        }
    }


}
