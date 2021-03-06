package com.ikang.libmvi.base.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aleyn.mvvm.event.Message
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.ikang.libmvi.R
import com.ikang.libmvi.base.BaseViewModel
import com.ikang.libmvi.base.ui.IBaseView
import com.ikang.libmvi.base.ui.fragment.IKToolbar
import com.ikang.libmvi.util.RomUtils
import com.ikang.libmvi.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.layout_loading_view.*
import org.jetbrains.anko.toast
import java.lang.reflect.ParameterizedType


/**
 * @author IK-zhulk
 * @version 1.0.0
 * @describe Activity父类，所有的activity都应该继承这个
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity(),
        IBaseView {

    //是否第一次加载
    private var isFirst: Boolean = true
    protected lateinit var viewModel: VM
    protected var mBinding: DB? = null

    var mToolbar: IKToolbar? = null
    var loadingView: View? = null
    var errorView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        //注册 UI事件
        registorDefUIChange()
        //router自动注入
        ARouter.getInstance().inject(this)

        initView(savedInstanceState)
        initData()

        handleStatusBar()
    }


    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()

    /**
     * DataBinding
     */
    private fun initViewDataBinding() {
        val cls =
                (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            bindContentView()
        } else {
            setContentView(R.layout.activity_base)
            val inflate = LayoutInflater.from(this).inflate(layoutId(), mContainer, false)
            mContainer.addView(inflate)
        }
        setToolBar(resources.getString(R.string.app_name), true)
        createViewModel()
    }

    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel =
                    ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(tClass) as VM
        }
    }

    /**
     * 处理content 和 titleBar
     */
    private fun bindContentView() {
        setContentView(R.layout.activity_base)
        mBinding = DataBindingUtil.inflate(layoutInflater, layoutId(), mContainer, false)
        mBinding?.lifecycleOwner = this

        mContainer.addView(mBinding?.root)
    }


    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        viewModel.defUI.showDialog.observe(this, Observer {
            showLoading()
        })
        viewModel.defUI.dismissDialog.observe(this, Observer {
            hideLoading()
        })
        viewModel.defUI.toastEvent.observe(this, Observer {
            toast(it)
        })
        viewModel.defUI.msgEvent.observe(this, Observer {
            handleEvent(it)
        })
    }


    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */

    override fun showLoading() {
        if (this.isFinishing || this.isDestroyed) {
            return
        }

        if (loadingView == null) {
            loadingView = vsLoading.inflate()
        }
        if (loadingView?.visibility != View.VISIBLE) {
            loadingView?.visibility = View.VISIBLE
        }
        Glide.with(img_progress).load(R.drawable.basic_loading_wait_dialog).into(img_progress)
        errorView?.visibility = View.GONE
    }


    protected fun setToolBar(
            title: CharSequence,
            hasTitleBar: Boolean = true
    ) {
        if (hasTitleBar) {
            if (mToolbar == null) {
                mToolbar = vsTitleLayout.inflate() as IKToolbar?
            }
            mToolbar?.run {
                visibility = View.VISIBLE
                setMainTitle(title)
                setLeftTitleClickListener(View.OnClickListener {
                    supportFinishAfterTransition()
                })
            }
        } else {
            mToolbar?.visibility = View.GONE
        }
    }


    fun handleStatusBar() {
        setStatusBar()
        setStatusBarIcon(true)
    }

    /**
     * 设置状态栏的背景颜色
     */
    open fun setStatusBar() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.common_white),0)
    }

    /**
     * 设置状态栏图标的颜色
     *
     * @param dark true: 黑色  false: 白色
     */
    open fun setStatusBarIcon(dark: Boolean) {
        if (RomUtils.isSupportStatusBarDarkFont(this)) {
            if (dark) {
                StatusBarUtil.setLightMode(this)
            } else {
                StatusBarUtil.setDarkMode(this)
            }
        } else {
            StatusBarUtil.setTranslucent(this, 66)

        }

    }

    override fun hideLoading() {
        loadingView?.visibility = View.GONE
    }


    protected fun showError() {
        hideLoading()
        if (errorView == null) {
            errorView = vsError.inflate()
            // 点击加载失败布局
            errorView?.setOnClickListener {
                showLoading()
                onRefresh()
            }
        } else {
            errorView?.visibility = View.VISIBLE
        }
        mBinding?.root?.visibility = View.GONE
    }


    /**
     * 失败后点击刷新
     */
    protected open fun onRefresh() {
    }


//    fun toast(msg: String) {
//        msg.let { Toast.makeText(BaseApp.instance, msg, Toast.LENGTH_LONG).show() }
//    }


}