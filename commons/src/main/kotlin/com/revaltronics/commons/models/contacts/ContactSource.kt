package com.revaltronics.commons.models.contacts

import com.revaltronics.commons.helpers.SMT_PRIVATE

data class ContactSource(var name: String, var type: String, var publicName: String, var count: Int = 0) {
    fun getFullIdentifier(): String {
        return if (type == SMT_PRIVATE) {
            type
        } else {
            "$name:$type"
        }
    }
}
