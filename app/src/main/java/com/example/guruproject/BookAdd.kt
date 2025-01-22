package com.example.guruproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class BookAdd : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var btnSave: Button
    lateinit var btnCancel: Button

    lateinit var edtTitle: EditText
    lateinit var edtAuthor: EditText
    lateinit var edtPublisher: EditText
    lateinit var edtStart: EditText
    lateinit var edtFinish: EditText

    lateinit var imageBook: ImageView

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null


    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 레이아웃 연결
        setContentView(R.layout.book_add)


        //book_add.xml의 요소들을 변수로 연결
        btnSave = findViewById(R.id.SaveButton)
        btnCancel = findViewById(R.id.CancelButton)
        edtTitle = findViewById(R.id.edtTitle)
        edtAuthor = findViewById(R.id.edtAuthor)
        edtPublisher = findViewById(R.id.edtPublisher)
        edtStart = findViewById(R.id.edtStart)
        edtFinish = findViewById(R.id.edtFinish)
        imageBook = findViewById(R.id.imageBook)


        //DB 초기화
        dbManager = DBManager(this, "book", null, 1)

        imageBook.setOnClickListener {
            Toast.makeText(this, "책 표지 선택하기", Toast.LENGTH_SHORT).show()
            openGallery()
        }

                // 이미지 선택 Activity Result 등록
                imagePickerLauncher = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == RESULT_OK && result.data != null) {
                        selectedImageUri = result.data?.data
                        if (selectedImageUri != null) {
                            imageBook.setImageURI(selectedImageUri) // 선택한 이미지 표시
                        } else {
                            Log.e("BookAdd", "이미지를 선택하지 않았습니다.")
                        }
                    }
                }


        //정보 입력 후 저장 클릭 시
        btnSave.setOnClickListener {
            var str_title: String = edtTitle.text.toString()
            var str_author: String = edtAuthor.text.toString()
            var str_publisher: String = edtPublisher.text.toString()
            var str_start: String = edtStart.text.toString()
            var str_finish: String = edtFinish.text.toString()


            // DB에 데이터 저장
            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL(
                """
                INSERT INTO book (title, author, publisher, start_date, end_date, memo, image_url) 
                VALUES ('$str_title', '$str_author', '$str_publisher', 
                '$str_start', '$str_finish', '', '${selectedImageUri}')
                """
            )
            Log.d("BookAdd", "<성공> DB에 저장 완료")
            sqlitedb.close()


            //책 저장 후에 메인화면으로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        //취소 클릭 시 메인화면으로 이동
        btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // MainActivity 실행

        }
    }

    // 갤러리 열기
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

}