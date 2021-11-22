package tech.araki.smartmemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.util.TimeUtil.toDateString
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * MainActivityで管理するRecyclerView用のAdapter
 * @param onItemClick ViewHolderのItemViewをクリックした際の処理
 */
class MainAdapter(
    private val onItemClick: (Memo)-> Unit
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var memoItems: List<Memo> = emptyList()

    // ItemViewのレイアウトをViewHolderにInflateする
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.memo_item_view, parent, false)
        )
    }

    // ViewHolderが作られた時や更新された時に呼ばれるメソッド
    // ここでViewの中身を決めたりViewの設定を行う
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        // positionからどのデータをViewHolderに保持させるViewを決める
        val memo = memoItems[position]

        holder.title.text = memo.title
        holder.createdDate.text = memo.createdTimeMillis.toDateString()
        holder.updatedData.text = memo.updateTimeMillis.toDateString()
        holder.expiredDate.text = memo.expireTimeMillis.toDateString()

        holder.itemView.setOnClickListener { onItemClick(memo) }
    }

    override fun getItemCount(): Int = memoItems.size

    /** データの変更とデータ変更通知 */
    fun setMemoItems(items: List<Memo>) {
        memoItems = items
        notifyDataSetChanged()
    }

    // 一つ一つのItemViewを持つ (Holdする) ViewHolder
    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.memo_title)
        val createdDate: TextView = itemView.findViewById(R.id.memo_created_date)
        val updatedData: TextView = itemView.findViewById(R.id.memo_updated_date)
        val expiredDate: TextView = itemView.findViewById(R.id.memo_expired_date)
    }
}