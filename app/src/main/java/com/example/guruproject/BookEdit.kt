package com.example.guruproject

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BookEdit : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var edtTitle: EditText
    lateinit var edtAuthor: EditText
    lateinit var edtPublisher: EditText
    lateinit var edtStart: EditText
    lateinit var edtFinish: EditText
    lateinit var btnSave: Button
    lateinit var btnCancel: Button
    lateinit var imageBook: ImageView
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_edit)

        // Intent로 전달된 데이터 받기
        val bookTitle = intent.getStringExtra("intent_title") ?: return

        // DB 초기화
        dbManager = DBManager(this, "book", null, 1)
        sqlitedb = dbManager.readableDatabase

        // 뷰 연결
        edtTitle = findViewById(R.id.edtTitle)
        edtAuthor = findViewById(R.id.edtAuthor)
        edtPublisher = findViewById(R.id.edtPublisher)
        edtStart = findViewById(R.id.edtStart)
        edtFinish = findViewById(R.id.edtFinish)
        btnSave = findViewById(R.id.SaveButton)
        btnCancel = findViewById(R.id.CancelButton)
        imageBook = findViewById(R.id.imageBook)

        // 기존 데이터 로드
        loadBookData(bookTitle)

        // 저장 버튼 클릭
        btnSave.setOnClickListener {
            saveBookData(bookTitle)
        }

        // 취소 버튼 클릭
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadBookData(bookTitle: String) {
        val cursor = sqlitedb.rawQuery("SELECT * FROM book WHERE title = ?", arrayOf(bookTitle))
        if (cursor.moveToFirst()) {
            edtTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")))
            edtAuthor.setText(cursor.getString(cursor.getColumnIndexOrThrow("author")))
            edtPublisher.setText(cursor.getString(cursor.getColumnIndexOrThrow("publisher")))
            edtStart.setText(cursor.getString(cursor.getColumnIndexOrThrow("start_date")))
            edtFinish.setText(cursor.getString(cursor.getColumnIndexOrThrow("end_date")))
            val imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_url"))
            if (!imageUri.isNullOrEmpty()) {
                selectedImageUri = Uri.parse(imageUri)
                imageBook.setImageURI(selectedImageUri)
            }
        }
        cursor.close()
    }

    private fun saveBookData(oldTitle: String) {
        val newTitle = edtTitle.text.toString()
        val newAuthor = edtAuthor.text.toString()
        val newPublisher = edtPublisher.text.toString()
        val newStartDate = edtStart.text.toString()
        val newEndDate = edtFinish.text.toString()
        val imageUriString = selectedImageUri?.toString() ?: ""

        sqlitedb = dbManager.writableDatabase
        sqlitedb.execSQL(
            """
            UPDATE book 
            SET title = ?, author = ?, publisher = ?, 
                start_date = ?, end_date = ?, image_url = ? 
            WHERE title = ?
            """,
            arrayOf(newTitle, newAuthor, newPublisher, newStartDate, newEndDate, imageUriString, oldTitle)
        )
        sqlitedb.close()

        val intent = Intent()
        intent.putExtra("updated_title", newTitle)
        setResult(RESULT_OK, intent)
        Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }
}
