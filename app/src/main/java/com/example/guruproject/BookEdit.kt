package com.example.guruproject

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_edit)

        val bookTitle = intent.getStringExtra("intent_title") ?: return

        dbManager = DBManager(this, "book", null, 1)
        sqlitedb = dbManager.readableDatabase

        edtTitle = findViewById(R.id.edtTitle)
        edtAuthor = findViewById(R.id.edtAuthor)
        edtPublisher = findViewById(R.id.edtPublisher)
        edtStart = findViewById(R.id.edtStart)
        edtFinish = findViewById(R.id.edtFinish)
        btnSave = findViewById(R.id.SaveButton)
        btnCancel = findViewById(R.id.CancelButton)
        imageBook = findViewById(R.id.iv_edit_imageBook)

        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    contentResolver.takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    imageBook.setImageURI(uri)
                }
            }
        }

        imageBook.setOnClickListener {
            openGallery()
        }

        imageBook.setImageResource(R.drawable.baseline_book_24)
        loadBookData(bookTitle)

        btnSave.setOnClickListener {
            saveBookData(bookTitle)
        }

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

            val imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image_url"))?.takeIf { it != "null" } ?: ""

            Log.d("load image uri", imageUri)
            if (imageUri.isNotBlank()) {
                try {
                    selectedImageUri = Uri.parse(imageUri)
                    imageBook.setImageURI(selectedImageUri)
                    Log.d("load image uri", selectedImageUri.toString())
                } catch (e: Exception) {
                    Log.e("Image Load Error", "Error loading image", e)
                    imageBook.setImageResource(R.drawable.baseline_book_24)
                }
            } else {
                imageBook.setImageResource(R.drawable.baseline_book_24)
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
            arrayOf(
                newTitle,
                newAuthor,
                newPublisher,
                newStartDate,
                newEndDate,
                imageUriString,
                oldTitle
            )
        )
        sqlitedb.close()

        val intent = Intent()
        intent.putExtra("updated_title", newTitle)
        setResult(RESULT_OK, intent)
        Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        imagePickerLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }
}
