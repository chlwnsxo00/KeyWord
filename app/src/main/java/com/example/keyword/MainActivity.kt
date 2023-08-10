package com.example.keyword

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keyword.adapter.NewsAdapter
import com.example.keyword.adapter.itemAdapter
import com.example.keyword.data.Items
import com.example.keyword.data.News
import com.example.keyword.databinding.ActivityMainBinding
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.databinding.ItemPowerMenuLibrarySkydovesBinding.inflate
import com.skydoves.powermenu.databinding.LayoutPowerBackgroundLibrarySkydovesBinding.inflate
import com.skydoves.powermenu.databinding.LayoutPowerMenuLibrarySkydovesBinding.inflate
import com.skydoves.powermenu.kotlin.powerMenu
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.lang.Runnable


class MainActivity : AppCompatActivity() {
    private val moreMenu by powerMenu<MoreMenuFactory>()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPowerMenu()
        getKeyword()
        NewsCrawling()
    }


    private fun initPowerMenu() {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
    }

    // 이 액티비티와 top_menu_main_index를 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        //R은 res 폴더의 약자. res폴더 안에 있는 context_menu_main.xml 파일과 연결시킨다.
        return super.onCreateOptionsMenu(menu)
    }

    // 상단 메뉴 item 선택시 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dehaze -> {
                moreMenu.showAsDropDown(item.actionView)
                moreMenu.setOnMenuItemClickListener { position, item ->
                    moreMenu.selectedPosition = position
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    private fun NewsCrawling() {
        Thread(Runnable {
            val doc =
                Jsoup.connect("https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=102")
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
}