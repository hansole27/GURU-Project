package com.example.guruproject

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BookInfo : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var edtTitle: TextView
    lateinit var edtAuthor: TextView
    lateinit var edtPublisher: TextView
    lateinit var edtStart: TextView
    lateinit var edtFinish: TextView
    lateinit var imageBook: ImageView
    lateinit var btnMemo: Button
    lateinit var btnEdit: Button
    lateinit var btnDelete: Button
    private var bookTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_info)

        // Intent로 전달된 데이터 받기
        bookTitle = intent.getStringExtra("intent_title") ?: return

        // DB 초기화
        dbManager = DBManager(this, "book", null, 1)
        sqlitedb = dbManager.readableDatabase

        // 뷰 연결
        edtTitle = findViewById(R.id.edtTitle)
        edtAuthor = findViewById(R.id.edtAuthor)
        edtPublisher = findViewById(R.id.edtPublisher)
        edtStart = findViewById(R.id.edtStart)
        edtFinish = findViewById(R.id.edtFinish)
        imageBook = findViewById(R.id.imageBook)
        btnMemo = findViewById(R.id.MemoButton)
        btnEdit = findViewById(R.id.ChangeButton)
        btnDelete = findViewById(R.id.DeleteButton)

        // 데이터 로드
        loadBookData()

        // 메모 버튼 클릭
        btnMemo.setOnClickListener {
            val intent = Intent(this, MemoWriting::class.java)
            intent.putExtra("intent_title", bookTitle)
            startActivity(intent)
        }

        // 수정 버튼 클릭
        btnEdit.setOnClickListener {
            val intent = Intent(this, BookEdit::class.java)
            intent.putExtra("intent_title", bookTitle)
            startActivityForResult(intent, REQUEST_EDIT_BOOK) // 수정 후 결과를 반환받음
        }

        // 삭제 버튼 클릭
        btnDelete.setOnClickListener {
            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("DELETE FROM book WHERE title = ?", arrayOf(bookTitle))
            sqlitedb.close()

            // 삭제 후 리스트 화면으로 이동
            val intent = Intent(this, BookList::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun loadBookData() {
        val cursor = sqlitedb.rawQuery("SELECT * FROM book WHERE title = ?", arrayOf(bookTitle))
        if (cursor.moveToFirst()) {
            edtTitle.text = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            edtAuthor.text = cursor.getString(cursor.getColumnIndexOrThrow("author"))
            edtPublisher.text = cursor.getString(cursor.getColumnIndexOrThrow("publisher"))
            edtStart.text = cursor.getString(cursor.getColumnIndexOrThrow("start_date"))
            edtFinish.text = cursor.getString(cursor.getColumnIndexOrThrow("end_date"))

            // 이미지 불러오기
            val imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_url"))?.takeIf { it != "null" } ?: ""
            if (imageUri.isNotBlank()) {
                try {
                    val uri = Uri.parse(imageUri)
                    imageBook.setImageURI(uri)
                } catch (e: Exception) {
                    imageBook.setImageResource(R.drawable.baseline_book_24) // 기본 이미지 설정
                }
            } else {
                imageBook.setImageResource(R.drawable.baseline_book_24) // 기본 이미지 설정
            }
        }
        cursor.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_BOOK && resultCode == RESULT_OK) {
            bookTitle = data?.getStringExtra("updated_title") ?: return
            loadBookData() // 수정된 데이터 다시 로드
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
