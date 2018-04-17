package mecalogik.com.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private TextView mensajeTex;
    private EditText mensajeEdit;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mensajeRef = ref.child("mensaje");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mensajeTex = (TextView) findViewById(R.id.mensaje);
        mensajeEdit = (EditText) findViewById(R.id.mensajeEdit);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mensajeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value =dataSnapshot.getValue(String.class);

                mensajeTex.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void modificar(View view){
        String mensaje = mensajeEdit.getText().toString();
        mensajeRef.setValue(mensaje);

        mensajeEdit.setText("");
    }
}
