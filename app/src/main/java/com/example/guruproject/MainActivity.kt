package com.example.guruproject

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    //lateinit var dbManager: DBManager
    //lateinit var sqlitedb: SQLiteDatabase
    lateinit var btnAdd: Button
    lateinit var btnList: Button


    //lateinit var edtTitle: EditText
    //lateinit var edtAuthor: EditText
    //lateinit var edtPublisher: EditText
    //lateinit var edtStart: EditText
    //lateinit var edtFinish: EditText

    //lateinit var imageBook: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd = findViewById(R.id.addButton)
        btnList = findViewById(R.id.listButton)
        



        //'추가' 버튼 클릭 시
        btnAdd.setOnClickListener{
            val intent = Intent(this, BookAdd::class.java)
            startActivity(intent)
        }

        //'목록' 버튼 클릭 시
        btnList.setOnClickListener{
            val intent = Intent(this, BookList::class.java)
            startActivity(intent)
        }

    }

}