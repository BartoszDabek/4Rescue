package pl.bdabek.a4rescue.fragments

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_contact.*
import pl.bdabek.a4rescue.R
import pl.bdabek.a4rescue.adapter.ContactListAdapter
import pl.bdabek.a4rescue.persistence.Contact
import pl.bdabek.a4rescue.util.SwipeHelper
import pl.bdabek.a4rescue.viewmodel.ContactViewModel


class ContactFragment : Fragment(), FragmentDrawerCheck {

    private val TAG = ContactFragment::class.java.simpleName
    private val PICK_CONTACT_REQUEST = 1
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")

        newContactBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            startActivityForResult(intent, PICK_CONTACT_REQUEST)
        }

        val adapter = setRecyclerView()
        setSwipeHelper(adapter)
        setViewModel(adapter)

        checkDrawer(activity!!, R.id.nav_contacts)
    }

    private fun setRecyclerView(): ContactListAdapter {
        Log.d(TAG, "setRecyclerView")
        val adapter = ContactListAdapter(context!!)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(context)

        return adapter
    }

    private fun setSwipeHelper(adapter: ContactListAdapter) {
        Log.d(TAG, "setSwipeHelper")
        val swipeHelper = SwipeHelper(adapter)
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(recyclerview)
    }

    private fun setViewModel(adapter: ContactListAdapter) {
        Log.d(TAG, "setViewModel")
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        contactViewModel.allContacts.observe(this, Observer<List<Contact>> { list ->
            adapter.insertContacts(list!!.toMutableList())
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                saveContact(data)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "Contact pick cancelled")
            }
        }
    }

    private fun saveContact(contactPicker: Intent?) {
        val contactUri = contactPicker?.data
        val cursor = context?.contentResolver?.query(contactUri, null, null, null, null)
        cursor!!.moveToFirst()
        fetchDataAndSaveInDatabase(cursor)
    }

    private fun fetchDataAndSaveInDatabase(cursor: Cursor) {
        val phoneNumber = getDataFrom(cursor, ContactsContract.CommonDataKinds.Phone.NUMBER)
        val contactName = getDataFrom(cursor, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        cursor.close()

        val contact = Contact(contactName, phoneNumber)
        Log.d(TAG, "Saving contact to database: $contact")

        contactViewModel.insert(contact)
    }

    private fun getDataFrom(cursor: Cursor, dataType: String): String {
        return cursor.getString(cursor.getColumnIndex(dataType))
    }

}