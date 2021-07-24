package com.example.studentverse.activity.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentverse.R
import com.example.studentverse.activity.adapter.AnswerAdapter
import com.example.studentverse.activity.model.Answer
import com.example.studentverse.activity.model.Post
import com.example.studentverse.activity.repository.QuestionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SinglePostActivity : AppCompatActivity() {
    private lateinit var stitle: TextView
    private lateinit var stime: TextView
    private lateinit var sviews: TextView
    private lateinit var suser: TextView
    private lateinit var sbody: TextView
    private lateinit var etanswer: EditText
    private lateinit var btnanswer: Button
    private lateinit var rvanswer: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_post)

        stitle=findViewById(R.id.stitle)
        stime=findViewById(R.id.stime)
        sviews=findViewById(R.id.sviews)
        suser=findViewById(R.id.suser)
        sbody=findViewById(R.id.sbody)
        etanswer=findViewById(R.id.etanswer)
        btnanswer=findViewById(R.id.btnanswer)
        rvanswer=findViewById(R.id.rvanswer)

        val intent = intent.getParcelableExtra<Post>("post")!!

        if(intent !=null){
            stitle.setText("${intent.title}")
//            stime.setText("${intent.time}")
//            sviews.setText("${intent.sviews}")
            sbody.setText("${intent.body}")

        }

        btnanswer.setOnClickListener {
            val ans = etanswer.text.toString()
            val answer = Answer(post = intent._id,text = ans)

            CoroutineScope(Dispatchers.IO).launch{
                try{
                    val questionRepository = QuestionRepository()
                    val response = questionRepository.answeradd(answer)
                    if (response.success == true){
                        withContext(Dispatchers.Main){
                            startActivity(
                                Intent(
                                    this@SinglePostActivity,
                                    DashboardActivity::class.java
                                )
                            )
                        }
                    }
                }
                catch (ex: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@SinglePostActivity,
                            ex.toString(),
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val questionRepository = QuestionRepository()
                val response = questionRepository.getanswer(intent._id!!)
                if (response.success == true) {
                    val comment = response.data!!
                    withContext(Dispatchers.Main) {
                        val answerAdapter = AnswerAdapter(comment, this@SinglePostActivity)
                        rvanswer.adapter = answerAdapter
                        rvanswer.layoutManager= LinearLayoutManager(this@SinglePostActivity, LinearLayoutManager.VERTICAL,false)
                    }
                }

            }
            catch (ex:Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SinglePostActivity,
                        ex.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}