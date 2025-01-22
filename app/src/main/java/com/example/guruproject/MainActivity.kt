package com.example.guruproject

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var btnAdd: Button
    lateinit var btnList: Button

    lateinit var bookImg: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // View 연결
        btnAdd = findViewById(R.id.addButton)
        btnList = findViewById(R.id.listButton)
        bookImg = findViewById(R.id.image1)

        // DBManager 초기화
        dbManager = DBManager(this, "book", null, 1)
        sqlitedb = dbManager.readableDatabase
        val cursor: Cursor = sqlitedb.rawQuery("SELECT image_url FROM book", null)



        //url 읽어오는 권한 받아오는 작업 필요


        // 첫 컬럼의 이미지만 bookImg에 띄우기
        if (cursor.moveToFirst()) {
            val imageUri = cursor.getString(0) // 첫 번째 컬럼의 image_url 값 가져오기
            Log.d("Main", "<성공> 첫 번째 컬럼의 image_url 값 가져오기 ")

            if (imageUri != null) {
                // content:// URI로 이미지 로드
                val uri = Uri.parse(imageUri)

                // ImageView에 URI 적용
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    bookImg.setImageBitmap(bitmap)
                    inputStream?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    // 오류 발생 시 기본 이미지로 대체
                    bookImg.setImageResource(R.drawable.baseline_book_24)
                }
            }
        } else {
            // DB에 데이터가 없을 경우 기본 이미지 설정
            bookImg.setImageResource(R.drawable.baseline_book_24)
        }

        cursor.close()



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