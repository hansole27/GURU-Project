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

        // 책표지 이미지 - 데이터 로드
        val cursor = sqlitedb.rawQuery("SELECT * FROM book", null)
        val rowCount = cursor.count // DB의 실제 row 개수
        cursor.close() // cursor는 닫아줍니다.

        for (i in 1..rowCount.coerceAtMost(9)) { // 최대 9개까지만 반복
            loadBookData(i)
        }


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

/*
    private fun loadBookData(rowNum: Int) {
        // book 테이블에서 모든 데이터를 가져옴
        val cursor = sqlitedb.rawQuery("SELECT * FROM book", null)
        if (cursor.moveToPosition(rowNum - 1)) { // rowNum은 1부터 시작하므로 1을 빼줌
            //image_url 컬럼의 데이터를 가져오기
            val imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
                ?.takeIf { it != "null" } ?: ""
            if (imageUri.isNotBlank()) {
                try {
                    val uri = Uri.parse(imageUri)
                    imageBook.setImageURI(uri)
                    Log.d("DEBUG", "<성공> 이미지로드 제대로 실행.")
                } catch (e: Exception) {
                    Log.d("DEBUG", "*** Exception에러 발생.")
                    imageBook.setImageResource(R.drawable.baseline_book_24) // 기본 이미지 설정
                }
            } else {
                Log.d("DEBUG", "*** 이미지Uri를 불러오지 못하였음.")
                imageBook.setImageResource(R.drawable.baseline_book_24) // 기본 이미지 설정
            }
        } else {
            // rowNum이 범위를 벗어날 경우 기본 이미지 설정
            Log.d("DEBUG", "*** rowNum이 범위를 벗어났습니다.")
            imageBook.setImageResource(R.drawable.baseline_book_24)

        }
        cursor.close()

    }

 */

    private fun loadBookData(rowNum: Int) {
        // book 테이블에서 모든 데이터를 가져옴
        val cursor = sqlitedb.rawQuery("SELECT * FROM book", null)

        // 1부터 rowNum까지 반복
        for (i in 1..rowNum) {
            if (cursor.moveToPosition(i - 1)) { // i는 1부터 시작하므로 1을 뺌
                // image_url 컬럼의 데이터를 가져오기
                val imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
                    ?.takeIf { it != "null" } ?: ""

                // 동적으로 ImageView 선택
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

                // 이미지 설정
                if (imageView != null) {
                    if (imageUri.isNotBlank()) {
                        try {
                            val uri = Uri.parse(imageUri)
                            imageView.setImageURI(uri)
                            Log.d("DEBUG", "<성공> $i 번째 이미지 로드 성공")
                        } catch (e: Exception) {
                            Log.d("DEBUG", "*** $i 번째 이미지 로드 실패: 예외 발생.")
                            imageView.setImageResource(R.drawable.baseline_book_24) // 기본 이미지 설정
                        }
                    } else {
                        Log.d("DEBUG", "*** $i 번째 이미지 URI가 비어 있음.")
                        imageView.setImageResource(R.drawable.baseline_book_24) // 기본 이미지 설정
                    }
                } else {
                    Log.d("DEBUG", "*** $i 번째 ImageView가 null입니다.")
                }
            } else {
                Log.d("DEBUG", "*** $i 번째 rowNum이 범위를 벗어났습니다.")
            }
        }
        cursor.close()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_BOOK && resultCode == RESULT_OK) {
            val updatedNum = data?.getIntExtra("updated_num", -1) ?: return
            if (updatedNum != -1) {
                loadBookData(updatedNum) // 수정된 데이터 다시 로드
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }

    companion object {
        private const val REQUEST_EDIT_BOOK = 1
    }

}