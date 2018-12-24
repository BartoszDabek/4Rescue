package pl.bdabek.a4rescue.util

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pl.bdabek.a4rescue.adapter.ContactListAdapter

class SwipeHelper(private val adapter: ContactListAdapter) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP, ItemTouchHelper.LEFT) {

    private val TAG = SwipeHelper::class.java.simpleName

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d(TAG, "Item on position: ${viewHolder.adapterPosition} is swiped")
        adapter.removeContact(viewHolder.adapterPosition)
    }

}