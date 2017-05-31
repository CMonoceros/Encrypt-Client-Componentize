package zjm.cst.dhu.menumodule.view

import android.annotation.TargetApi
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.daimajia.swipe.util.Attributes

import java.util.ArrayList

import javax.inject.Inject

import zjm.cst.dhu.basemodule.MyApplication
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule
import zjm.cst.dhu.basemodule.model.EncryptRelation
import zjm.cst.dhu.basemodule.model.EncryptType
import zjm.cst.dhu.basemodule.model.File
import zjm.cst.dhu.basemodule.view.BaseFragment
import zjm.cst.dhu.menumodule.FileTypeContract
import zjm.cst.dhu.menumodule.dagger2.component.DaggerFileTypeFragmentComponent
import zjm.cst.dhu.menumodule.dagger2.module.FileTypeModule
import zjm.cst.dhu.menumodule.R
import zjm.cst.dhu.menumodule.databinding.ModuleMenuFileTypeBinding
import zjm.cst.dhu.menumodule.view.adapter.FileTypeAdapter

/**
 * Created by zjm on 2017/3/3.
 */
@TargetApi(21)
class FileTypeFragment : BaseFragment(), FileTypeContract.View {

    @Inject
    lateinit var fileTypePresenter: FileTypeContract.Presenter
    private var sourceEncryptTypeList: MutableList<EncryptType>? = null
    private var file: File? = null
    private var fileTypeAdapter: FileTypeAdapter? = null

    var module_menu_rcv_file_type: RecyclerView? = null
    var adb_menu_file_encrypt: android.support.v7.app.AlertDialog.Builder? = null
    var adb_others: android.support.v7.app.AlertDialog.Builder? = null

    private var et_menu_file_encrypt_exinf: EditText? = null
    private var tv_file_type_detail: TextView? = null
    private var rb_file_type_rate: RatingBar? = null
    private var v_menu_file_encrypt: View? = null
    private var vg_menu_file_encrypt: ViewGroup? = null

    private var type_rate:Float =3.toFloat()

    override fun updateEncryptType(list: List<EncryptType>) {
        sourceEncryptTypeList!!.clear()
        for (i in list.indices) {
            sourceEncryptTypeList!!.add(list[i])
        }
        fileTypeAdapter!!.notifyDataSetChanged()
    }

    override fun getFileTypeNetworkError() {
        Toast.makeText(activity, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show()
    }

    override fun typeDetailClick(encryptType: EncryptType) {
        setupTypeDetailDialog()
        adb_others!!.setTitle(encryptType.name)
        tv_file_type_detail!!.setText(encryptType.description)
        rb_file_type_rate!!.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            type_rate=rating
        }
        adb_others!!.setPositiveButton("确定") { dialog, which ->
            fileTypePresenter.setEncryptTypeRate(file!!.owner, encryptType.id, type_rate)
            dialog.dismiss()
        }
        adb_others!!.show()
    }

