package com.example.guruproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookList : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var layout: LinearLayout
    lateinit var btnAdd: Button

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_list)

        // DB 초기화
        dbManager = DBManager(this, "book", null, 1)
        sqlitedb = dbManager.readableDatabase
        btnAdd = findViewById(R.id.addButton)
        layout = findViewById(R.id.books)

        // 데이터 로드
        loadData()

        // '추가' 버튼 클릭 시
        btnAdd.setOnClickListener {
            val intent = Intent(this, BookAdd::class.java)
            startActivity(intent)
        }
    }

    override fun onRestart() {
        loadData()
        super.onRestart()
    }

    @SuppressLint("Range")
    private fun loadData() {
        layout.removeAllViews() // 기존 뷰 제거
        sqlitedb = dbManager.readableDatabase

        // 데이터 읽기
        val cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM book", null)
        var num = 0
        if (cursor.moveToFirst()) {
            do {
                val strTitle = cursor.getString(cursor.getColumnIndex("title"))

                val layoutItem = LinearLayout(this)
                layoutItem.orientation = LinearLayout.VERTICAL
                layoutItem.id = num

                val tvTitle = TextView(this)
                tvTitle.text = strTitle
                tvTitle.textSize = 30f
                tvTitle.setTextColor(Color.WHITE)
                tvTitle.setBackgroundColor(Color.parseColor("#72FFEB3B"))
                layoutItem.addView(tvTitle)



                //각 항목을 누르면 BookInfo 로 넘어간다
                layoutItem.setOnClickListener {
                    val intent = Intent(this, BookInfo::class.java)
                    intent.putExtra("intent_title", strTitle)
                    startActivityForResult(intent, 1)
                }

                layout.addView(layoutItem)
                num++
            } while (cursor.moveToNext())
        }
        cursor.close()
        sqlitedb.close()
    }

    // 추가된 부분: 수정된 제목 반영
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadData() // 수정 후 리스트를 다시 로드
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.close()
    }
}