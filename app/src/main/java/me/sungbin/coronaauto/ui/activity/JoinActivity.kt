package me.sungbin.coronaauto.ui.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.sungbin.sungbintool.util.Logger
import com.sungbin.sungbintool.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_join.*
import me.sungbin.coronaauto.R
import me.sungbin.coronaauto.ui.dialog.LoadingDialog
import org.jsoup.Connection
import javax.inject.Inject

@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {

    @Inject
    lateinit var client: Connection

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val areas = listOf(
            "서울",
            "경기",
            "대전",
            "대구",
            "부산",
            "인천",
            "광주",
            "울산",
            "세종",
            "충북",
            "충남",
            "경북",
            "경남",
            "강원",
            "전북",
            "전남",
            "제주",
        )
        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, areas)
        actv_area.setAdapter(adapter)

        btn_start.setOnClickListener {
            val school = tiet_school.text.toString()
            val area = actv_area.text.toString()
            val name = tiet_name.text.toString()
            val birth = tiet_birth.text.toString()

            if (school.isNotBlank() && area.isNotBlank() && name.isNotBlank() && birth.isNotBlank()) {
                Logger.w(client.data("schulNm", school)
                    .data("geoNm", area)
                    .get().text())
            } else {
                ToastUtil.show(
                    applicationContext,
                    getString(R.string.join_input_all),
                    ToastUtil.SHORT,
                    ToastUtil.WARNING
                )
            }
        }
    }
}