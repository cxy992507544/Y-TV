package com.example.myapplication.play

import android.content.Context
import android.text.method.Touch
import com.shuyu.aliplay.AliPlayerManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

// 静态的GSPlayer播放器配置帮助类
object GSPlayerSystem {

    /**
     * 设置播放器的内核
     * @param num 选择内核的编号
     * 0 - 系统播放器模式
     * 1 - IJK播放器模式，支持较多格式，但兼容性因设备而异
     * 2 - EXOPlayer模式，推荐处理流媒体
     * 3 - AliPlayer模式，适用于阿里云播放需求
     */
    fun setPlayHx(num: Int) {
        when (num) {
            0 -> PlayerFactory.setPlayManager(SystemPlayerManager::class.java) // 系统模式
            1 -> PlayerFactory.setPlayManager(IjkPlayerManager::class.java) // IJK模式
            2 -> PlayerFactory.setPlayManager(Exo2PlayerManager::class.java) // EXO模式
            3 -> PlayerFactory.setPlayManager(AliPlayerManager::class.java) // AliPlayer模式
        }
    }

    /**
     * 设置播放器的显示比例
     * @param num 选择显示比例的编号
     * 0 - 默认显示比例，根据视频自适应
     * 1 - 16:9比例，适合大多数宽屏视频
     * 2 - 全屏裁剪显示，适用于CoverImageView+FrameLayout
     * 3 - 全屏拉伸显示，适用于surface_container+FrameLayout
     * 4 - 4:3比例，适合经典视频比例
     */
    fun setPlayLook(num: Int) {
        when (num) {
            0 -> GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT) // 默认显示比例
            1 -> GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9) // 16:9显示比例
            2 -> GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL) // 全屏裁剪显示
            3 -> GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL) // 全屏拉伸显示
            4 -> GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3) // 4:3显示比例
        }
    }

    /**
     * 设置播放器的渲染模式
     * @param num 选择渲染模式的编号
     * 0 - TextureView模式，默认模式，兼容性较好
     * 1 - SurfaceView模式，适合无动画切换的场景，部分动画效果较差
     * 2 - GLSurfaceView模式，支持滤镜，适合需要图形处理的播放场景
     */
    fun setPlayStyle(num: Int) {
        when (num) {
            0 -> GSYVideoType.setRenderType(GSYVideoType.TEXTURE) // 默认TextureView
            1 -> GSYVideoType.setRenderType(GSYVideoType.SUFRACE) // SurfaceView
            2 -> GSYVideoType.setRenderType(GSYVideoType.GLSURFACE) // GLSurfaceView，支持滤镜
        }
    }

    /**
     * 设置播放的速度（倍率）
     * @param speed 播放速度，如 1.0f 表示正常速度，0.5f 为半速，2.0f 为双倍速
     * @param soundTouch 是否可以触摸调节速度
     */
    fun setPlaySpeed(speed: Float, soundTouch: Boolean) {
        GSYVideoManager.instance().setSpeed(speed, soundTouch)
    }

    /**
     * 退出全屏
     * @param context 对象
     */
    fun backFull(context: Context) {
        GSYVideoManager.backFromWindowFull(context)
    }

    /**
     * 清除播放器全部缓存
     * @param context 对象
     */
    fun clear(context: Context) {
        GSYVideoManager.instance().clearAllDefaultCache(context)
    }


    /**
     * 设置是否开启硬件加速 开始播放前设置
     * @param enable true 表示启用硬件加速，false 表示关闭硬件加速
     */
    fun setEnableHardwareAcceleration(enable: Boolean) {
        if (enable) {
            GSYVideoType.enableMediaCodec()
        } else {
            GSYVideoType.disableMediaCodec()
        }
    }


}
