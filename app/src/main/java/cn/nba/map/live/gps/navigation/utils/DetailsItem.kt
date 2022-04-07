package cn.nba.map.live.gps.navigation.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import cn.nba.map.live.gps.navigation.R
import cn.nba.map.live.gps.navigation.entity.DataEntity
import com.bumptech.glide.Glide
import uk.co.ribot.easyadapter.ItemViewHolder
import uk.co.ribot.easyadapter.PositionInfo
import uk.co.ribot.easyadapter.annotations.LayoutId
import uk.co.ribot.easyadapter.annotations.ViewId

@SuppressLint("NonConstantResourceId")
@LayoutId(R.layout.item_near)
class DetailsItem(view: View) : ItemViewHolder<DataEntity>(view) {


    @ViewId(R.id.itemIv)
    var itemIv: ImageView? = null

    @ViewId(R.id.itemTv)
    var itemTv: TextView? = null

    @ViewId(R.id.card)
    var card: CardView? = null
    override fun onSetValues(item: DataEntity?, positionInfo: PositionInfo?) {
        Glide.with(context).load(item!!.imageUrl).into(itemIv!!)
        itemTv!!.text = item.title
        card?.let {
            it.setOnClickListener {
                listener?.let { l ->
                    (l as ItemCallback).itemClick(positionInfo?.position!!, item)
                }
            }
        }
    }

}