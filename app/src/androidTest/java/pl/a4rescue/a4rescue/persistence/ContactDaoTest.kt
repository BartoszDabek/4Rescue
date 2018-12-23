package pl.a4rescue.a4rescue.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ContactDaoTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var mDatabase: ContactDatabase
    private lateinit var mContactDao: ContactDao

    companion object {
        private val CONTACT = Contact("username", "500 789 987")
        private val CONTACT_TWO = Contact("username2", "600 123 456")
    }

    @Before
    @Throws(Exception::class)
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext,
                ContactDatabase::class.java)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build()

        mContactDao = mDatabase.contactDao()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        mDatabase.close()
    }

    @Test
    fun getContactWhenNoContactInserted() {
        val contacts = LiveDataTestUtil.getValue(mContactDao.getAll())
        assertEquals(0, contacts.size)
    }

    @Test
    fun insertAndGetContact() {
        mDatabase.contactDao().insert(CONTACT)

        val contacts = LiveDataTestUtil.getValue(mContactDao.getAll())
        assertEquals(1, contacts.size)
        assertEquals(CONTACT.userName, contacts[0].userName)
        assertEquals(CONTACT.phoneNumber, contacts[0].phoneNumber)
    }

    @Test
    fun insertTheSameAndGetContacts() {
        mDatabase.contactDao().insert(CONTACT)
        mDatabase.contactDao().insert(CONTACT)

        val contacts = LiveDataTestUtil.getValue(mContactDao.getAll())
        assertEquals(1, contacts.size)
    }

    @Test
    fun insertTwoDifferentAndGetContacts() {
        mDatabase.contactDao().insert(CONTACT)
        mDatabase.contactDao().insert(CONTACT_TWO)

        val contacts = LiveDataTestUtil.getValue(mContactDao.getAll())
        assertEquals(2, contacts.size)
    }

    @Test
    fun deleteAndGetContact() {
        mDatabase.contactDao().insert(CONTACT)

        mDatabase.contactDao().delete(CONTACT)

        val contacts = LiveDataTestUtil.getValue(mContactDao.getAll())
        assertEquals(0, contacts.size)
    }

    @Test
    fun deleteWrongAndGetContact() {
        mDatabase.contactDao().insert(CONTACT)

        mDatabase.contactDao().delete(CONTACT_TWO)

        val contacts = LiveDataTestUtil.getValue(mContactDao.getAll())
        assertEquals(1, contacts.size)
    }

}