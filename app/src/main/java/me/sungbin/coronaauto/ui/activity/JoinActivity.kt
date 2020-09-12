package me.sungbin.coronaauto.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.sungbin.sungbintool.util.PermissionUtil
import com.sungbin.sungbintool.util.StorageUtil
import com.sungbin.sungbintool.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.sungbin.coronaauto.R
import me.sungbin.coronaauto.tool.manager.PathManager
import org.json.JSONObject
import org.jsoup.Connection
import javax.inject.Inject

@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {

    @Inject
    lateinit var client: Connection

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

        btn_request_storage.setOnClickListener {
            PermissionUtil.request(
                this,
                null,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            StorageUtil.createFolder(PathManager.MAIN, true)
            StorageUtil.createFolder(PathManager.SCHOOL, true)
            StorageUtil.createFolder(PathManager.STUDENT, true)
            btn_request_storage.apply {
                alpha = 0.5f
                text = context.getString(R.string.join_permission_granted)
            }
            btn_start.apply {
                alpha = 1f
                setOnClickListener {
                    StorageUtil.createFolder(PathManager.MAIN, true)

                    val school = tiet_school.text.toString()
                    val area = actv_area.text.toString()
                    val name = tiet_name.text.toString()
                    val birth = tiet_birth.text.toString()

                    if (school.isNotBlank() && area.isNotBlank() && name.isNotBlank() && birth.isNotBlank()) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val schoolData = async {
                                client.data("schulNm", school).data("geoNm", area).get().text()
                            }
                            val schoolJson = JSONObject(schoolData.await())
                            val schoolCode = schoolJson["schulNm"]

                            if (schoolCode == getString(R.string.join_school_search_none)) {
                                ToastUtil.show(
                                    applicationContext,
                                    getString(R.string.join_school_search_error),
                                    ToastUtil.SHORT, ToastUtil.WARNING
                                )
                            } else {
                                val studentData = async {
                                    client.data("pName", name).data("frnoRidno", birth).get().text()
                                }
                                StorageUtil.save(
                                    "${PathManager.SCHOOL}/$school.json",
                                    schoolJson.toString(),
                                    true
                                )
                                StorageUtil.save(
                                    "${PathManager.STUDENT}/$name.json",
                                    studentData.await(),
                                    true
                                )
                                ToastUtil.show(
                                    applicationContext,
                                    getString(R.string.join_welcome),
                                    ToastUtil.SHORT, ToastUtil.SUCCESS
                                )
                            }
                        }
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
    }

}