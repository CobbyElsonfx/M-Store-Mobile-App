package com.momokotle.agent.data.contacts

import android.content.Context
import android.provider.ContactsContract
import com.momokotle.agent.utils.PermissionHelper
import com.momokotle.agent.utils.PhoneNumberFormatter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class DeviceContact(
    val id: String,
    val displayName: String,
    val phoneNumber: String
)

@Singleton
class DeviceContactReader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Reads phone-book numbers the agent can save (valid Ghana mobiles).
     * Requires [PermissionHelper.CONTACTS_PERMISSION].
     */
    suspend fun readImportableContacts(): List<DeviceContact> = withContext(Dispatchers.IO) {
        if (!PermissionHelper.hasContactsPermission(context)) return@withContext emptyList()

        val byPhone = LinkedHashMap<String, DeviceContact>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} COLLATE NOCASE ASC"
        )?.use { cursor ->
            val idIdx = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIdx = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                val rawNumber = cursor.getString(numberIdx).orEmpty()
                val phone = PhoneNumberFormatter.normalizeGhanaNumber(rawNumber)
                if (!PhoneNumberFormatter.isValidGhanaNumber(phone)) continue
                if (byPhone.containsKey(phone)) continue

                val name = cursor.getString(nameIdx)
                    ?.trim()
                    .orEmpty()
                    .ifBlank { phone }
                val contactId = cursor.getString(idIdx).orEmpty()
                byPhone[phone] = DeviceContact(
                    id = "${contactId}_$phone",
                    displayName = name,
                    phoneNumber = phone
                )
            }
        }

        byPhone.values.toList()
    }
}
