package com.example.guruproject

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.DropBoxManager
import android.provider.Telephony
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Date

class BookAdd : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var btnSave: Button
    lateinit var btnCancle: Button

    lateinit var edtTitle: EditText
    lateinit var edtAuthor: EditText
    lateinit var edtPublisher: EditText
    lateinit var edtStart: Date
    lateinit var edtFinish: Date


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_add)


        //book_add.xml의 요소들을 변수로 연결
        btnSave = findViewById(R.id.SaveButton)
        btnCancle = findViewById(R.id.CancleButton)
        edtTitle = findViewById(R.id.edtTitle)
        edtAuthor = findViewById(R.id.edtAuthor)
        edtPublisher = findViewById(R.id.edtPublisher)
        edtStart = findViewById(R.id.edtStart)
        edtFinish = findViewById(R.id.edtFinish)

        dbManager = DBManager(this, "personnel", null, 1)


        //정보 입력 후 저장 클릭 시
        btnSave.setOnClickListener {
            var str_name: String = edtName.text.toString()
            var str_age: String = edtAge.text.toString()
            var str_tel: String = edtTel.text.toString()
            var str_gender: String = ""

            if (rg_gender.checkedRadioButtonId == R.id.male) {
                str_gender = rb_gender_m.text.toString()
            }
            if (rg_gender.checkedRadioButtonId == R.id.female) {
                str_gender = rb_gender_f.text.toString()
            }


            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL(
                "INSERT INTO personnel VALUES ('" + str_name + "', '"
                        + str_gender + "', '" + str_age + "', '" + str_tel + "')"
            )

            sqlitedb.close()

            val intent = Intent(this, PersonnelInfo::class.java)
            intent.putExtra("intent_name", str_name)
            startActivity(intent)

        }

        //취소 클릭 시
        btnCancle.setOnClickListener {

        }

    }
}