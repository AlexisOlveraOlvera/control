package mecalogik.com.control;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RsgisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private CircleImageView mImageBtn;
    private EditText mEmailLabel;
    private EditText mNameLabel;
    private EditText mPassword;
    private Button mRegBtn;
    private Button mLoginPageBtn;

    private Uri imageUri;

    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private ProgressBar mRegisterPrigressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsgister);

        imageUri = null;

        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mImageBtn = (CircleImageView) findViewById(R.id.register_imagen);
        mEmailLabel = (EditText) findViewById(R.id.emailregi);
        mNameLabel = (EditText) findViewById(R.id.nameregi);
        mPassword = (EditText) findViewById(R.id.passwordreg);
        mRegBtn = (Button) findViewById(R.id.btnreg);
        mLoginPageBtn = (Button) findViewById(R.id.btnregistroreg);
        mRegisterPrigressBar = (ProgressBar) findViewById(R.id.registerprogressbar);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imageUri != null) {

                    mRegisterPrigressBar.setVisibility(View.VISIBLE);

                    final String name = mNameLabel.getText().toString();
                    String email = mEmailLabel.getText().toString();
                    String password = mPassword.getText().toString();

                    if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    final String user_id = mAuth.getCurrentUser().getUid();

                                    StorageReference user_profile = mStorage.child(user_id + ".jpg");

                                    user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {

                                            if(uploadTask.isSuccessful()){

                                                String download_url = uploadTask.getResult().getDownloadUrl().toString();

                                                Map<String, Object> userMap = new HashMap<>();
                                                userMap.put("name",name);
                                                userMap.put("image",download_url);
                                                mFirestore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        mRegisterPrigressBar.setVisibility(View.INVISIBLE);

                                                        sendToMain();
                                                    }
                                                });

                                            }else {

                                                Toast.makeText(RsgisterActivity.this, "Error : " + uploadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                mRegisterPrigressBar.setVisibility(View.INVISIBLE);
                                            }

                                        }
                                    });

                                }else {

                                    Toast.makeText(RsgisterActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    mRegisterPrigressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    }

                }
            }
        });

        mLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pinture"),PICK_IMAGE);
            }
        });

    }

    private void sendToMain() {
        Intent mainIntent = new Intent(RsgisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE){

            imageUri = data.getData();
            mImageBtn.setImageURI(imageUri);

        }
    }

}
