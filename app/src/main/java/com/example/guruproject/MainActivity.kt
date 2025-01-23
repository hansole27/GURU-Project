package com.example.guruproject

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var btnAdd: Button
    lateinit var btnList: Button

    lateinit var imageBook1: ImageView
    lateinit var imageBook2: ImageView
    lateinit var imageBook3: ImageView
    lateinit var imageBook4: ImageView
    lateinit var imageBook5: ImageView
    lateinit var imageBook6: ImageView
    lateinit var imageBook7: ImageView
    lateinit var imageBook8: ImageView
    lateinit var imageBook9: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // DBManager 초기화
        dbManager = DBManager(this, "book", null, 1)
        sqlitedb = dbManager.readableDatabase

        // View 연결
        btnAdd = findViewById(R.id.addButton)
        btnList = findViewById(R.id.homeButton)
        imageBook1 = findViewById(R.id.image1)
        imageBook2 = findViewById(R.id.image2)
        imageBook3 = findViewById(R.id.image3)
        imageBook4 = findViewById(R.id.image4)
        imageBook5 = findViewById(R.id.image5)
        imageBook6 = findViewById(R.id.image6)
        imageBook7 = findViewById(R.id.image7)
        imageBook8 = findViewById(R.id.image8)
        imageBook9 = findViewById(R.id.image9)

        // 책표지 이미지 - 데이터 로드
        loadAllBookData()

        //'추가' 버튼 클릭 시
        btnAdd.setOnClickListener {
            val intent = Intent(this, BookAdd::class.java)
            startActivity(intent)
        }

        //'목록' 버튼 클릭 시
        btnList.setOnClickListener {
            val intent = Intent(this, BookList::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 뒤로 가기로 돌아올 때 데이터를 갱신
        loadAllBookData()
    }

    private fun loadAllBookData() {
        // 책 데이터를 모두 불러오고 각 ImageView에 설정
        val cursor = sqlitedb.rawQuery("SELECT * FROM book", null)
        val rowCount = cursor.count

        for (i in 1..rowCount.coerceAtMost(9)) {
            if (cursor.moveToPosition(i - 1)) { // i는 1부터 시작하므로 1을 뺌
                val imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
                    ?.takeIf { it != "null" } ?: ""

                val imageView = when (i) {
                    1 -> imageBook1
                    2 -> imageBook2
                    3 -> imageBook3
                    4 -> imageBook4
                    5 -> imageBook5
                    6 -> imageBook6
                    7 -> imageBook7
                    8 -> imageBook8
                    9 -> imageBook9
                    else -> null
                }

                imageView?.let { iv ->
                    if (imageUri.isNotBlank()) {
                        try {
                            val uri = Uri.parse(imageUri)
                            iv.setImageURI(uri)
                            Log.d("DEBUG", "<성공> $i 번째 이미지 로드 성공")
                        } catch (e: Exception) {
                            Log.e("DEBUG", "*** $i 번째 이미지 로드 실패: 예외 발생")
                            iv.setImageResource(R.drawable.baseline_book_24) // 기본 이미지 설정
                        }
                    } else {
                        iv.setImageResource(R.drawable.baseline_book_24) // 기본 이미지 설정
                    }
                }
            }
        }
        cursor.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }
}
