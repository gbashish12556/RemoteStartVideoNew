package com.example.remotestarvideonew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import java.util.ArrayList

class MainActivity : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener{

    private var viewModel: ViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var allVideos: ArrayList<Video>? = ArrayList()
    private var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var errorOuterLayout: ConstraintLayout? = null
    private var retryButton: Button? = null
    private var noteAdapter:ContactsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefreshLayout = findViewById(R.id.swipe_layout)
        swipeRefreshLayout!!.setOnRefreshListener(this);
        swipeRefreshLayout!!.visibility = View.GONE

        errorOuterLayout = findViewById(R.id.errorOuterLayout)
        retryButton = findViewById(R.id.retryButton)
        retryButton!!.setOnClickListener{

//            fetchAgain(null)
        }

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container)
        shimmerFrameLayout!!.startShimmerAnimation()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        noteAdapter = ContactsRecyclerViewAdapter(allVideos!!,this )
        recyclerView!!.adapter = noteAdapter
        intialiseViewModel()


    }

    fun intialiseViewModel(){

        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)

        viewModel!!.apiStatus.observe(this, Observer { aBoolean ->
            if (aBoolean == false) {
                apiFailed()
            }
        })

        viewModel!!.videos.observe(this, Observer { videos ->
            Handler().postDelayed({
                dataRecived(videos.results!!)
            },1000)
        })

    }

    override fun onRefresh() {
        fetchAgain("no-cache")
    }

    fun apiFailed(){
        shimmerFrameLayout!!.stopShimmerAnimation()
        shimmerFrameLayout!!.visibility = View.GONE
        swipeRefreshLayout!!.setRefreshing(false);
        swipeRefreshLayout!!.visibility = View.GONE
        errorOuterLayout!!.visibility = View.VISIBLE
    }

    fun dataRecived(result:ArrayList<Video>){

        shimmerFrameLayout!!.stopShimmerAnimation()
        shimmerFrameLayout!!.visibility = View.GONE
        swipeRefreshLayout!!.setRefreshing(false);
        swipeRefreshLayout!!.visibility = View.VISIBLE
        noteAdapter!!.setData(result)

    }

    fun fetchAgain(cahceControl:String?){
        swipeRefreshLayout!!.visibility = View.GONE
        shimmerFrameLayout!!.visibility = View.VISIBLE
        shimmerFrameLayout!!.startShimmerAnimation()
        errorOuterLayout!!.visibility = View.GONE
        viewModel!!.fetchVideos(cahceControl)
    }

}
