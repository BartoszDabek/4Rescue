package pl.bdabek.a4rescue.adapter

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import pl.bdabek.a4rescue.R
import pl.bdabek.a4rescue.persistence.Contact
import pl.bdabek.a4rescue.viewmodel.ContactViewModel


class ContactListAdapter internal constructor(context: Context) : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    private val TAG = ContactListAdapter::class.java.simpleName
    private val contactViewModel: ContactViewModel = ContactViewModel(context.applicationContext as Application)
    private var contacts: MutableList<Contact>? = null
    private var oldContacts: List<Contact> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder")
        val currentContact = contacts!![position]
        holder.contactName.text = currentContact.userName
        holder.contactPhone.text = currentContact.phoneNumber
        holder.contactIcon.setImageDrawable(generateUniqueIcon(currentContact.userName))
    }

    private fun generateUniqueIcon(userName: String): TextDrawable? {
        val generator = ColorGenerator.MATERIAL
        val color = generator.getColor(userName)

        val builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round()

        return builder.build(userName.first().toUpperCase().toString(), color)
    }

    internal fun insertContacts(words: MutableList<Contact>) {
        contacts = words
        if (words.size > oldContacts.size) {
            Log.d(TAG, "Inserting contact(s)")
            notifyDataSetChanged()
        }
        oldContacts = words.toList()
    }

    internal fun removeContact(position: Int) {
        val contactToRemove = contacts?.removeAt(position)
        contactViewModel.delete(contactToRemove!!)

        Log.d(TAG, "Contact $contactToRemove is going to be removed")
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount - position)
    }

    override fun getItemCount(): Int {
        return if (contacts != null) {
            Log.d(TAG, "Contacts defined: ${contacts!!.size}")
            contacts!!.size
        } else {
            Log.d(TAG, "Contacts list is a null")
            0
        }
    }

    inner class ContactViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val contactName: TextView = itemView.findViewById(R.id.contact_name_tv)
        internal val contactPhone: TextView = itemView.findViewById(R.id.contact_phone_number_tv)
        internal val contactIcon: ImageView = itemView.findViewById(R.id.contact_image_view)
    }
}