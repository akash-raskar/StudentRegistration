package com.example.studentregistration;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class Registration extends Activity implements OnClickListener {
    EditText Rollno, Name, Marks;

    RadioButton R1,R2,R3;
    RadioGroup radioGroup;
    Button Insert, Delete, Update, View, ViewAll;
    SQLiteDatabase db;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        Rollno = findViewById(R.id.edt1);
        Name = findViewById(R.id.edt2);
        Marks = findViewById(R.id.edt3);

        R1 =findViewById(R.id.radioButton);
        R2 = findViewById(R.id.radioButton2);
        R3 =findViewById(R.id.radioButton3);

        Insert = findViewById(R.id.BtnAdd);
        Delete = findViewById(R.id.BtnDelete);
        Update = findViewById(R.id.BtnModify);
        View = findViewById(R.id.BtnView);
        ViewAll = findViewById(R.id.BtnViewAll);


        Insert.setOnClickListener(this);
        Delete.setOnClickListener(this);
        Update.setOnClickListener(this);
        View.setOnClickListener(this);
        ViewAll.setOnClickListener(this);


       radioGroup= findViewById(R.id.radioGroup);
        final String value =
                ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId()))
                        .getText().toString();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();
            }
        });

        // Creating database and table
        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR,gender VARCHAR);");
    }

    public void onClick(View view) {

        switch (view.getId()) {

            // Inserting a record to the Student table
            case R.id.BtnAdd:

                if (view == Insert)
                {
                    // Checking for empty fields
                    if (Rollno.getText().toString().trim().length() == 0 ||
                            Name.getText().toString().trim().length() == 0 ||
                            Marks.getText().toString().trim().length() == 0 || radioGroup.getTag().toString().trim().length()==0)
                    {
                        showMessage("Error", "Please enter all values");
                        return;
                    }
                }
                if (view == Insert)
                {
                    // Checking for empty fields
                    if (Rollno.getText().toString().trim().length() == 0 ||
                            Name.getText().toString().trim().length() == 0 ||
                            Marks.getText().toString().trim().length() == 0)
                    {
                        showMessage("Error", "Please enter all values");
                        return;
                    }
                    db.execSQL("INSERT INTO student VALUES('" + Rollno.getText() + "','" + Name.getText() +
                            "','" + Marks.getText() + "');");
                    showMessage("Success", "Record added");
                    clearText();
                }

                break;


            // Deleting a record from the Student table
            case R.id.BtnDelete:

                if (view == Delete)
                {
                    // Checking for empty roll number
                    if (Rollno.getText().toString().trim().length() == 0)
                    {
                        showMessage("Error", "Please enter Rollno");
                        return;
                    }
                    Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + Rollno.getText() + "'", null);
                    if (c.moveToFirst())
                    {
                        db.execSQL("DELETE FROM student WHERE rollno='" + Rollno.getText() + "'");
                        showMessage("Success", "Record Deleted");
                    } else {
                        showMessage("Error", "Invalid Rollno");
                    }
                    clearText();
                }

                break;


            // Updating a record in the Student table

            case R.id.BtnModify:
                if (view == Update)
                {
                    // Checking for empty roll number
                    if (Rollno.getText().toString().trim().length() == 0)
                    {
                        showMessage("Error", "Please enter Rollno");
                        return;
                    }
                    Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + Rollno.getText() + "'", null);
                    if (c.moveToFirst())
                    {
                        db.execSQL("UPDATE student SET name='" + Name.getText() + "',marks='" + Marks.getText() +
                                "' WHERE rollno='" + Rollno.getText() + "'");
                        showMessage("Success", "Record Modified");
                    } else {
                        showMessage("Error", "Invalid Rollno");
                    }
                    clearText();
                }

                break;


            // Display a record from the Student table

            case R.id.BtnView:

                if (view == View)
                {
                    // Checking for empty roll number
                    if (Rollno.getText().toString().trim().length() == 0)
                    {
                        showMessage("Error", "Please enter Rollno");
                        return;
                    }
                    Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + Rollno.getText() + "'", null);
                    if (c.moveToFirst()) {
                        Name.setText(c.getString(1));
                        Marks.setText(c.getString(2));
                    } else {
                        showMessage("Error", "Invalid Rollno");
                        clearText();
                    }
                }
                break;


            // Displaying all the records
            case R.id.BtnViewAll:

                if (view == ViewAll)
                {
                    Cursor c = db.rawQuery("SELECT * FROM student", null);
                    if (c.getCount() == 0)
                    {
                        showMessage("Error", "No records found");
                        return;
                    }
                    StringBuffer buffer1 = new StringBuffer();
                    while (c.moveToNext())
                    {
                        buffer1.append("Rollno: " + c.getString(0) + "\n");
                        buffer1.append("Name: " + c.getString(1) + "\n");
                        buffer1.append("Marks: " + c.getString(2) + "\n\n");
                    }
                    showMessage("Student Details", buffer1.toString());
                }
                break;


        }


    }

    private void clearText()
    {
        Rollno.setText("");
        Name.setText("");
        Marks.setText("");
        Rollno.requestFocus();
    }

    private void showMessage(String title, String message)
    {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}