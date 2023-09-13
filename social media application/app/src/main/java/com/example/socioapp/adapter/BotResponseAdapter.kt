import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socioapp.ItemType
import com.example.socioapp.R
import com.example.socioapp.responses.botreponseItem


class BotResponseAdapter(private val messageList: ArrayList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ItemType.SENDER.ordinal -> {
                val view = inflater.inflate(R.layout.item_sender, parent, false)
                SenderViewHolder(view)
            }
            ItemType.RECEIVER.ordinal -> {
                val view = inflater.inflate(R.layout.item_receiver, parent, false)
                ReceiverViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        when (holder.itemViewType) {
            ItemType.SENDER.ordinal -> {
                val senderViewHolder = holder as SenderViewHolder
                senderViewHolder.senderTextView.text = message
            }
            ItemType.RECEIVER.ordinal -> {
                val receiverViewHolder = holder as ReceiverViewHolder
                receiverViewHolder.receiverTextView.text = message
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            ItemType.SENDER.ordinal
        } else {
            ItemType.RECEIVER.ordinal
        }
    }

    class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderTextView: TextView = itemView.findViewById(R.id.senderTextView)
    }

    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiverTextView: TextView = itemView.findViewById(R.id.receiverTextView)
    }
}

