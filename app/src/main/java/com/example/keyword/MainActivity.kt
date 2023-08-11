package com.example.keyword

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keyword.adapter.NewsAdapter
import com.example.keyword.adapter.itemAdapter
import com.example.keyword.data.Items
import com.example.keyword.data.News
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.select.Elements


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var sid = 102
    private var itemList = ArrayList<Items>()
    private var newsList = ArrayList<News>()
    private val newsRecyclerView: RecyclerView by lazy {
        findViewById(R.id.news_rv)
    }
    private val keywordRecyclerView: RecyclerView by lazy {
        findViewById(R.id.keyword_rv)
    }
    private lateinit var keywordAdapter: itemAdapter
    private lateinit var newsadApter: NewsAdapter

    private val ivMenu: ImageView by lazy {
        findViewById(R.id.iv_menu)
    }
    private val drawerLayout: DrawerLayout by lazy {
        findViewById(R.id.drawer)
    }
    private val toolbar: androidx.appcompat.widget.Toolbar by lazy {
        findViewById(R.id.toolbar)
    }
    private val now: TextView by lazy {
        findViewById(R.id.now)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getKeyword()
        NewsCrawling(sid)
        initList()

        val navigationView: NavigationView = findViewById(R.id.navigation)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun initList() {
        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type
        setSupportActionBar(toolbar)

        ivMenu.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
    }


    private fun getKeyword() {
        itemList.add(Items("keyword1"))
        itemList.add(Items("keyword2"))
        itemList.add(Items("keyword3"))
        itemList.add(Items("keyword4"))
        itemList.add(Items("keyword5"))
        itemList.add(Items("keyword6"))
        itemList.add(Items("keyword7"))
        itemList.add(Items("keyword8"))
        itemList.add(Items("keyword9"))
        itemList.add(Items("keyword10"))

        initKeywordRecyclerView(itemList)
    }

    private fun NewsCrawling(sid: Int) {
        Thread(Runnable {
            newsList.clear()
            val doc =
                Jsoup.connect("https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=$sid")
                    .userAgent("Chrome").get()
            var elements: Elements = doc.select(".sh_item._cluster_content")
            // mobile-padding 클래스의 board-list의 id를 가진 것들을 elements 객체에 저장
            /*
            크롤링 하는 법 : class 는 .(class) 로 찾고 id 는 #(id) 로 검색
             */
            for (elements in elements) {  //elements의 개수만큼 반복
                val title = elements.select(".sh_text a").first()!!.text()
                Log.d("crawling", title)
                val cover = elements.select(".sh_thumb_inner a img").attr("src")
                Log.d("crawling", cover)
                val sum = elements.select(".sh_text_lede").text()
                Log.d("crawling", sum)
                val press = elements.select(".sh_text_info div").text()
                Log.d("crawling", press)
                val address = elements.select(".sh_text a").attr("href")
                Log.d("crawling", address)
                newsList.add(
                    News(
                        title, cover, sum, press, address
                    )
                )     //위에서 크롤링 한 내용들을 itemlist에 추가
            }
            runOnUiThread {
                initNewsRecyclerView(newsList)
            }
        }).start()
    }

    private fun initNewsRecyclerView(newsList: ArrayList<News>) {
        newsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        newsadApter = NewsAdapter(newsList)
        newsRecyclerView.adapter = newsadApter
    }

    private fun initKeywordRecyclerView(itemList: ArrayList<Items>) {
        keywordRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        keywordAdapter = itemAdapter(itemList)
        keywordRecyclerView.adapter = keywordAdapter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.politic -> {
                NewsCrawling(100)
                now.text = "정치"
                true
            }
            R.id.economic -> {
                NewsCrawling(101)
                now.text = "경제"
                true
            }
            R.id.society -> {
                NewsCrawling(102)
                now.text = "사회"
                true
            }
            R.id.life -> {
                NewsCrawling(103)
                now.text = "생활/문화"
                true
            }
            R.id.IT -> {
                NewsCrawling(104)
                now.text = "IT/과학"
                true
            }
            R.id.world -> {
                NewsCrawling(105)
                now.text = "세계"
                true
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}