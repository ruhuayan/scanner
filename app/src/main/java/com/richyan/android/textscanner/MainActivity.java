package com.richyan.android.textscanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {

    private EditText codeEdit;
    private EditText markEdit;
    private EditText productEdit;
    private EditText tagEdit;
    private Spinner unitSpinner;
    private EditText measureEdit;
    private EditText priceEdit;
    private EditText purchaseNumEdit;
    private TextView purchaseDateTv;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                codeEdit.setText(result.getContents());
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                Intent intent = new Intent(MainActivity.this, LauncherActivity.class);
                //intent.putExtra("numCust", numCust);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_save:

                return true;
            case R.id.menu_cancel:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Stock In");

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null){
            //Not signed in, launch the Sign In Activity
            startActivity(new Intent(this, EmailPasswordActivity.class));
            finish();
            return;
        }
        codeEdit = (EditText)findViewById(R.id.codeEdit) ;
        markEdit = (EditText)findViewById(R.id.markEdit);
        productEdit = (EditText)findViewById(R.id.productEdit);
        tagEdit = (EditText)findViewById(R.id.tagEdit);
        unitSpinner = (Spinner)findViewById(R.id.unitSpinner);
        measureEdit = (EditText)findViewById(R.id.measureEdit);
        purchaseNumEdit = (EditText)findViewById(R.id.pNumberEdit);
        priceEdit = (EditText)findViewById(R.id.priceEdit);
        purchaseDateTv = (TextView) findViewById(R.id.pdateTV);

        final Calendar calendar = Calendar.getInstance();
        updateDisplay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*purchaseDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"clicked", Toast.LENGTH_LONG).show();
                DatePickerDialog dialog = new DatePickerDialog(getBaseContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        //int _year = year, _month = monthOfYear, _day = dayOfMonth;
                        //updateDisplay(_year, _month, _day);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });*/

        //Bundle bundle = getIntent().getExtras();

        findViewById(R.id.scanBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                //integrator.setCameraId(0);  // Use a specific camera of the device
                //integrator.setBeepEnabled(false);
                // integrator.setOrientationLocked(false);
                //integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        findViewById(R.id.readerBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ReaderFragment readerFragment = new ReaderFragment();
                readerFragment.show(getSupportFragmentManager(), "Reader");
            }
        });

        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int valid = 1;
                if(codeEdit.getText().toString().length()==0){codeEdit.setError("Enter Barcode"); valid =0; }
                if(markEdit.getText().toString().length()==0) {markEdit.setError("Mark is required"); valid =0; }
                if(productEdit.getText().toString().length()==0) {productEdit.setError("Description is Required"); valid=0;}
                if(tagEdit.getText().toString().length()==0) {tagEdit.setError("Tag is required"); valid=0;}
                if(measureEdit.getText().toString().length()==0) {measureEdit.setError("Enter Measurement number");valid=0;}
                if(priceEdit.getText().toString().length()==0) {priceEdit.setError("Enter Price"); valid=0;}

                if(valid==1){
                    FirebaseApp.initializeApp(getApplicationContext());
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    writeMark(markEdit.getText().toString());
                    writeProduct(productEdit.getText().toString());
                    writeArticle(codeEdit.getText().toString(), markEdit.getText().toString(), productEdit.getText().toString(),tagEdit.getText().toString(),
                            unitSpinner.getSelectedItem().toString(), Float.parseFloat(measureEdit.getText().toString()),
                            Float.parseFloat(priceEdit.getText().toString()));
                }
            }
        });
    }
    private void writeItem(String barcode, String mark, String product, String tag, String unit, float measure){
        Article item = new Article(mark, product, tag, unit, measure);
        mDatabase.child("items").child(barcode).setValue(item);
    }

    private void writeArticle(String barcode,String mark, String product, String tag, String unit, float measure, float price){
        String superid = mFirebaseUser.getUid();
        Article article = new Article(mark, product, tag, unit, measure, price);
        mDatabase.child("supermarket/"+superid).child("articles").child(barcode).setValue(article);
    }
    private void writeMark(String name){
        Mark mark = new Mark(name);
        mDatabase.child("marks").child(name).setValue(mark);
    }
    private void writeProduct(String name){
        /*mDatabase.child("products").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    return;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        Productname product = new Productname(name);
        mDatabase.child("products").child(name).setValue(product);
    }

    private void updateDisplay(int year, int month, int day) {

        purchaseDateTv.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(month + 1).append("/").append(day).append("/").append(year).append(" "));
    }
}