    override fun setEncryptTypeRateNetworkError() {
        Toast.makeText(activity, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show()
        rb_file_type_rate!!.rating = 3.toFloat()
    }

    override fun setFile(file: File) {
        this.file = file
    }


    override fun setDesKey(encryptRelation: EncryptRelation) {
        setupEncryptExinfDialog()
        adb_menu_file_encrypt!!.setTitle("输入DES密钥！")
        adb_menu_file_encrypt!!.setPositiveButton("确定") { dialog, which ->
            val i = et_menu_file_encrypt_exinf!!.text.toString()
            if (i.length < 8) {
                Toast.makeText(activity, "DES Key at least 8 bit!",
                        Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                fileTypePresenter!!.encryptBaseType(encryptRelation, i, "1")
            }
        }
        adb_menu_file_encrypt!!.show()
    }

    override fun encryptBaseTypeNetworkError() {
        Toast.makeText(activity, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show()
    }

    override fun encryptBaseTypeEncryptSuccess() {
        Toast.makeText(activity, "Encrypt File Success!",
                Toast.LENGTH_SHORT).show()
    }

    override fun decryptFileExistError() {
        Toast.makeText(activity, "File doesn't exist!Please download first!",
                Toast.LENGTH_SHORT).show()
    }

    override fun decryptBaseTypeDecryptSuccess() {
        Toast.makeText(activity, "Decrypt File Success!",
                Toast.LENGTH_SHORT).show()
    }

    override fun decryptBaseTypeDecryptError() {
        Toast.makeText(activity, "Decrypt Error.Please try again later!",
                Toast.LENGTH_SHORT).show()
    }

    override fun decryptBaseTypeDecryptFailed() {
        Toast.makeText(activity, "Decrypt Failed.Please confirm your key!",
                Toast.LENGTH_SHORT).show()
    }

    override fun confirmDesKey(encryptRelation: EncryptRelation) {
        setupEncryptExinfDialog()
        adb_menu_file_encrypt!!.setTitle("输入DES密钥！")
        adb_menu_file_encrypt!!.setPositiveButton("确定") { dialog, which ->
            val i = et_menu_file_encrypt_exinf!!.text.toString()
            if (i.length < 8) {
                Toast.makeText(activity, "DES Key at least 8 bit!",
                        Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                fileTypePresenter!!.decryptBaseType(file, i)
            }
        }
        adb_menu_file_encrypt!!.show()
    }

    override val contentViewLayoutId: Int
        get() = R.layout.module_menu_file_type

    override fun injectDependences() {
        val applicationComponent = (activity.application as MyApplication).applicationComponent
        val fileTypeFragmentComponent = DaggerFileTypeFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(ActivityModule(activity))
                .fileTypeModule(FileTypeModule())
                .build()
        fileTypeFragmentComponent.inject(this)

        fileTypePresenter!!.attachView(this)
    }

    override fun dataBinding(view: View) {
        val binding = DataBindingUtil.bind<ModuleMenuFileTypeBinding>(view)
        module_menu_rcv_file_type = binding.moduleMenuRcvFileType
    }


    private fun setupView() {
        adb_others = android.support.v7.app.AlertDialog.Builder(activity)

        sourceEncryptTypeList = ArrayList<EncryptType>()
        fileTypeAdapter = FileTypeAdapter(activity, sourceEncryptTypeList)
        fileTypeAdapter!!.mode = Attributes.Mode.Single
        fileTypeAdapter!!.setDownloadClickListener {
            val menuActivity = activity as MenuActivity
            menuActivity.downloadFileStartService(file!!)
        }
        fileTypeAdapter!!.setEncryptClickListener { position ->
            val encryptRelation = EncryptRelation(fileId = file!!.id, typeId = sourceEncryptTypeList!![position].id)
            fileTypePresenter!!.encryptFile(encryptRelation)
        }
        fileTypeAdapter!!.setClickListener { position -> typeDetailClick(sourceEncryptTypeList!![position]) }
        fileTypeAdapter!!.setDecryptClickListener { position ->
            val encryptRelation = EncryptRelation(fileId = file!!.id, typeId = sourceEncryptTypeList!![position].id)
            fileTypePresenter!!.decryptFile(encryptRelation, file)
        }

        module_menu_rcv_file_type!!.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        module_menu_rcv_file_type!!.adapter = fileTypeAdapter

        fileTypePresenter!!.getEncryptTypeByOwner(file!!.owner)
    }

    private fun setupEncryptExinfDialog() {
        val inflater = activity.layoutInflater
        vg_menu_file_encrypt = activity.findViewById(R.id.module_menu_rl_file_encrypt_exinf) as? ViewGroup
        v_menu_file_encrypt = inflater.inflate(R.layout.module_menu_file_encrypt_exinf, vg_menu_file_encrypt)
        et_menu_file_encrypt_exinf = v_menu_file_encrypt!!.findViewById(R.id.module_menu_et_file_encrypt_exinf) as EditText
        adb_menu_file_encrypt = android.support.v7.app.AlertDialog.Builder(activity)
        adb_menu_file_encrypt!!.setView(v_menu_file_encrypt)
    }

    private fun setupTypeDetailDialog() {
        val inflater = activity.layoutInflater
        vg_menu_file_encrypt = activity.findViewById(R.id.module_menu_rl_file_type_rate) as? ViewGroup
        v_menu_file_encrypt = inflater.inflate(R.layout.module_menu_file_type_rate, vg_menu_file_encrypt)
        tv_file_type_detail = v_menu_file_encrypt!!.findViewById(R.id.module_menu_tv_file_type_detail) as TextView
        rb_file_type_rate = v_menu_file_encrypt!!.findViewById(R.id.module_menu_rb_file_type_rate) as RatingBar
        adb_others = android.support.v7.app.AlertDialog.Builder(activity)
        adb_others!!.setView(v_menu_file_encrypt)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val menuActivity = activity as MenuActivity
        menuActivity.outToMenu()
        setupView()
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.getmRefWatcher(activity)!!.watch(this)
    }

}
