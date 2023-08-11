package com.example.restaurantmanagement.presentation.activity.Home


import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.restaurantmanagement.R
import com.example.restaurantmanagement.Util.*
import com.example.restaurantmanagement.databinding.ActivityHomeBinding
import com.example.restaurantmanagement.domain.model.response.SalesReportItem
import com.example.restaurantmanagement.domain.model.response.TableOrdersItem
import com.example.restaurantmanagement.domain.model.response.response_login
import com.example.restaurantmanagement.presentation.activity.BaseActivity
import com.example.restaurantmanagement.presentation.activity.MainActivity.MainViewModel
import com.example.restaurantmanagement.presentation.activity.Menu.EditMenuActivity
import com.example.restaurantmanagement.presentation.activity.Menu.MenuActivity
import com.example.restaurantmanagement.presentation.activity.Subitems.AddSubItemActivity
import com.example.restaurantmanagement.presentation.activity.Subitems.EditSubMenuitemActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    lateinit var _binding:ActivityHomeBinding
    lateinit var pieChart: PieChart
    private val viewModel: HomeViewModel by viewModels()

    val forBarentries: ArrayList<BarEntry> = ArrayList()
    var counter:Float=0f
    val dateArray=kotlin.collections.ArrayList<String>()
    val ewan:kotlin.collections.ArrayList<Double> = ArrayList()

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    var sevendaysago:Date?= null
    var calendar:Calendar?= null
    var today:Date?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        pieChart = findViewById(R.id.pieChart)

        initbg()

        initializedTableOrders()

        _binding.swipeRefreshLayout.setOnRefreshListener {
            counter=0f
            forBarentries.clear()
            dateArray.clear()
            initializedTableOrders()
        }

        _binding.llMenu.setOnClickListener {
          startActivityWithAnimation<MenuActivity>(R.anim.screenslideright,R.anim.screen_slide_out_left)
        }
        _binding.llEditmenu.setOnClickListener {
            startActivityWithAnimation<EditMenuActivity>(R.anim.screenslideright,R.anim.screen_slide_out_left)
        }

        _binding.llAddSubmenu.setOnClickListener {
            startActivityWithAnimation<AddSubItemActivity>(R.anim.screenslideright,R.anim.screen_slide_out_left)
        }
        _binding.txtEdititemItem.setOnClickListener {
            startActivityWithAnimation<EditSubMenuitemActivity>(R.anim.screenslideright,R.anim.screen_slide_out_left)
        }
    }

    val initbg={
        val black = "#000000"
        val blackColorInt = Color.parseColor(black)
        val superLightBlack = Color.argb(20, Color.red(blackColorInt), Color.green(blackColorInt), Color.blue(blackColorInt))
        _binding.bottomBar.setBackgroundColor(superLightBlack)
        _binding.cvSales.setBackgroundColor(superLightBlack)
    }

    val initializedTableOrders={
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date= formatter.format(currentDate)
        viewModel.getTableOrdersForChart(date.toString())
    }

    val initializeTotalSales={
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date= formatter.format(currentDate)
        viewModel.getSalesReportForChart(date.toString())
    }

    val initializeSalesForGraph={

        calendar = Calendar.getInstance()
        today = calendar?.time

        calendar?.add(Calendar.DAY_OF_YEAR, -7)
        sevendaysago = calendar?.time

        if (today != null) {
            if (today!! > sevendaysago ) {


                val formattedDate = dateFormat.format(sevendaysago)
                viewModel.getSalesReportLoop(formattedDate.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.tableorders.observe(this, Observer {
            state -> ProcessTableOrders(state)
        })

        viewModel.salesreport.observe(this, Observer {
            state -> ProcessSalesReport(state)
        })

        viewModel.salesreportloop.observe(this, Observer {
            state -> ProcessSalesReportLoop(state)
        })

    }

    private fun ProcessTableOrders(state: ResultState<ArrayList<TableOrdersItem>>){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{
                _binding.swipeRefreshLayout.isRefreshing = false
                var totalPaid:Double?= 0.0
                var totalunpaid:Double?= 0.0
                hideProgressDialog()
                val isPaid=  state.data.filter {it.isPaid}
                val ispending = state.data.filter { !it.isPaid }

                if (isPaid.size > 0 ) {
                    if (totalPaid != null) {
                        isPaid.forEach {
                            totalPaid += it.orderPrice
                        }
                        _binding.txtTotalsale.text= totalPaid.toString()
                        _binding.txtPaidCustomer.text= "Customer: ${isPaid.size}"
                    }
                }else {
                    _binding.txtTotalsale.text= 0.0.toString()
                    _binding.txtPaidCustomer.text= "Customer: ${0}"
                }

                if (ispending.size > 0){
                    if(totalunpaid!= null){
                        ispending.forEach {
                            totalunpaid += it.orderPrice
                        }
                        _binding.cvUnpaid.text=totalunpaid.toString()
                        _binding.txtUnpaidCustomer.text= "Customer: ${ispending.size}"
                    }
                }else
                {
                    _binding.cvUnpaid.text=0.0.toString()
                    _binding.txtUnpaidCustomer.text= "Customer: ${0}"
                }

                showchart(isPaid.size.toFloat(),ispending.size.toFloat())
                //initializeSalesForGraph()
                initializeTotalSales()



            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }

    private fun ProcessSalesReport(state: ResultState<ArrayList<SalesReportItem>>){
        when(state){
            is ResultState.Loading ->{}
            is ResultState.Success->{
                var barentry:BarEntry?= null
                hideProgressDialog()
                var amount:Double=0.0
                state.data.forEach {
                 amount += it.total
                }
                if (state.data.size > 0 ) {
                    val decimalFormat = DecimalFormat("#.00")
                    val formattedNumber = decimalFormat.format(amount)
                   _binding.txtTotalsaleSalereport.text=formattedNumber.toString()
                   // initializeSalesForGraph()
                }else {
                    _binding.txtTotalsaleSalereport.text=0.0.toString()
                }
                initializeSalesForGraph()
            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }

    }

    private fun ProcessSalesReportLoop(state: ResultState<ArrayList<SalesReportItem>>){
        when(state){
            is ResultState.Loading ->{}
            is ResultState.Success->{
                var barentry:BarEntry?= null
                hideProgressDialog()
                var amount:Double=0.0
                state.data.forEach {
                    amount += it.total
                }
                barentry= BarEntry(counter,amount.toFloat())
                forBarentries?.add(barentry)
                counter = counter +1
                calendar?.add(Calendar.DAY_OF_YEAR, +1)
                sevendaysago=calendar?.time
                if(today!! >= sevendaysago){
                    val formattedDate = dateFormat.format(sevendaysago)
                    viewModel.getSalesReportLoop(formattedDate.toString())
                }else initGraph()

            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }

    //region THIS IS GRAPH
    val showchart:(Float,Float) -> Unit = { ispaid,notpaid ->
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.setDragDecelerationFrictionCoef(0.95f)

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(R.color.border_color)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        pieChart.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(ispaid))
        entries.add(PieEntry(notpaid))
        entries.add(PieEntry(notpaid))

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Mobile OS")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.purple_200))
        colors.add(resources.getColor(R.color.yellow))
        colors.add(resources.getColor(R.color.red))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }

    val initGraph={
        val barChart: BarChart = findViewById(R.id.barChart)
//        val entries = arrayListOf(
//            BarEntry(0f, 20f),
//            BarEntry(1f, 35f),
//            BarEntry(2f, 10f),
//            BarEntry(3f, 45f),
//            BarEntry(4f, 45f),
//            BarEntry(5f, 45f),
//            BarEntry(6f, 45f)
//        )
        val entries= forBarentries

        val dataSet = BarDataSet(entries, "")
        dataSet.color = Color.BLUE


        val data = BarData(dataSet)
        barChart.data = data


        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(listOf("DAY 1", "DAY 2", "DAY 3", "DAY 4","DAY 5","DAY 6","YESTERDAY","TODAY"))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        xAxis.textColor = Color.WHITE
        xAxis.textSize=15f// Change the text color of the x-axis labels

        val leftAxis = barChart.axisLeft
        leftAxis.textColor = Color.GREEN
        leftAxis.textSize=15f
        // Change the text color of the left y-axis labels

        val rightAxis = barChart.axisRight
        rightAxis.textColor = Color.YELLOW
        rightAxis.textSize=15f

        barChart.setFitBars(true)
        barChart.invalidate()

    }

    //endregion

    val changeActivity:(Class<Any>)-> Unit={
        val intent = Intent(this, it::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_vertical,
            R.anim.exit_vertical);
    }
}