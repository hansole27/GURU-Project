package com.example.guruproject

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MemoWriting : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var edtTitle: TextView
    lateinit var edtAuthor: TextView
    lateinit var edtPublisher: TextView
    lateinit var edtMemo: EditText
    lateinit var btnSave: Button
    lateinit var btnCancel: Button

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_writing)

        // Intent로 전달된 데이터 받기
        val bookTitle = intent.getStringExtra("intent_title")

        // DB 초기화
        dbManager = DBManager(this, "book", null, 1)
        sqlitedb = dbManager.readableDatabase

        // 뷰 연결
        edtTitle = findViewById(R.id.edtTitle)
        edtAuthor = findViewById(R.id.edtAuthor)
        edtPublisher = findViewById(R.id.edtPublisher)
        edtMemo = findViewById(R.id.Memo)
        btnSave = findViewById(R.id.SaveButton)
        btnCancel = findViewById(R.id.CancelButton)

        // 기존 데이터 로드
        val cursor = sqlitedb.rawQuery("SELECT * FROM book WHERE title = '$bookTitle'", null)
        if (cursor.moveToFirst()) {
            edtTitle.text = cursor.getString(cursor.getColumnIndex("title"))
            edtAuthor.text = cursor.getString(cursor.getColumnIndex("author"))
            edtPublisher.text = cursor.getString(cursor.getColumnIndex("publisher"))
            edtMemo.setText(cursor.getString(cursor.getColumnIndex("memo")))
        }
        cursor.close()

        // 저장 버튼
        btnSave.setOnClickListener {
            val newMemo = edtMemo.text.toString()
            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("UPDATE book SET memo = '$newMemo' WHERE title = '$bookTitle'")
            sqlitedb.close()

            val intent = Intent(this, BookInfo::class.java)
            intent.putExtra("intent_title", bookTitle)
            startActivity(intent)
            finish()
        }

        // 취소 버튼
        btnCancel.setOnClickListener {
            finish()
        }
    }
}
