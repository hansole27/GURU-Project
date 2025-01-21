package com.example.guruproject

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


        dbManager = DBManager(this, "book", null, 1)
        sqlitedb = dbManager.readableDatabase
        btnAdd = findViewById(R.id.addButton)
        layout = findViewById(R.id.books)

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM book", null)

        var num: Int = 0
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // 데이터를 읽어오는 코드
                // 데이터베이스에서 데이터 추출
                val str_title = cursor.getString(cursor.getColumnIndex("title"))


                Log.d("BookList", "<성공> $num 번째 책 - 데이터 읽어오기 성공")

                val layout_item = LinearLayout(this)
                layout_item.orientation = LinearLayout.VERTICAL
                layout_item.id = num

                // 제목
                val tvTitle = TextView(this)
                tvTitle.text = str_title
                tvTitle.textSize = 30f
                tvTitle.setTextColor(Color.BLACK)
                tvTitle.setBackgroundColor(Color.parseColor("#72FFEB3B"))
                layout_item.addView(tvTitle)

                // 선 나누기
                val tvLine = TextView(this)
                tvLine.setBackgroundColor(Color.WHITE)
                layout_item.addView(tvLine)

                layout_item.setOnClickListener {
                    val intent = Intent(this, BookInfo::class.java)
                    intent.putExtra("intent_title", str_title)
                    startActivity(intent)
                }

                // 레이아웃에 최종 추가
                layout.addView(layout_item)
                num++

            } while (cursor.moveToNext())  // 수정된 부분: moveToNext()로 모든 행을 처리

            cursor.close()  // 커서 닫기
        } else {
            Log.d("BookList", "<실패> 데이터가 없어서 읽어올 수 없습니다.")
        }

        sqlitedb.close()
        dbManager.close()

        //'추가' 버튼 클릭 시
        btnAdd.setOnClickListener{
            val intent = Intent(this, BookAdd::class.java)
            startActivity(intent)
        }
    }

}