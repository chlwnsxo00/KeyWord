package com.example.keyword

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balysv.materialmenu.MaterialMenuDrawable
import com.example.keyword.adapter.NewsAdapter
import com.example.keyword.adapter.itemAdapter
import com.example.keyword.data.Items
import com.example.keyword.data.News
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import org.jsoup.Jsoup
import org.jsoup.select.Elements


class MainActivity : AppCompatActivity() {
    private lateinit var powerMenu: PowerMenu
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
    private val drawerLayout: DrawerLayout? = null
    private var isDrawerOpened = false
    private val materialMenu: MaterialMenuDrawable? = null

    private val onMenuItemClickListener = OnMenuItemClickListener<PowerMenuItem> { position, item ->
        // 아이템 클릭 시 수행할 동작을 정의합니다.
        // 예: 아이템 이름을 출력하거나 해당 아이템에 맞는 동작을 수행합니다.
        Toast.makeText(this@MainActivity, "Clicked: ${item.title}", Toast.LENGTH_SHORT).show()
        powerMenu.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getKeyword()
        NewsCrawling()
        initPowerMenu()
        initMaterialMenu()
    }


    private fun initPowerMenu() {
        powerMenu = PowerMenu.Builder(this)
            .addItem(PowerMenuItem("정치", false))
            .addItem(PowerMenuItem("경제", false))
            .addItem(PowerMenuItem("사회", false))
            .addItem(PowerMenuItem("생활/문화", false))
            .addItem(PowerMenuItem("IT/과학", false))
            .addItem(PowerMenuItem("세계", false))
            .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
            .setMenuRadius(10f)
            .setMenuShadow(10f)
            .setTextColor(getColor(R.color.black))
            .setTextGravity(Gravity.CENTER)
            .setTextTypeface(Typeface.DEFAULT_BOLD)
            .setSelectedTextColor(getColor(R.color.blue))
            .setMenuColor(Color.WHITE)
            .setSelectedMenuColor(getColor(R.color.blue))
            .setOnMenuItemClickListener(onMenuItemClickListener)
            .build()

        yourView.setOnClickListener {
            powerMenu.showAsDropDown(it)
        }

    }

    private fun getKeyword() {
        //TODO("keyword 받아 오기")
        itemList.add(Items("keyword1"))
        itemList.add(Items("keyword2"))
        itemList.add(Items("keyword3"))
        itemList.add(Items("keyword4"))
        itemList.add(Items("keyword5"))
        itemList.add(Items("keyword5"))
        itemList.add(Items("keyword5"))
        itemList.add(Items("keyword5"))
        itemList.add(Items("keyword5"))
        itemList.add(Items("keyword5"))
        itemList.add(Items("keyword5"))
        itemList.add(Items("keyword5"))

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