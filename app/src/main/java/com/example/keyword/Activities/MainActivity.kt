package com.example.keyword.Activities

import SearchItems
import `in`.srain.cube.views.ptr.PtrClassicFrameLayout
import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.keyword.R
import com.example.keyword.SearchAPIService.SearchApiService
import com.example.keyword.SumAPIService.Sum
import com.example.keyword.SumAPIService.SumApiService
import com.example.keyword.`interface`.KeywordClick
import com.example.keyword.`interface`.NewsClick
import com.example.keyword.adapter.NewsAdapter
import com.example.keyword.adapter.itemAdapter
import com.example.keyword.data.Items
import com.example.keyword.data.News
import com.example.keyword.keywordAPI.KeywordAPIService
import com.example.keyword.keywordAPI.KeywordResponse
import com.example.keyword.naverAPI.NaverApiService
import com.example.keyword.naverAPI.descriptionItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    NewsClick, KeywordClick {
    private var sid = 102
    private var Address = ""
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
    private lateinit var refreshLayout: PtrClassicFrameLayout
    private val fab: FloatingActionButton by lazy {
        findViewById(R.id.fab)
    }
    private val nsv: androidx.core.widget.NestedScrollView by lazy {
        findViewById(R.id.nsv)
    }
    private val power_menu: ConstraintLayout by lazy {
        findViewById(R.id.powerMenu)
    }
    private val power_title: TextView by lazy {
        findViewById(R.id.power_title)
    }
    private val power_press: TextView by lazy {
        findViewById(R.id.power_press)
    }
    private val power_body: TextView by lazy {
        findViewById(R.id.power_sum)
    }
    private val power_close: AppCompatButton by lazy {
        findViewById(R.id.close)
    }
    private val power_readMore: AppCompatButton by lazy {
        findViewById(R.id.readMore)
    }
    private val power_image: ImageView by lazy {
        findViewById(R.id.power_image)
    }
    private val power_background: FrameLayout by lazy {
        findViewById(R.id.powerMenuBackground)
    }
    private val progress_wheel: com.pnikosis.materialishprogress.ProgressWheel by lazy {
        findViewById(R.id.progress_wheel)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPowerMenu()
        getKeyword(sid)
        NewsCrawling(sid)
        initList()
        initRefreshLayout()
        initFloatingButton()
        initPowerMenuBackground()
    }

    private fun initPowerMenuBackground() {
        power_background.setOnClickListener { }
    }

    private fun initPowerMenu() {
        power_menu.isClickable = false
        power_menu.setOnClickListener { }
        initCloseButton()
        initReadMoreButton()
    }

    private fun initReadMoreButton() {
        power_readMore.setOnClickListener {
            val intent = Intent(this, ArticleActivity::class.java)
            intent.putExtra("address", Address)
            startActivity(intent)
        }
    }

    private fun initCloseButton() {
        power_close.setOnClickListener {
            power_menu.isVisible = false
            power_menu.isClickable = false
            power_background.isClickable = false
            power_background.isVisible = false
            fab.isVisible = true
            fab.isClickable = true
        }
    }

    private fun initFloatingButton() {
        fab.setOnClickListener {
            nsv.smoothScrollTo(0, 0)
        }
    }

    private fun initRefreshLayout() {
        refreshLayout = findViewById(R.id.store_house_ptr_frame)
        refreshLayout.setPtrHandler(object : PtrDefaultHandler() {
            override fun onRefreshBegin(frame: PtrFrameLayout) {
                NewsCrawling(sid)
                // 예를 들어, 데이터를 다시 로드하거나 업데이트할 수 있습니다.
                refreshLayout.postDelayed({
                    // 리프레시 완료 후 호출
                    refreshLayout.refreshComplete()
                }, 1000) // 예시: 2초 후 리프레시 완료
            }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun initList() {
        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type
        setSupportActionBar(toolbar)

        ivMenu.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        val searchView = toolbar.findViewById<SearchView>(R.id.search)
        val searchAutoComplete: SearchView.SearchAutoComplete =
            searchView.findViewById(androidx.appcompat.R.id.search_src_text)

        searchAutoComplete.setTextColor(Color.WHITE)
        searchAutoComplete.setHintTextColor(Color.WHITE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null)
                    searchKeyword(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        val navigationView: NavigationView = findViewById(R.id.navigation)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun getKeyword(sid: Int) {
        itemList.clear()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://songssam.site:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS) // Adjust the timeout as needed
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()

        val apiService = retrofit.create(KeywordAPIService::class.java)

        val call = apiService.getKeywords(0, sid)
        call.enqueue(object : Callback<KeywordResponse> {
            override fun onResponse(
                call: Call<KeywordResponse>,
                response: Response<KeywordResponse>
            ) {
                if (response.isSuccessful.not())
                    return
                val keywordData = response.body()?.response
                keywordData?.let {
                    Log.d("response", it.toString())
                    itemList.add(Items(it.one))
                    itemList.add(Items(it.two))
                    itemList.add(Items(it.three))
                    itemList.add(Items(it.four))
                    itemList.add(Items(it.five))
                    itemList.add(Items(it.six))
                    itemList.add(Items(it.seven))
                    itemList.add(Items(it.eight))
                    itemList.add(Items(it.nine))
                    itemList.add(Items(it.ten))
                }

                initKeywordRecyclerView(itemList)
            }

            override fun onFailure(call: Call<KeywordResponse>, t: Throwable) {
                Log.d("retrofit", t.stackTraceToString())
                // 네트워크 오류 등 호출 실패 시 처리
            }
        })
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
        newsadApter = NewsAdapter(newsList, this)
        newsRecyclerView.adapter = newsadApter

        refreshLayout.requestDisallowInterceptTouchEvent(false)
    }

    private fun initKeywordRecyclerView(itemList: ArrayList<Items>) {
        keywordRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        keywordAdapter = itemAdapter(itemList, this)
        keywordRecyclerView.adapter = keywordAdapter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.politic -> {
                sid = 100
                NewsCrawling(sid)
                getKeyword(sid)
                now.text = "정치"
                true
            }
            R.id.economic -> {
                sid = 101
                NewsCrawling(sid)
                getKeyword(sid)
                now.text = "경제"
                true
            }
            R.id.society -> {
                sid = 102
                NewsCrawling(sid)
                getKeyword(sid)
                now.text = "사회"
                true
            }
            R.id.life -> {
                sid = 103
                NewsCrawling(sid)
                getKeyword(sid)
                now.text = "생활/문화"
                true
            }
            R.id.IT -> {
                sid = 105
                NewsCrawling(sid)
                getKeyword(sid)
                now.text = "IT/과학"
                true
            }
            R.id.world -> {
                sid = 104
                NewsCrawling(sid)
                getKeyword(sid)
                now.text = "세계"
                true
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun NewsClick(
        address: String,
        title: String,
        press: String,
        body: String,
        image: String
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://songssam.site:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS) // Adjust the timeout as needed
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()

        val apiService = retrofit.create(SumApiService::class.java)
        val call = apiService.sumBody(address)
        call.enqueue(object : Callback<Sum> {
            override fun onResponse(
                call: Call<Sum>,
                response: Response<Sum>
            ) {
                if (response.isSuccessful.not()) {
                    Toast.makeText(this@MainActivity, "서버가 닫혀있습니다!", Toast.LENGTH_LONG).show()
                    return
                }
                Log.d("sum", "연결 성공")
                val keywordData = response.body()?.sum
                Log.d("sum", "sum = " + response.body()?.sum)
                keywordData?.let {
                    Log.d("sum", it)
                    power_body.text = CleanSumText(it)
                }
                Thread(Runnable {
                    runOnUiThread {
                        power_title.text = title
                        power_press.text = press
                        Glide.with(this@MainActivity).load(image).into(power_image)
                        power_menu.isVisible = true
                        power_menu.isClickable = true
                        power_background.isClickable = true
                        power_background.isVisible = true
                        fab.isVisible = false
                        fab.isClickable = false
                        Address = address
                    }
                }).start()
            }

            override fun onFailure(call: Call<Sum>, t: Throwable) {
                Log.d("retrofit", t.stackTraceToString())
                // 네트워크 오류 등 호출 실패 시 처리
            }
        })
    }

    private fun CleanSumText(text: String): String {
        val lines = text.split("\n")
        val formattedLines = lines.map { "\t$it" }.joinToString("\n\n")
        return formattedLines
    }

    private fun searchKeyword(keyword: String) {
        progress_wheel.visibility = VISIBLE
        newsList.clear()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://songssam.site:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS) // Adjust the timeout as needed
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()

        val apiService = retrofit.create(SearchApiService::class.java)
        val call = apiService.searchNews(keyword)
        call.enqueue(object : Callback<SearchItems> {
            override fun onResponse(
                call: Call<SearchItems>,
                response: Response<SearchItems>
            ) {
                if (response.isSuccessful.not()) {
                    Log.d("search", "서버 정상 접속 실패")
                    progress_wheel.visibility=GONE
                    Toast.makeText(this@MainActivity, "서버가 닫혀있습니다!", Toast.LENGTH_LONG).show()
                    return
                }
                Log.d("search", "연결 성공")
                Log.d("search", "search = " + response.body()?.searchItems)
                val searchItems = response.body()?.searchItems
                if (searchItems != null) {
                    for (searchItem in searchItems) {
                        val news = News(
                            searchItem.title,
                            searchItem.img,
                            searchItem.summary,
                            searchItem.press,
                            searchItem.origin
                        )
                        newsList.add(news)
                    }
                }
                Thread(Runnable {
                    runOnUiThread {
                        progress_wheel.visibility= GONE
                        initNewsRecyclerView(newsList)
                    }
                }).start()
            }

            override fun onFailure(call: Call<SearchItems>, t: Throwable) {
                progress_wheel.isVisible=false
                Toast.makeText(this@MainActivity,"네트워크 오류 등의 이유로 호출 실패",Toast.LENGTH_SHORT).show()
                Log.d("retrofit", t.stackTraceToString())
                // 네트워크 오류 등 호출 실패 시 처리
            }
        })
    }

    override fun OnKeywordSelected(keyword: String) {
        searchKeyword(keyword)
    }

    override fun OnKeywordUnselected() {
        NewsCrawling(sid)
    }

    override fun OnLongKeywordClick(keyword: String, itemView: View) {
        val CLIENT_ID = "IP3Fb3DpFvR9wKPuRPsc"
        val CLIENT_SECRET = "LGQc25YVzU"
        var descryption = "설명이 보이는 칸입니다."

        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(NaverApiService::class.java)
        val callGetSearchNews = api.searchEncyclopedia(CLIENT_ID, CLIENT_SECRET, keyword, 1)

        callGetSearchNews.enqueue(object : Callback<descriptionItem> {
            override fun onResponse(
                call: Call<descriptionItem>,
                response: Response<descriptionItem>
            ) {
                if (response.isSuccessful.not())
                    return
                Log.d("naver", "연결 성공")
                val keywordData = response.body()?.items
                keywordData?.let {
                    descryption = it[0].description
                    Log.d("naver", it[0].description)
                }

                val balloon = Balloon.Builder(this@MainActivity)
                    .setWidthRatio(1.0f)
                    .setHeight(BalloonSizeSpec.WRAP)
                    .setText(removeB(descryption))
                    .setTextColorResource(R.color.white)
                    .setTextSize(15f)
                    .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                    .setArrowSize(10)
                    .setArrowPosition(0.5f)
                    .setPadding(12)
                    .setCornerRadius(8f)
                    .setBackgroundColorResource(R.color.blue)
                    .setBalloonAnimation(BalloonAnimation.ELASTIC)
                    .setLifecycleOwner(this@MainActivity)
                    .build()

                balloon.showAsDropDown(itemView)
            }

            override fun onFailure(call: Call<descriptionItem>, t: Throwable) {
                Log.d("naver", "실패 : $t")
            }
        })
    }

    private fun removeB(text: String): Spannable {
        val formattedText = SpannableStringBuilder()
        val startTag = "<b>"
        val endTag = "</b>"

        var currentIndex = 0
        while (currentIndex < text.length) {
            val startIndex = text.indexOf(startTag, currentIndex)
            if (startIndex != -1) {
                val endIndex = text.indexOf(endTag, startIndex)
                if (endIndex != -1) {
                    // Add the text before <b> tag
                    formattedText.append(text.substring(currentIndex, startIndex))

                    // Add the bold styled text
                    val boldText = text.substring(startIndex + startTag.length, endIndex)
                    formattedText.append(boldText).setSpan(
                        StyleSpan(Typeface.BOLD),
                        formattedText.length - boldText.length,
                        formattedText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    // Move the current index after the </b> tag
                    currentIndex = endIndex + endTag.length
                } else {
                    // No more <b> tags found, append the remaining text
                    formattedText.append(text.substring(currentIndex))
                    break
                }
            } else {
                // No <b> tag found, append the remaining text
                formattedText.append(text.substring(currentIndex))
                break
            }
        }

        return formattedText
    }

}