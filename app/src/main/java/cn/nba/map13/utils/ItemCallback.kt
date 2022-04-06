package cn.nba.map13.utils

import cn.nba.map13.entity.DataEntity

interface ItemCallback {
    fun itemClick(position:Int, any: Any)
}