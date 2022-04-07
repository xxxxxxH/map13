package cn.nba.map.live.gps.navigation.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import cn.nba.map.live.gps.navigation.R
import cn.nba.map.live.gps.navigation.entity.NearbyEntity
import com.bumptech.glide.Glide
import uk.co.ribot.easyadapter.ItemViewHolder
import uk.co.ribot.easyadapter.PositionInfo
import uk.co.ribot.easyadapter.annotations.LayoutId
import uk.co.ribot.easyadapter.annotations.ViewId

@SuppressLint("NonConstantResourceId")
@LayoutId(R.layout.item_near)
class NearbyItem(view: View) : ItemViewHolder<NearbyEntity>(view) {
    @ViewId(R.id.itemIv)
    var itemIv: ImageView? = null

    @ViewId(R.id.itemTv)
    var itemTv: TextView? = null

    @ViewId(R.id.card)
    var card: CardView? = null
    override fun onSetValues(item: NearbyEntity?, positionInfo: PositionInfo?) {
        Glide.with(context).load(item!!.img).placeholder(R.drawable.cat).into(itemIv!!)
        itemTv!!.text = item.name.substring(0, item.name.length - 4)
        card?.click {
            (listener as ItemCallback).itemClick(positionInfo?.position!!, item)
        }
    }
}